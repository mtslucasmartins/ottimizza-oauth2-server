import { findOrganizationsByExternalId, saveOrganization } from './../services/organizations.service.js';
import { BootstrapBreadcrumbComponent } from './../components/bootstrap-breadcrumb.component.js';

// let organizationBasicInfo = new Vue({
//   el: '#organization-basic-info-card',
//   data: {
//     loading: false,
//     breadcrumb: [
//       { label: 'Início', href: '/', active: false },
//       { label: 'Usuários', href: '/usuarios', active: false },
//       { label: 'Lucas Martins', href: '/usuarios/1', active: false }
//     ],
//     organization: { name: '', cnpj: '', codigoERP: '' }
//   },
//   methods: {
//     findOrganizationsByExternalId: (externalId) => {
//       return findOrganizationsByExternalId(externalId);
//     },
//     saveOrganization: async (organization = {}) => {
//       return saveOrganization(organization).then((response) => {
//         const organization = response;
//         window.location = `/organizations/${organization.externalId}`;
//       });;
//     }
//   },
//   created() {
//     const externalId = window.location.href.split('organizations/')[1].replace(/\?.*/, '');
//     this.findOrganizationsByExternalId(externalId).then((response) => {
//       this.organization = response;
//     });
//   }
// });

var app = new Vue({
  el: '#app',
  components: { 'breadcrumb': BootstrapBreadcrumbComponent },
  data: {
    breadcrumb: [
      { label: 'Início', href: '/', active: false },
      { label: 'Empresas', href: '/usuarios', active: false },
      { label: 'Lucas Martins', href: '/usuarios/1', active: false }
    ],
    organization: { name: '', cnpj: '', codigoERP: '' }
  },
  methods: {
    findOrganizationsByExternalId: (externalId) => {
      return findOrganizationsByExternalId(externalId);
    },
    saveOrganization: async (organization = {}) => {
      return saveOrganization(organization).then((response) => {
        const organization = response;
        window.location = `/organizations/${organization.externalId}`;
      });;
    }
  },
  created() {
    const externalId = window.location.href.split('organizations/')[1].replace(/\?.*/, '');
    this.findOrganizationsByExternalId(externalId).then((response) => {
      this.organization = response;
    });
  }
});
