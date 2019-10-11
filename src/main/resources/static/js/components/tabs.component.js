export const TabButton = Vue.component('v-tab-button', {
  props: {
    id: { type: String, default: '' },
    target: { type: String, default: '' },
    active: { type: Boolean, default: false }
  },
  template: `
    <a class="p-2 nav-link" :class="{ active: active }" data-toggle="tab" role="tab"
      :id="id" :href="target" :aria-controls="target" :aria-selected="active">
      <slot></slot>
    </a>
  `,
  methods: {
    onClose(event) { this.$emit('onClose', event); },
    onClick(event) { this.$emit('selected', JSON.parse(JSON.stringify({ selected: event }))); }
  }
});

export const TabContent = Vue.component('v-tab-content', {
  props: {
    value: { type: Object, default: {} }
  },
  template: `
    <div v-on:click="onClick(value)" class="autocomplete-option"><slot v-bind:value="value"></slot></div>
  `,
  methods: {
    onClose(event) { this.$emit('onClose', event); },
    onClick(event) { this.$emit('selected', JSON.parse(JSON.stringify({ selected: event }))); }
  }
});
