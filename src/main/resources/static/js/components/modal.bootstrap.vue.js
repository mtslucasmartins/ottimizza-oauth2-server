$('#meuModal').on('shown.bs.modal', function () {
  $('#meuInput').trigger('focus')
})


export const BootstrapModal = Vue.component('bootstrap-modal', {
  props: { value: { type: Object, default: {} } },
  template: `
    <div v-on:click="onClick(value)" class="autocomplete-option"><slot v-bind:value="value"></slot></div>
  `,
  methods: {
    onClose(event) { this.$emit('onClose', event); },
    onClick(event) { this.$emit('selected', JSON.parse(JSON.stringify({ selected: event }))); }
  }
});
