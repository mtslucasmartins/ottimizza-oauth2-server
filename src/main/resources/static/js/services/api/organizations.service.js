import { URIService } from '../uri.service.js';
import { HttpClient } from '../http.service.js';

export var OrganizationService = (function () {
  var endpoint = '/api/v1/organizations';

  var headers = { 'Content-Type': 'application/json' };
  var options = { 'responseType': 'json' };

  const controller = {

    // -> '/api/v1/organizations'
    fetchAll(filters = {}, pageIndex = 0, pageSize = 10) { 
      filters['pageIndex'] = pageIndex;
      filters['pageSize'] = pageSize;
      return HttpClient.get(`${endpoint}/?${URIService.encodeData(filters)}`, headers, options);
    },

    // -> '/api/v1/organizations/:id'
    fetchById(id) {
      return HttpClient.get(`${endpoint}/${id}`, headers, options);
    },

    // *************************************************************************************************
    // CRUD
    //
    // -> '/api/v1/organizations'
    create(data) {
      return HttpClient.post(`${endpoint}`, data, headers, options);
    },
    // -> '/api/v1/organizations/:id'
    patch(id, data) { // -> '/api/v1/organizations/:id'
      return HttpClient.patch(`${endpoint}/${id}`, data, headers, options);
    },

  };

  return controller;
})();
