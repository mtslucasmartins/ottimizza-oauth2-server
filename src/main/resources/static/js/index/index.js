import { findAllOrganizations, saveOrganization } from './../services/organizations.service.js';

Vue.component('suspension-points-animation', {
  template: `
    <div class="suspension-points">
      <div class="bounce1" title="Carregando..."></div>
      <div class="bounce2" title="Carregando..."></div>
      <div class="bounce3" title="Carregando..."></div>
    </div>
  `
});



// Tabela com Lista de organizações relacionadas ao Usuário logado.
var organizationsTable = new Vue({
  el: '#organizations-table',
  data: {
    loading: false,
    organizations: []
  },
  methods: {
    findAllOrganizations: (filter = '', pageIndex = 0, pageSize = 10) => {
      return findAllOrganizations(filter, pageIndex, pageSize);
    }
  },
  created() {
    this.loading = true;
    this.findAllOrganizations().then((organizations) => {
      this.loading = false;
      this.organizations = organizations;
    });
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
    saveOrganization: (organization = {}) => {
      return saveOrganization(organization).then((response) => {
        const organization = response;
        window.location = `/organizations/${organization.externalId}`;
      });;
    }
  },
  created() { }
});


// Triggers
$('.organization-create-sidebar-cancel').on('click', function () {
  $('.notification-sidebar').removeClass('open');
});


