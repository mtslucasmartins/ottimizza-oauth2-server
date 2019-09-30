// VueJS Components
import { BootstrapBreadcrumbComponent, BreadcrumItemModel } from './../../components/bootstrap-breadcrumb.component.js';
import { ModalComponent, AdvancedInputComponent } from '../../components/modals/Modal.vue.js';
import { ButtonComponent } from '../../components/buttons/Button.vue.js';

// services 
import { URIService } from '../../services/uri.service.js';
import { UserService } from '../../services/api/users.service.js';
import { ImageCompressionService } from '../../services/image.service.js';
import { StorageService } from '../../services/storage.service.js';
import { EventBus } from '../../services/event-bus.js';

var gUser = {};

var app = new Vue({
  el: '#app',
  components: {
    'breadcrumb': BootstrapBreadcrumbComponent,
    'v-button': ButtonComponent,
    'v-modal': ModalComponent,
    'v-input': AdvancedInputComponent
  },
  data() {
    return {
      id: null, user: gUser, pwd: { password: '', newPassword: '', inputType: 'password' },
      breadcrumb: [
        new BreadcrumItemModel().withLabel('Início').withIcon('fad fa-home-alt').withHref('/').build(),
        new BreadcrumItemModel().withLabel('Usuários').withIcon('fad fa-users').withHref('/usuarios').build()
      ],
      editingField: null
    }
  },
  methods: {
    updateBreadcrumb: function (user = this.user) {
      const fullname = `${user.firstName} ${user.lastName}`;
      this.breadcrumb = [
        new BreadcrumItemModel().withLabel('Início').withIcon('fad fa-home-alt').withHref('/').build(),
        new BreadcrumItemModel().withLabel('Usuários').withIcon('fad fa-users').withHref('/usuarios').build(),
        new BreadcrumItemModel().withLabel(fullname).withHref(`/usuarios/${user.id}`).isActive(true).build()
      ];
    },
    log: function (e) {
      console.log(e);

    },
    editing: function (field) {
      this.editingField = field;
    },
    updatePassword: function () {
      if (this.pwd.password && this.pwd.newPassword) {
        this.patch(this.user.id, {
          password: this.pwd.password,
          newPassword: this.pwd.newPassword
        });
      }
    },
    update: function (data) {
      gUser = this.user = data;
      console.log(gUser);
      this.updateBreadcrumb();
      this.editing()
    },
    fetch: async function (id = this.user.id) {
      return UserService.fetchById(id).subscribe()
        .then((response) => this.update(response.record));
    },
    patch: async function (id = this.user.id, data) {
      return UserService.patch(id, data).subscribe()
        .then((response) => this.update(response.record));
    }
  },
  created() {
    this.fetch(URIService.getLocationPathnameParams('usuarios/:id').id)
  }
});

