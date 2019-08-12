import {
  findOrganizationsByExternalId,
  findCustomersByOrganizationId,
  findCustomersInvitedByOrganizationId,
  patchOrganization
} from './../services/organizations.service.js';
import {
  findUserByEmail,
} from './../services/users.service.js';
import { BootstrapBreadcrumbComponent } from './../components/bootstrap-breadcrumb.component.js';


var app = new Vue({
  el: '#app',
  components: {
    'breadcrumb': BootstrapBreadcrumbComponent
  },
  data() {
    return {
      user: { email: '' },
      userInputTimeout: null,
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
      customers: [],
      customersInvited: []
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
    findUserByEmail: function (email = '') {
      console.log('find by email');

      clearTimeout(userInputTimeout);
      userInputTimeout = setTimeout(function () {
        return findUserByEmail(email);
      }, 1000);

    },
    findOrganizationsByExternalId: function (externalId) {
      return findOrganizationsByExternalId(externalId);
    },
    findCustomersByOrganizationId: function (id) {
      return findCustomersByOrganizationId(id);
    },
    findCustomersInvitedByOrganizationId: function (id) {
      return findCustomersInvitedByOrganizationId(id);
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
      that.findCustomersInvitedByOrganizationId(that.organization.id).then((response) => {
        that.customersInvited = response.records;
      });
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


var AddUserOrganizationSidebar = new Vue({
  el: '#tab-add-user-content',
  data: {
    user: { email: '' },
    timeout: null,
    isLoading: false
  },
  methods: {
    findUserByEmail: function (email = null) {
      const that = this;
      // if (!email == null && email != '') return;
      clearTimeout(that.timeout);
      that.timeout = null;
      that.timeout = setTimeout(function () {
        return findUserByEmail(email);
      }, 1000);
    }
  }
});