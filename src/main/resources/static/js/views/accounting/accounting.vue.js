import { OrganizationService } from './../../services/organizations.service.js';

import { URIService } from '../../services/uri.service.js';
import { StorageService } from '../../services/storage.service.js';
import { ImageCompressionService } from '../../services/image.service.js';


// Vue.js Components
import { BootstrapBreadcrumbComponent } from '../../components/bootstrap-breadcrumb.component.js';
import { ButtonComponent } from '../../components/buttons/Button.vue.js';
import { ModalComponent } from '../../components/modals/Modal.vue.js';



const BREADCRUMB = [
  { label: 'Início', icon: { 'fad': true, 'fa-home': true }, href: '/', active: false }
]

var gAccounting = {};

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
                  <input id="inputGroupFile01" type="file" class="m-0 p-0 d-none" @change="onUpload" aria-describedby="inputGroupFileAddon01">
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
    init() {
      this.status = '';
      this.image = { name: '', status: '', value: '' };
    },
    destroy() {
      this.cropper.destroy();
      this.status = '';
      this.image = { name: '', state: '', uploaded: null, resized: null, cropped: null, };
    },
    reset() { this.cropper.reset(); },
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
          .catch(() => e);
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
    if (this.cropper) this.reset();
    const that = this;
    $("#modal-image-cropper").on('shown.bs.modal', function () {
      var image = document.getElementById('image');
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
  el: '#aside', components: {
    'breadcrumb': BootstrapBreadcrumbComponent,
    'bootstrap-modal': BootstrapModal,
    'v-button': ButtonComponent,
    'v-modal': ModalComponent
  },
  data() {
    return {
      showModal: true,
      accounting: gAccounting,
      breadcrumb: BREADCRUMB,
      editingId: null
    }
  },
  methods: {
    onImageUploaded: async (e) => {
      return ImageCompressionService.compress(e, 800).subscribe()
        .then((response) => response)
        .catch(() => e);
    },
    onImageCropped: async (e) => {
      const file = StorageService.blobToFile(e.blob, e.name);
      const accounting = gAccounting;
      return ImageCompressionService.compress(file).subscribe()
        .then(async function (response) {
          return StorageService.store(StorageService.blobToFile(response, e.name)).subscribe()
            .then(async function (response) {
              const accountingId = accounting.id;
              const data = { avatar: StorageService.getResourceURL(response.record.id) };
              return OrganizationService.patch(accountingId, data).subscribe()
                .then((response) => true)
                .catch(() => false);
            })
            .catch(() => false);
        })
        .catch(() => false);
    }
  },
  created() {
  }
});

const app = new Vue({
  el: '#app',
  components: {
    'breadcrumb': BootstrapBreadcrumbComponent
  },
  data() {
    return {
      accounting: gAccounting,
      breadcrumb: BREADCRUMB,
      editingId: null
    }
  },
  methods: {
    editing: function (field) {
      this.editingId = field;
    },
    patch: async function (id, data) {
      OrganizationService.patch(gAccounting.id, data).subscribe().then((response) => {
        gAccounting = response.record;
      }).then(() => {
        this.accounting = gAccounting;
        this.editing(null);
      });
    }
  },
  created() {
    OrganizationService.fetchOrganizationByExternalId(
      URIService.getLocationPathnameParams('contabilidade/:externalId').externalId
    ).subscribe().then((response) => {
      gAccounting = response;
      this.breadcrumb = [
        { label: 'Início', icon: { 'fad': true, 'fa-home': true }, href: '/', active: false },
        { label: gAccounting.name, icon: null, href: '', active: true }
      ]
    }).then(() => {
      this.accounting = gAccounting;
    });
  }
});
