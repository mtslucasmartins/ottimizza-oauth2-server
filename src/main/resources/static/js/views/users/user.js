import {
  findOrganizationsByExternalId,
  findCustomersByOrganizationId,
  findCustomersInvitedByOrganizationId,
  patchOrganization,
  fetchInvitedCustomers,
  appendCustomer,
  inviteCustomer
} from './../../services/organizations.service.js';
import {
  fetchCustomers, fetchUserById, patchUser,
} from './../../services/users.service.js';
import { BootstrapBreadcrumbComponent } from './../../components/bootstrap-breadcrumb.component.js';
import { AutocompleteWrapper, Autocomplete, AutocompleteOption } from '../../components/autocomplete.component.js';


const BREADCRUMB = [
  { label: 'Início', icon: { 'fa': true, 'fa-user': true }, href: '/', active: false },
  { label: 'Empresas', icon: { 'fa': true, 'fa-user': true }, href: '/empresas', active: false }
]

let organization = {};

var app = new Vue({
  el: '#app',
  components: {
    'breadcrumb': BootstrapBreadcrumbComponent
  },
  data() {
    return {
      id: null,
      user: {},
      alert: {
        icon: '',
        type: '',
        title: '',
        message: '',
        visible: false
      },
      breadcrumb: BREADCRUMB,
      editingField: null
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
    fetchUserById: async function (id) {
      return fetchUserById(id);
    },
    patchUser: async function (id, user) {
      return patchUser(id, user)
        .then((response) => {
          this.editingField = null;
          this.user = response.record;
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
    that.id = window.location.href.split('usuarios/')[1].replace(/\?.*/, '');
    that.fetchUserById(that.id).then((response) => {
      that.user = response;
      organization = that.user;
      that.breadcrumb = [
        { label: 'Início', icon: { 'fad': true, 'fa-home-alt': true }, href: '/', active: false },
        { label: 'Usuários', icon: { 'fad': true, 'fa-industry-alt': true }, href: '/usuarios', active: false },
        { label: `${that.user.firstName} ${that.user.lastName}`, icon: null, href: `/usuarios/${that.externalId}`, active: true }
      ];
    });
  }
});
