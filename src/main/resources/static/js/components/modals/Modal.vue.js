export let ModalComponent = Vue.component('v-modal', {
  template: `
    <transition name="modal">
      <div class="modal-mask">
        <div class="modal-dialog">
          <div class="modal-content">

            <div class="modal-header">
              <slot name="header">
                default header
              </slot>
            </div>

            <div class="modal-body">
              <slot name="body">
                default body
              </slot>
            </div>

            <div class="modal-footer">
              <slot name="footer">
                default footer
                <button class="modal-default-button" @click="$emit('close')">
                  OK
                </button>
              </slot>
            </div>

          </div>
        </div>
      </div>
    </transition>
  `,
});


// <div class="form-group row">
//   <div class="col-md-3"></div>
//   <label for="field-organization-cnpj" class="col-md-3 label-control">CPF ou CNPJ: </label>
//   <div class="input-group col-md-6">
//     <input id="field-organization-cnpj" v-model="organization.cnpj"
//       :readonly="editingField != 'cnpj'" class="form-control" type="text">
//     <div th:if="${authorizedUser.getType()} == 1" v-if="editingField === 'cnpj'"
//       class="input-group-append">
//       <button v-on:click="patchOrganization(organization.id, {'cnpj': organization.cnpj})"
//         class="btn" style="border: solid 1px #9E9E9E;" type="button">
//         <i class="fa fa-save text-success"></i></span>
//       </button>
//       <button v-on:click="editing(null)" class="btn" style="border: solid 1px #9E9E9E;"
//         type="button">
//         <i class="fa fa-times text-danger"></i>
//       </button>
//     </div>
//     <div th:if="${authorizedUser.getType()} == 1" v-if="editingField !== 'cnpj'"
//       v-on:click="editing('cnpj')" class="input-group-append">
//       <button class="btn" style="border: solid 1px #9E9E9E;" type="button">
//         <i class="fas fa-pencil"></i>
//       </button>
//     </div>
//   </div>
// </div>

export let AdvancedInputComponent = Vue.component('v-input', {
  props: {
    id: { type: String, default: '' },
    value: { type: String, default: '' },
    label: { type: String, default: '' },
    type: { type: String, default: 'text' },
    readonly: { type: Boolean, default: false },
    editable: { type: Boolean, default: false },
  },
  template: `
    <div class="form-group">
      <label :for="id" class="label-control">{{ label }}</label>
      <div class="input-group">
        <input :id="id" :value="value" :readonly="readonly" class="form-control" :type="type" @input="$emit('input', $event.target.value)">
        <slot name="appends" class="input-group-append" style="border: solid 1px #9E9E9E;" >
        </slot>
      </div>
    </div>
  `,
  data: function() {
    return {
    }
  },
});
