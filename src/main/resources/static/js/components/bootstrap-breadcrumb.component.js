
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
          <a href="#" aria-label="Previous" class="page-link">
            <span aria-hidden="true">&laquo; </span> 
            <span class="sr-only">Previous</span>
          </a>
        </li> 
        <li v-on="pageInfo && pageInfo.hasPrevious ? { click: () => onPageChange('previous') } : {}" 
            class="page-item" v-bind:class="{ disabled: !pageInfo || !pageInfo.hasPrevious }">
          <a href="#" aria-label="Previous" class="page-link">
            <span aria-hidden="true">&lsaquo; </span> 
            <span class="sr-only">Previous</span>
          </a>
        </li>
        <li v-on="pageInfo && pageInfo.hasNext ? { click: () => onPageChange('next') } : {}" 
            class="page-item" v-bind:class="{ disabled: !pageInfo || !pageInfo.hasNext }">
          <a href="#" aria-label="Previous" class="page-link">
            <span aria-hidden="true">&rsaquo; </span> 
            <span class="sr-only">Previous</span>
          </a>
        </li>
        <li v-on="pageInfo && pageInfo.hasNext ? { click: () => onPageChange('last') } : {}" 
            class="page-item" v-bind:class="{ disabled: !pageInfo || !pageInfo.hasNext }">
          <a href="#" aria-label="Next" class="page-link">
            <span aria-hidden="true"> &raquo;</span>
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
