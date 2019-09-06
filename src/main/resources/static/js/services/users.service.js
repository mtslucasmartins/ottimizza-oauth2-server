
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


export var UserService = (function() {

  const BASE_URL = `api/v1/users`;

  var privateMethod = function() {
    console.log('Inside a private method!');
  }

  var methodToExpose = function() {
    console.log('This is a method I want to expose!');
  }

  var otherMethodIWantToExpose = function() {
    privateMethod();
  }

  return {
      first: methodToExpose,
      second: otherMethodIWantToExpose
  };
})();