import { findAllOrganizations, saveOrganization } from './../../services/organizations.service.js';
import { BootstrapBreadcrumbComponent, BootstrapPaginationComponent } from './../../components/bootstrap-breadcrumb.component.js';
import { fetchUsers, invite, UserService } from '../../services/users.service.js';


const animation = Vue.component('suspension-points-animation', {
  template: `
    <div class="suspension-points">
      <div class="bounce1" title="Carregando..."></div>
      <div class="bounce2" title="Carregando..."></div>
      <div class="bounce3" title="Carregando..."></div>
    </div>
  `
});

export const InvitedUsersTable = Vue.component('table-invited-users', {
  props: { users: { type: Array, default: function () { return [] } }, loading: { type: Boolean, default: false } },
  template: `
    <div class="col-sm-12">
      <!-- style="border: 1px solid rgb(222, 226, 230);" -->
      <table class="table table-striped">
        <thead>
          <tr>
            <th class="w-auto border-0">E-mail</th>
            <th class="w-auto border-0">Empresa</th>
            <th class="w-auto border-0">Tipo</th>
            <th class="w-auto border-0">Ações</th>
          </tr>
        </thead>
        <tbody>
          <tr v-cloak v-if="!loading" v-for="user in users">
            <td class="w-25"><a v-on:click="copyRegisterURLToClipboard(user.token)"> {{ user.email }} </a></td>
            <td class="w-auto">{{ user.organization.name }}</td>
            <td class="w-25">{{ user.type == 1 ? 'Contabilidade' : 'Cliente' }}</td>
            <td class="w-auto">
              <a v-on:click="copyRegisterURLToClipboard(user.token)"><i class="far fa-copy"></i>&nbsp;&nbsp;</a>
              <a v-bind:href="'/register?token=' + user.token" target="_blank"><i class="far fa-external-link"></i></a>
            </td>
          </tr>
          <tr v-if="loading" class="js-table-row-loader bg-white">
            <td class="p-2"><suspension-points-animation></suspension-points-animation></td>
            <td class="p-2"><suspension-points-animation></suspension-points-animation></td>
            <td class="p-2"><suspension-points-animation></suspension-points-animation></td>
            <td class="p-2"><suspension-points-animation></suspension-points-animation></td>
          </tr>
        </tbody>
      </table>
      <div class="col">
        <bootstrap-pagination v-bind:page-info="pageInfo" @change="onPageChange($event)">
        </bootstrap-pagination>
      </div>
    </div>
`,
  data: function () {
    return {
      pageInfo: {
        pageIndex: 0,
        pageSize: 10,
        totalPages: 0
      },
    }
  },
  methods: {
    copyRegisterURLToClipboard: function (token) {
      function copyToClipboard(text) {
        var dummy = document.createElement("textarea");
        // to avoid breaking orgain page when copying more words
        // cant copy when adding below this code
        // dummy.style.display = 'none'
        document.body.appendChild(dummy);
        //Be careful if you use texarea. setAttribute('value', value), which works with "input" does not work with "textarea". – Eduard
        dummy.value = text;
        dummy.select();
        document.execCommand("copy");
        document.body.removeChild(dummy);
      }
      /* Get the text field */
      const hostname = window.location.host;

      const registerURL = `${hostname}/register?token=${token}`;

      copyToClipboard(registerURL);
    },
    onPageChange: function (el) {
      if (event.selected === 'first')
        this.pageInfo.pageIndex = 0;
      if (event.selected === 'previous')
        this.pageInfo.pageIndex = (this.pageInfo.pageIndex - 1 < 0) ? 0 : this.pageInfo.pageIndex - 1;
      if (event.selected === 'next')
        this.pageInfo.pageIndex = this.pageInfo.pageIndex + 1 > this.pageInfo.totalPages ? this.pageInfo.totalPages - 1 : this.pageInfo.pageIndex + 1;
      if (event.selected === 'last')
        this.pageInfo.pageIndex = this.pageInfo.totalPages - 1;
      this.loading = true;
      this.findAllOrganizations().then((response) => {
        this.loading = false;
        this.organizations = response.records;
        this.pageInfo = response.pageInfo;
      });
    },
  },
  mounted() {
  }
});


const BREADCRUMB = [
  { label: 'Início', href: '/', active: false },
  { label: 'Usuários', href: '/usuarios', active: true },
]


// Tabela com Lista de organizações relacionadas ao Usuário logado.
let organizationsTable = new Vue({
  el: '#app',
  components: {
    'breadcrumb': BootstrapBreadcrumbComponent,
    'bootstrap-pagination': BootstrapPaginationComponent,
    'table-invited-users': InvitedUsersTable
  },
  data: {
    loading: false,
    breadcrumb: BREADCRUMB,
    pageInfo: {
      pageIndex: 0,
      pageSize: 10,
      totalPages: 0
    },
    organizations: [],
    invitedUsers: []
  },
  methods: {
    findAllOrganizations: function (filter = '', pageIndex = this.pageInfo.pageIndex, pageSize = this.pageInfo.pageSize) {
      return fetchUsers(filter, pageIndex, pageSize);
    },
    fetchAllInvitedUsers: function (filter = null, pageIndex = this.pageInfo.pageIndex, pageSize = this.pageInfo.pageSize) {
      return UserService.fetchAllInvitedUsers(filter, pageIndex, pageSize);
    },
    onPageChange: function (event) {
      if (event.selected === 'first')
        this.pageInfo.pageIndex = 0;
      if (event.selected === 'previous')
        this.pageInfo.pageIndex = (this.pageInfo.pageIndex - 1 < 0) ? 0 : this.pageInfo.pageIndex - 1;
      if (event.selected === 'next')
        this.pageInfo.pageIndex = this.pageInfo.pageIndex + 1 > this.pageInfo.totalPages ? this.pageInfo.totalPages - 1 : this.pageInfo.pageIndex + 1;
      if (event.selected === 'last')
        this.pageInfo.pageIndex = this.pageInfo.totalPages - 1;
      this.loading = true;
      this.findAllOrganizations().then((response) => {
        this.loading = false;
        this.organizations = response.records;
        this.pageInfo = response.pageInfo;
      });
    }
  },
  created() {
    this.loading = true;
    this.findAllOrganizations().then((response) => {
      this.loading = false;
      this.organizations = response.records;
      this.pageInfo = response.pageInfo;
      console.log(this.pageInfo);
    });
    this.fetchAllInvitedUsers().subscribe().then((response) => {
      this.invitedUsers = response.records;
      console.log(response);
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



var AddUserOrganizationSidebar = new Vue({
  el: '#tab-invite-user',
  data: {
    email: '',
    requestStatus: '',
    isLoading: false
  },
  methods: {
    invite: async function (email) {
      this.isLoading = true;
      return invite(email).then((response) => {
        if (response.record) this.requestStatus = 'invited';
      }).then(() => this.isLoading = false);
    }
  }
});