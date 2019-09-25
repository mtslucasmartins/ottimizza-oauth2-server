import { URIService } from '../uri.service.js';
import { HttpClient } from '../http.service.js';

export var UserService = (function () {
  var endpoint = '/api/v1/users';

  var headers = { 'Content-Type': 'application/json' };
  var options = { 'responseType': 'json' };

  const controller = {

    fetchAll(filters = {}, pageIndex = 0, pageSize = 10) { // -> '/api/v1/users'
      filters['pageIndex'] = pageIndex;
      filters['pageSize'] = pageSize;
      return HttpClient.get(`${endpoint}/?${URIService.encodeData(filters)}`, headers, options);
    },

    fetchById(id) { // -> '/api/v1/users/:id'
      return HttpClient.get(`${endpoint}/${id}`, headers, options);
    },

    // *************************************************************************************************
    // CRUD
    //
    patch(id, data) { // -> '/api/v1/users/:id'
      return HttpClient.patch(`${endpoint}/${id}`, data, headers, options);
    },

    //
    //
    //
    fetchAllInvitedUsers(email = null, pageIndex = 0, pageSize = 10) {
      const url = email === null ? `${endpoint}/invited` : `${endpoint}/invited?email=${email}`;
      return HttpClient.get(url, data, headers, options);
    },
  };

  return controller;
})();
