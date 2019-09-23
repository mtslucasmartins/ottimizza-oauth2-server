import { HttpClient } from './http.service.js';

export var StorageService = (function () {

  const APPLICATION_ID = `development-bussola-contabil`;
  const ACCOUNTING_ID = `ottimizza`;

  const BASE_URL = `https://s4.ottimizzacontabil.com:55325/storage`;

  let blobToFile = function (blob, name) {
    const b = blob;
    //A Blob() is almost a File() - it's just missing the two properties below which we will add
    b.lastModifiedDate = new Date();
    b.name = name;
    //Cast to a File() type
    return new File([blob], blob.name, { type: blob.type });;
  }
  let getResourceURL = function (resourceId) {
    return `${BASE_URL}/${resourceId}`;
  }

  let store = function (file) {
    let formData = new FormData();
    formData.append('file', file);

    return HttpClient.post(`${BASE_URL}/${APPLICATION_ID}/accounting/${ACCOUNTING_ID}/store`, formData, { 'Authorization': 'xxx' }, { responseType: 'json' });
  };

  return {
    blobToFile: blobToFile,
    store: store,
    getResourceURL: getResourceURL
  };
})();