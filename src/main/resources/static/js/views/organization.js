import {
  findOrganizationsByExternalId,
  findCustomersByOrganizationId,
  findCustomersInvitedByOrganizationId,
  patchOrganization,
  fetchInvitedCustomers,
  appendCustomer,
  inviteCustomer
} from './../services/organizations.service.js';
import {
  fetchCustomers,
} from './../services/users.service.js';
import { BootstrapBreadcrumbComponent } from './../components/bootstrap-breadcrumb.component.js';
import { AutocompleteWrapper, Autocomplete, AutocompleteOption } from '../components/autocomplete.component.js';



let organization = {};

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
        { label: 'Início', icon: { 'fa': true, 'fa-user': true }, href: '/', active: false },
        { label: 'Empresas', icon: { 'fa': true, 'fa-user': true }, href: '/empresas', active: false }
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
        return fetchCustomers(email);
      }, 1000);

    },
    findOrganizationsByExternalId: function (externalId) {
      return findOrganizationsByExternalId(externalId);
    },
    findCustomersByOrganizationId: function (id) {
      return findCustomersByOrganizationId(id);
    },
    findCustomersInvitedByOrganizationId: function (id) {
      return fetchInvitedCustomers(id);
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
      organization = response;

      console.log(organization);


      that.findCustomersInvitedByOrganizationId(that.organization.id).then((response) => {
        that.customersInvited = response.records;
      });
      that.findCustomersByOrganizationId(that.organization.id).then((response) => {
        that.customers = response.records;
      });
      that.breadcrumb = [
        { label: 'Início', icon: { 'fad': true, 'fa-home-alt': true }, href: '/', active: false },
        { label: 'Empresas', icon: { 'fad': true, 'fa-industry-alt': true }, href: '/empresas', active: false },
        { label: response.name, icon: null, href: `/empresas/${that.externalId}`, active: true }
      ];
    });
  }
});


var AddUserOrganizationSidebar = new Vue({
  el: '#tab-add-user-content',
  components: {
    'autocomplete-wrapper': AutocompleteWrapper,
    'autocomplete': Autocomplete,
    'autocomplete-option': AutocompleteOption
  },
  data: {
    requestStatus: '',
    user: { email: '' },
    users: [],
    timeout: null,
    isLoading: false
  },
  methods: {
    findUserByEmail: function (email = null) {
      const that = this;
      that.user = { email: email };
      clearTimeout(that.timeout);
      that.timeout = null;
      that.timeout = setTimeout(async function () {
        that.users = [];
        fetchCustomers(email).then((response) => {
          const records = response.records;
          if (records.length == 0) {
            console.log('Users not found.');
          } else {
            that.users = records;
          }
        });
      }, 380);
    },
    appendCustomer: async function (user = {}) {
      console.log('Appending...', user);
      if (user.username) {
        return appendCustomer(organization.id, user).then((response) => {
          console.log(response);
        });
      } else {
        return this.inviteCustomer(user.email);
      }
    },
    inviteCustomer: async function (email) {
      return inviteCustomer(organization.id, email).then((response) => {
        if (response.record) {
          this.requestStatus = 'invited';
        }
      });
    },
    onSelected: function (event) {
      this.user = {};
      this.user = event.selected;
      this.users = [];
    }
  }
});