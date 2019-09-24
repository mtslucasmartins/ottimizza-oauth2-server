import { HttpClient } from './http.service.js';

const BASE_URL = '/api/v1/users';

export function findUserByEmail(filter = '', pageIndex = 0, pageSize = 10) {
  return new Promise(function (resolve, reject) {
    const url = `/api/users?filter=${encodeURI(filter)}&page_index=${pageIndex}&pageSize=${pageSize}`;
    $.get(url, (response) => {
      resolve(response);
    });
  });
};

export function fetchCustomers(email = '', pageIndex = 0, pageSize = 10) {
  return new Promise(function (resolve, reject) {
    const url = `/api/v1/customers?email=${encodeURI(email)}&page_index=${pageIndex}&pageSize=${pageSize}`;
    $.get(url, (response) => {
      resolve(response);
    });
  });
};


export function fetchUsers(email = '', pageIndex = 0, pageSize = 10) {
  return new Promise(function (resolve, reject) {
    const url = `/api/v1/users?email=${encodeURI(email)}&page_index=${pageIndex}&pageSize=${pageSize}`;
    $.get(url, (response) => {
      resolve(response);
    });
  });
};

export function fetchUserById(id) {
  return new Promise(function (resolve, reject) {
    const url = `/api/v1/users/${id}`;
    $.get(url, (response) => {
      resolve(response);
    });
  });
};

export function patchUser(id, user = {}) {
  return new Promise(function (resolve, reject) {
    const url = `/api/v1/users/${id}`;
    $.ajax({
      url: url, type: 'patch',
      dataType: 'json',
      contentType: 'application/json; charset=utf-8',
      data: JSON.stringify(user)
    }).done(function (response) {
      resolve(response);
    }).fail(function (jqXHR, textStatus, response) {
      reject(jqXHR);
    });
  });
};




export function invite(email = '') {
  return new Promise(function (resolve, reject) {
    const url = `${BASE_URL}/invite`;
    $.ajax({
      url: url,
      type: 'post',
      dataType: 'json',
      contentType: 'application/json; charset=utf-8',
      data: JSON.stringify({ email: email })
    }).done(function (response) {
      resolve(response);
    }).fail(function (jqXHR, textStatus, response) {
      reject(response);
    });
  });
};


export var UserService = (function () {
  var endpoint = '/api/v1/users'
  const controller = {

    fetchAllInvitedUsers(email = null, pageIndex = 0, pageSize = 10) {
      const url = email === null ? `${endpoint}/invited` : `${endpoint}/invited?email=${email}`;
      const headers = { 'Content-Type': 'application/json' };
      const options = { responseType: 'json' };
      return HttpClient.get(url, headers, options);
    },

    patch(id, data) {
      const headers = { 'Content-Type': 'application/json' };
      const options = { responseType: 'json' };
      return HttpClient.patch(`${endpoint}/${id}`, data, headers, options);
    }
  }

  return controller
})();



const UserAPIService = (() => {
  // Private methods and variables can be defined isolated in the scope of the Service/Store
  var endpoint = '/api/v1/users'

  // All public methods and variables are available in the controller
  const controller = {
    fetchById(id) {
      return new Promise((resolve, reject) => {
        fetch(`${endpoint}/${id}`, {
          method: 'GET',
          headers: {
            'Content-type': 'application/json'
          },
          data: JSON.stringify(data)
        }).then((response) => {
          return response.json()
        }).then((json) => {
          resolve(json)
        })
      })
    },
    patch(id, data = {}) {
      return new Promise((resolve, reject) => {
        fetch(`${endpoint}/${id}`, {
          method: 'PATCH',
          headers: {
            'Content-type': 'application/json'
          },
          data: JSON.stringify(data)
        }).then((response) => {
          return response.json()
        }).then((json) => {
          resolve(json)
        })
      })
    }
  }

  return controller
})();
