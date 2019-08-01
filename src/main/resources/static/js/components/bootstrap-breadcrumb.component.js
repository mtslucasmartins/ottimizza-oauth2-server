
export let BootstrapBreadcrumbComponent = Vue.component('bootstrap-breadcrumb', {
  props: ['items'],
  template: `
    <nav aria-label="breadcrumb">
      <ol class="breadcrumb">
        <li v-for="item in items" class="breadcrumb-item"  v-bind:class="{ active: item.active }">
          <a v-bind:href="item.href" class="">
            {{ item.label }}
          </a>
        </li>
      </ol>
    </nav>
  `
});
