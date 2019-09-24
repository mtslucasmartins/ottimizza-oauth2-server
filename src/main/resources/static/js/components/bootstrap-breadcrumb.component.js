export function BreadcrumItemModel() {
  this.label = '';
  this.href = '';
  this.icon = null;
  this.active = false;

  this.isActive = function (active) {
    this.active = active;
    return this;
  }

  this.withLabel = function (label) {
    this.label = label;
    return this;
  }

  this.withHref = function (href) {
    this.href = href;
    return this;
  }

  this.withIcon = function (icon) {
    this.icon = {};
    let classes = icon.split(' ');
    for (let cls of classes) {
      this.icon[`${cls}`] = true;
    }
    return this;
  }

  this.build = function () {
    return {
      label: this.label, icon: this.icon, href: this.href, active: this.active
    }
  }
};

export let BootstrapBreadcrumbComponent = Vue.component('bootstrap-breadcrumb', {
  props: ['items'],
  template: `
    <nav aria-label="breadcrumb">
      <div class="breadcrumb content-wrapper m-0">
        <div class="container">
          <ol class="breadcrumb m-0">
            <li v-for="item in items" class="breadcrumb-item" 
                v-bind:class="{ active: item.active }">
              <a v-bind:href="item.href" class="">
                <span class="d-sm-inline-block d-md-none">
                  <i v-bind:class="item.icon"></i>
                </span>
                <span v-if="item.icon !== null" class="d-none d-md-inline-block">
                  {{ item.label }}
                </span>
                <span v-if="item.icon === null">
                  {{ item.label }}
                </span>
              </a>
            </li>
          </ol>
        </div>
      </div>
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
