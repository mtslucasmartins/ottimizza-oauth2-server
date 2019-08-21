
export const AutocompleteOption = Vue.component('autocomplete-option', {
  props: { value: { type: Object, default: {} } },
  template: `<div v-on:click="onClick(value)" class="autocomplete-option"><slot v-bind:value="value"></slot></div>`,
  methods: {
    onClick(event) { this.$emit('selected', JSON.parse(JSON.stringify({ selected: event }))); }
  }
});

export const Autocomplete = Vue.component('autocomplete', {
  template: `<div class="autocomplete-items"><slot></slot></div>`
});

export const AutocompleteWrapper = Vue.component('autocomplete-wrapper', {
  props: { visible: { type: Boolean, default: false } },
  template: `<div class="autocomplete" style="width:300px;"><slot></slot></div>`,
  data: function () {
    return {
      KEY_ARROW_DOWN: 40,
      KEY_ARROW_ENTER: 40,
      KEY_ARROW_UP: 40,
      currentFocus: -1
    }
  },
  methods: {
    addActive: function (el) {
      if (!el) return false;
      this.removeActive(el);
      if (this.currentFocus >= el.length) this.currentFocus = 0;
      if (this.currentFocus < 0) this.currentFocus = (el.length - 1);
      el[this.currentFocus].classList.add("autocomplete-active");
    },
    removeActive: function (el) {
      for (var i = 0; i < el.length; i++) {
        el[i].classList.remove("autocomplete-active");
      }
    }
  },
  mounted() {
    let that = this;
    let input = this.$el.querySelector('input');
    // input.addEventListener('focus', function () {
    //   that.$el.querySelector('.autocomplete-items').classList.toggle("hide");
    // });
    // input.addEventListener('blur', function (e) {
    //   that.$el.querySelector('.autocomplete-items').classList.toggle("hide");
    // });
    input.addEventListener('keydown', function (e) {
      let autocompleteItems = that.$el.querySelector('.autocomplete-items');
      if (autocompleteItems) {
        autocompleteItems = autocompleteItems.getElementsByClassName("autocomplete-option");
      }
      if (e.keyCode == 40) { // down
        that.currentFocus++;
        that.addActive(autocompleteItems);
      } else if (e.keyCode == 38) { // up
        that.currentFocus--;
        that.addActive(autocompleteItems);
      } else if (e.keyCode == 13) { // enter
        e.preventDefault();
        if (that.currentFocus > -1) {
          autocompleteItems[that.currentFocus].click();
        }
      }
    });
  }
});