export function findAllOrganizations(filter = '', pageIndex = 0, pageSize = 10) {
  return new Promise(function (resolve, reject) {
    const url = `/api/organizations?filter=${encodeURI(filter)}&page_index=${pageIndex}&pageSize=${pageSize}`;
    $.get(url, (response) => {
      resolve(response);
    });
  });
};

export function findCustomersByOrganizationId(id) {
  return new Promise(function (resolve, reject) {
    const url = `/api/organizations/${id}/users`;
    $.ajax({
      url: url, type: 'get'
    }).done(function (response) {
      resolve(response);
    }).fail(function (jqXHR, textStatus, response) {
      reject(response);
    });
  });
};

export function findOrganizationsByExternalId(externalId = '') {
  return new Promise(function (resolve, reject) {
    const url = `/api/organizations/uuid/${externalId}`;
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
    const url = `/api/organizations`;
    $.ajax({
      url: '/api/organizations',
      type: 'post',
      data: organization
      // beforeSend: function () {
      //   // $("#resultado").html("ENVIANDO...");
      // }
    }).done(function (response) {
      resolve(response);
    }).fail(function (jqXHR, textStatus, response) {
      reject(response);
    });
  });
};

export function updateOrganization(externalId, organization = {}) {
  return new Promise(function (resolve, reject) {
    const url = `/api/organizations/${externalId}`;
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
