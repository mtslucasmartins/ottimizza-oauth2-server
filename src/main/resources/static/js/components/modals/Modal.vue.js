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
  methods: {

  }
});