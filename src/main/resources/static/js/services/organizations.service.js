const BASE_URL = '/api/v1/organizations';


export function findAllOrganizations(filter = '', pageIndex = 0, pageSize = 10) {
  return new Promise(function (resolve, reject) {
    const url = `${BASE_URL}?filter=${encodeURI(filter)}&page_index=${pageIndex}&pageSize=${pageSize}`;
    $.get(url, (response) => {
      resolve(response);
    });
  });
};

export function findCustomersByOrganizationId(id) {
  return new Promise(function (resolve, reject) {
    const url = `${BASE_URL}/${id}/customers`;
    $.ajax({
      url: url, type: 'get'
    }).done(function (response) {
      resolve(response);
    }).fail(function (jqXHR, textStatus, response) {
      reject(response);
    });
  });
};

export function findCustomersInvitedByOrganizationId(id) {
  return new Promise(function (resolve, reject) {
    const url = `${BASE_URL}/${id}/customers_invited`;
    $.ajax({
      url: url, type: 'get'
    }).done(function (response) {
      resolve(response);
    }).fail(function (jqXHR, textStatus, response) {
      reject(response);
    });
  });
};

/* **************************************************************************************** **
 * CUSTOMERS
 * **************************************************************************************** */
export function fetchCustomers(id) {
  return new Promise(function (resolve, reject) {
    const url = `${BASE_URL}/${id}/customers`;
    $.ajax({
      url: url, type: 'get'
    }).done(function (response) {
      resolve(response);
    }).fail(function (jqXHR, textStatus, response) {
      reject(response);
    });
  });
};

export function appendCustomer(id, user = {}) {
  return new Promise(function (resolve, reject) {
    const url = `${BASE_URL}/${id}/customers`;
    $.ajax({
      url: url,
      type: 'post',
      dataType: 'json',
      contentType: 'application/json; charset=utf-8',
      data: JSON.stringify(user)
    }).done(function (response) {
      resolve(response);
    }).fail(function (jqXHR, textStatus, response) {
      reject(response);
    });
  });
};

/* **************************************************************************************** **
 * INVITED CUSTOMERS
 * **************************************************************************************** */
export function fetchInvitedCustomers(id) {
  return new Promise(function (resolve, reject) {
    const url = `${BASE_URL}/${id}/customers/invited`;
    $.ajax({
      url: url, type: 'get'
    }).done(function (response) {
      resolve(response);
    }).fail(function (jqXHR, textStatus, response) {
      reject(response);
    });
  });
};

export function inviteCustomer(id, email = '') {
  return new Promise(function (resolve, reject) {
    const url = `${BASE_URL}/${id}/customers/invite`;
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




export function findOrganizationsByExternalId(externalId = '') {
  return new Promise(function (resolve, reject) {
    const url = `${BASE_URL}/uuid/${externalId}`;
    $.ajax({
      url: url, type: 'get'
    }).done(function (response) {
      resolve(response);
    }).fail(function (jqXHR, textStatus, response) {
      reject(response);
    });
  });
};

export function saveOrganization(organization = {}) {
  return new Promise(function (resolve, reject) {
    const url = `${BASE_URL}`;
    $.ajax({
      url: url,
      type: 'post',
      dataType: 'json',
      contentType: 'application/json; charset=utf-8',
      data: JSON.stringify(organization)
    }).done(function (response) {
      resolve(response);
    }).fail(function (jqXHR, textStatus, response) {
      reject(response);
    });
  });
};

export function updateOrganization(externalId, organization = {}) {
  return new Promise(function (resolve, reject) {
    const url = `${BASE_URL}/${externalId}`;
    $.ajax({
      url: url,
      dataType: 'json',
      contentType: 'application/json; charset=utf-8',
      type: 'put',
      data: JSON.stringify(organization)
    }).done(function (response) {
      resolve(response);
    }).fail(function (jqXHR, textStatus, response) {
      reject(jqXHR);
    });
  });
};

export function patchOrganization(id, organization = {}) {
  return new Promise(function (resolve, reject) {
    const url = `${BASE_URL}/${id}`;
    $.ajax({
      url: url, type: 'patch',
      dataType: 'json',
      contentType: 'application/json; charset=utf-8',
      data: JSON.stringify(organization)
    }).done(function (response) {
      resolve(response);
    }).fail(function (jqXHR, textStatus, response) {
      reject(jqXHR);
    });
  });
};

export var OrganizationService = (function () {
  const BASE_URL = `api/v1/organizations`;
  let fetchAllInvitedUsers = function (email = null, pageIndex = 0, pageSize = 10) {
    const url = email === null ? `${BASE_URL}/invited` : `${BASE_URL}/invited?email=${email}`;
    return new Promise(function (resolve, reject) {
      $.ajax({
        url: url,
        type: 'get',
        contentType: 'application/json; charset=utf-8'
      }).done(function (response) {
        resolve(response);
      }).fail(function (jqXHR, textStatus, response) {
        reject(response);
      });
    });
  }

  return {
    fetchAllInvitedUsers: fetchAllInvitedUsers
  };
})();