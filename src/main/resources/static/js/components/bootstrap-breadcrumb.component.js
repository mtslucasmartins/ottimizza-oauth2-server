
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

export let BootstrapPaginationComponent = Vue.component('bootstrap-pagination', {
  props: ['pageInfo'],
  template: `
    <nav aria-label="Page navigation">
      <ul class="pagination justify-content-end">
        <li v-on="pageInfo && pageInfo.hasPrevious ? { click: () => onPageChange('first') } : {}"  
          class="page-item" v-bind:class="{ disabled: !pageInfo || !pageInfo.hasPrevious }">
          <a href="#" aria-label="Previous" class="page-link font-medium-2">
            <strong aria-hidden="true">&laquo; </strong> 
            <span class="sr-only">Previous</span>
          </a>
        </li> 
        <li v-on="pageInfo && pageInfo.hasPrevious ? { click: () => onPageChange('previous') } : {}" 
            class="page-item" v-bind:class="{ disabled: !pageInfo || !pageInfo.hasPrevious }">
          <a href="#" aria-label="Previous" class="page-link font-medium-2">
            <strong aria-hidden="true">&lsaquo; </strong> 
            <span class="sr-only">Previous</span>
          </a>
        </li>
        <li v-on="pageInfo && pageInfo.hasNext ? { click: () => onPageChange('next') } : {}" 
            class="page-item" v-bind:class="{ disabled: !pageInfo || !pageInfo.hasNext }">
          <a href="#" aria-label="Previous" class="page-link font-medium-2">
            <strong aria-hidden="true">&rsaquo; </strong> 
            <span class="sr-only">Previous</span>
          </a>
        </li>
        <li v-on="pageInfo && pageInfo.hasNext ? { click: () => onPageChange('last') } : {}" 
            class="page-item" v-bind:class="{ disabled: !pageInfo || !pageInfo.hasNext }">
          <a href="#" aria-label="Next" class="page-link font-medium-2">
            <strong aria-hidden="true"> &raquo;</strong>
            <span class="sr-only">Next</span>
          </a>
        </li>
      </ul>
    </nav>
  `,
  methods: {
    onPageChange(event) {
      this.$emit('change', { selected: event });
    }
  }
});
