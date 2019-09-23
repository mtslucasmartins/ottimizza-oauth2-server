
export let ButtonStyles = (function () {
  return {
    PRIMARY: 'primary',
    SECONDARY: 'secondary',
    SUCCESS: 'success',
    DANGER: 'danger',
    WARNING: 'warning',
    INFO: 'info',
    LIGHT: 'light',
    DARK: 'dark'
  };
})();

export let ButtonComponent = Vue.component('v-button', {
  props: ['buttonStyle'],
  template: `
    <button type="button" class="btn" v-bind:class="classObject">
      <slot></slot>
    </button>
  `,
  computed: {
    classObject: function () {
      return {
        'btn-primary': this.buttonStyle === ButtonStyles.PRIMARY,
        'btn-secondary': this.buttonStyle === ButtonStyles.SECONDARY,
        'btn-success': this.buttonStyle === ButtonStyles.SUCCESS,
        'btn-danger': this.buttonStyle === ButtonStyles.DANGER,
        'btn-warning': this.buttonStyle === ButtonStyles.WARNING,
        'btn-info': this.buttonStyle === ButtonStyles.INFO,
        'btn-light': this.buttonStyle === ButtonStyles.LIGHT,
        'btn-dark': this.buttonStyle === ButtonStyles.DARK
      }
    }
  }
});
