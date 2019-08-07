import {
  findOrganizationsByExternalId,
  findCustomersByOrganizationId,
  patchOrganization
} from './../services/organizations.service.js';
import { BootstrapBreadcrumbComponent } from './../components/bootstrap-breadcrumb.component.js';

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
      editingField: null,
      externalId: '',
      organization: { id: null, name: '', cnpj: '', codigoERP: '' },
      customers: []
    }
  },
  methods: {
    editing: function (field) {
      this.editingField = field;
    },
    showBasicInfoAlert: function (message, level) {
      this.alert.visible = true;
      this.alert.type = level;
      this.alert.message = message;
    },
    findOrganizationsByExternalId: function (externalId) {
      return findOrganizationsByExternalId(externalId);
    },
    findCustomersByOrganizationId: function (id) {
      return findCustomersByOrganizationId(id);
    },
    patchOrganization: async function (id, organization) {
      return patchOrganization(id, organization)
        .then((response) => {
          this.editingField = null;
          this.organization = response.record;
        })
        .catch((response) => {
          if (response.status === 409) {
            this.showBasicInfoAlert('Este CPF ou CNPJ já está sendo usado em outra empresa!', 'warning');
          }
        });
    }
  },
  created() {
    const that = this;
    that.externalId = window.location.href.split('organizations/')[1].replace(/\?.*/, '');
    that.findOrganizationsByExternalId(that.externalId).then((response) => {
      that.organization = response;
      that.findCustomersByOrganizationId(that.organization.id).then((response) => {
        that.customers = response.records;
      });
      that.breadcrumb = [
        { label: 'Início', href: '/', active: false },
        { label: 'Empresas', href: '/empresas', active: false },
        { label: response.name, href: `/empresas/${that.externalId}`, active: true }
      ];
    });
  }
});