export const BootstrapModal = Vue.component('bootstrap-modal', {
  props: {
    callback: { type: Function, default: {} },
    onUploadCallback: { type: Function, default: function () { return {} } },
    feedbacks: {
      type: Object, default: function () {
        return {
          'success': 'Imagem salva com sucesso!',
          'failed': 'Não foi possível fazer o upload da imagem!'
        }
      }
    }
  },
  template: `
    <div id="modal-image-cropper" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
    aria-hidden="true">
      <div class="modal-dialog" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <h5 id="exampleModalLabel" class="modal-title">
              Alterar Logo da Contabilidade
            </h5>
            <button type="button" class="close" data-dismiss="modal" aria-label="Fechar" v-on:click="dismiss">
              <i class="far fa-times"></i>
            </button>
          </div>
          <div class="modal-body">
            <div class="container">
              <div class="row">
                <div class="col justify-content-center text-center">
                <div v-if="status === 'success'"
                    class="alert bg-success alert-icon-left alert-dismissible mb-3" role="alert">
                    {{ feedbacks.success }}
                  </div>
                  <div v-if="status === 'failed'"
                    class="alert bg-danger alert-icon-left alert-dismissible mb-3" role="alert">
                    {{ feedbacks.failed }}
                  </div>
                  <label for="inputGroupFile01" class="btn btn-outline-primary">
                    Escolher Arquivo
                  </label>
                  <input ref="fileInput" id="inputGroupFile01" type="file" class="m-0 p-0 d-none" @change="onUpload" aria-describedby="inputGroupFileAddon01">
                  <div class="w-100">
                    <img id="image" v-bind:src="image.uploaded" class="cropper-hidden">
                    <div id="result">
                      <img src="" />
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <div class="container">
              <div class="row">
                <div class="col-6">
                  <button type="button" class="btn btn-danger w-100" data-dismiss="modal">
                    Cancelar
                  </button>
                </div>
                <div class="col-6">
                  <v-button class="btn-primary w-100" 
                          v-on:click.native="crop(callback)" 
                          :disabled="image.uploaded === null">
                    Cortar & Salvar
                  </v-button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  `,
  data: function () {
    return {
      Status: (function () {
        return {
          COMPRESSING: 'compressing',
          STORING: 'storing',
          SAVING: 'saving'
        }
      })(),
      status: '',
      cropper: null,
      image: {
        name: '',
        value: '',
        state: '',
        uploaded: null,
        resized: null,
        cropped: null,
      }
    }
  },
  methods: {
    resetInput: function () {
      const input = this.$refs.fileInput;
      input.type = 'text';
      input.type = 'file';
    },
    init() {
      this.status = '';
      this.image = { name: '', status: '', value: '' };
    },
    destroy() {
      this.cropper.destroy();
      this.status = '';
      this.image = { name: '', state: '', uploaded: null, resized: null, cropped: null, };
    },
    reset() {
      this.resetInput();
      this.cropper.reset();
    },
    update(src) { this.cropper.reset(); this.cropper.replace(src); },
    dismiss() { this.destroy(); },
    onUpload(event) {
      const that = this;
      if (event.target.files && event.target.files[0]) {
        let file = event.target.files[0];
        const reader = new FileReader();
        reader.onload = function (e) {
          that.image.name = file.name;
          that.image.uploaded = reader.result;
          that.update(that.image.uploaded);
        }
        ImageCompressionService.compress(file, 460)
          .subscribe()
          .then((response) => {
            reader.readAsDataURL(response);
          })
          .catch(() => e).then(() => {
            var image = document.getElementById('image');
            var croppable = false;
            that.cropper = new Cropper(image, {
              zoomable: false,
              aspectRatio: 1,
              viewMode: 1,
              ready: function () {
                croppable = true;
              },
            });
          });
      }
    },
    crop: function (callback) {
      const that = this;
      this.cropper.getCroppedCanvas().toBlob((blob) => {
        if (callback) {
          callback({ blob: blob, name: that.image.name }).then((status) => {
            if (status === true) {
              that.status = 'success';
              console.log(this.status);
            }
          });
        }
      });
    }
  },
  mounted() {
    const that = this;
    // Quando Modal é Aberto...
    $("#modal-image-cropper").on('shown.bs.modal', function () {

      // Reseta input para poder enviar a mesma imagem novamente.
      that.resetInput();
      if (that.cropper) that.update('');
      var image = document.getElementById('image');
      console.log(image);
      image.classList.add("cropper-hidden");

      var croppable = false;
      that.cropper = new Cropper(image, {
        zoomable: false,
        aspectRatio: 1,
        viewMode: 1,
        ready: function () {
          croppable = true;
        },
      });
    });
  }
});

const aside = new Vue({
  el: '#aside',
  components: {
    'bootstrap-modal': BootstrapModal,
    'v-button': ButtonComponent,
    'v-modal': ModalComponent
  },
  methods: {
    onImageUploaded: async (e) => {
      return ImageCompressionService.compress(e, 800).subscribe()
        .then((response) => response)
        .catch(() => e);
    },
    onImageCropped: async function (e) {
      const file = StorageService.blobToFile(e.blob, e.name);
      console.log(gUser);
      const user = gUser;
      return ImageCompressionService.compress(file).subscribe()
        .then(async function (response) {
          return StorageService.store(StorageService.blobToFile(response, e.name)).subscribe()
            .then(async function (response) {
              return UserService.patch(user.id, {
                avatar: StorageService.getResourceURL(response.record.id)
              }).subscribe()
                .then((response) => true).catch(() => false)
                .then(() => window.location.reload(true));
            })
            .catch(() => false);
        })
        .catch(() => false);
    }
  }, created() {




    EventBus.$on('i-got-clicked', clickCount => {
      console.log(`Oh, that's nice. It's gotten ${clickCount} clicks! :)`)
    });
  }
});
