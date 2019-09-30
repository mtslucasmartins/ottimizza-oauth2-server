import { findAllOrganizations, saveOrganization } from './../services/organizations.service.js';
import { BootstrapBreadcrumbComponent, BootstrapPaginationComponent } from './../components/bootstrap-breadcrumb.component.js';
import { OrganizationService } from '../services/api/organizations.service.js';

Vue.component('suspension-points-animation', {
  template: `
    <div class="suspension-points">
      <div class="bounce1" title="Carregando..."></div>
      <div class="bounce2" title="Carregando..."></div>
      <div class="bounce3" title="Carregando..."></div>
    </div>
  `
});

var app = new Vue({
  el: '#app',
  components: {
    'breadcrumb': BootstrapBreadcrumbComponent,
    'bootstrap-pagination': BootstrapPaginationComponent
  },  
  data: {
    loading: false,
    breadcrumb: [
      { label: 'Início', href: '/', active: false },
      { label: 'Empresas', href: '/empresas', active: true },

    ],
    pageInfo: { pageIndex: 0, pageSize: 10, totalPages: 0 },
    organizations: []
  },
  methods: {
    fetchAll: async function (filter = {}) {
      this.loading = true;
      return OrganizationService.fetchAll({}, this.pageInfo.pageIndex, this.pageInfo.pageSize).subscribe()
        .then((response) => {
          this.organizations = response.records;
          this.pageInfo = response.pageInfo;
        }).then(() => this.loading = false);
    },
    onPageChange: function (event) {
      if (event.selected === 'first') this.pageInfo.pageIndex = 0;

      if (event.selected === 'previous')
        this.pageInfo.pageIndex = (this.pageInfo.pageIndex - 1 < 0) ? 0 : this.pageInfo.pageIndex - 1;

      if (event.selected === 'next')
        this.pageInfo.pageIndex = this.pageInfo.pageIndex + 1 > this.pageInfo.totalPages ? this.pageInfo.totalPages - 1 : this.pageInfo.pageIndex + 1;

      if (event.selected === 'last')
        this.pageInfo.pageIndex = this.pageInfo.totalPages - 1;

      this.fetchAll();
    }
  },
  created() {
    this.fetchAll();
  }
});

var organizationSidebarTab = new Vue({
  el: '#organization-create-tab',
  data: {
    loading: false,
    organization: { name: '', cnpj: '', codigoERP: '' }
  },
  methods: {
    findAllOrganizations: (filter = '', pageIndex = 0, pageSize = 10) => {
      return findAllOrganizations(filter, pageIndex, pageSize)
    },
    saveOrganization: async (organization = {}) => {
      if (organization) {
        if (typeof organization.name === 'undefined'
          || organization.name === null || organization.name === '') {
          return;
        }
        if (typeof organization.cnpj === 'undefined'
          || organization.cnpj === null || organization.cnpj === '') {
          return;
        }
        if (typeof organization.codigoERP === 'undefined'
          || organization.codigoERP === null || organization.codigoERP === '') {
          return;
        }
        return saveOrganization(organization).then((response) => {
          const organization = response;
          window.location = `/organizations/${organization.externalId}`;
        });;
      }
    }
  },
  created() { }
});


// Triggers
$('.organization-create-sidebar-cancel').on('click', function () {
  $('.notification-sidebar').removeClass('open');
});


