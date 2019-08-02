import { findOrganizationsByExternalId, updateOrganization } from './../services/organizations.service.js';
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
  data() {
    return {
      alert: {
        icon: '',
        type: '',
        title: '',
        message: '',
        visible: false
      },
      breadcrumb: [
        { label: 'Início', href: '/', active: false },
        { label: 'Empresas', href: '/empresas', active: false }
      ],
      externalId: '',
      organization: { name: '', cnpj: '', codigoERP: '' }
    }
  },
  methods: {
    showBasicInfoAlert: function (message, level) {
      this.alert.visible = true;
      this.alert.type = level;
      this.alert.message = message;
    },
    findOrganizationsByExternalId: function (externalId) {
      return findOrganizationsByExternalId(externalId);
    },
    updateOrganization: async function (externalId, organization) {
      return updateOrganization(externalId, organization)
        .then((response) => {
          this.showBasicInfoAlert('Empresa atualizada com sucesso!', 'success');
        })
        .catch((response) => {
          if (response.status === 409) {
            this.showBasicInfoAlert('Este CPF ou CNPJ já está sendo usado em outra empresa!', 'warning');
          }
        });
    }
  },
  created() {
    this.externalId = window.location.href.split('organizations/')[1].replace(/\?.*/, '');
    this.findOrganizationsByExternalId(this.externalId).then((response) => {
      this.organization = response;
      this.organization = response;
      this.breadcrumb = [
        { label: 'Início', href: '/', active: false },
        { label: 'Empresas', href: '/empresas', active: false },
        { label: response.name, href: `/empresas/${this.externalId}`, active: true }
      ];
    });
  }
});
