import { HttpClient } from './http.service.js';

export var ImageCompressionService = (function () {

  const APPLICATION_ID = `development-bussola-contabil`;
  const ACCOUNTING_ID = `ottimizza`;

  const BASE_URL = `https://development-image-compression.herokuapp.com/api/v1/image_compressor`;

  const b64toBlob = (b64Data, contentType = '', sliceSize = 512) => {
    const byteCharacters = atob(b64Data);
    const byteArrays = [];

    for (let offset = 0; offset < byteCharacters.length; offset += sliceSize) {
      const slice = byteCharacters.slice(offset, offset + sliceSize);

      const byteNumbers = new Array(slice.length);
      for (let i = 0; i < slice.length; i++) {
        byteNumbers[i] = slice.charCodeAt(i);
      }

      const byteArray = new Uint8Array(byteNumbers);
      byteArrays.push(byteArray);
    }

    const blob = new Blob(byteArrays, { type: contentType });
    return blob;
  }

  let blobToFile = function (blob, name) {
    const b = blob;
    //A Blob() is almost a File() - it's just missing the two properties below which we will add
    b.lastModifiedDate = new Date();
    b.name = name;
    //Cast to a File() type
    return new File([blob], blob.name, { type: blob.type });;
  }

  let compress = function (file, size = 460, removeTransparency = true) {
    let formData = new FormData();
    formData.append('file', file);

    return HttpClient.post(`${BASE_URL}?size=${size}&remove_transparency=${removeTransparency}`, formData, {}, { responseType: 'blob' });
  };

  return {
    blobToFile: blobToFile,
    b64toBlob: b64toBlob,
    compress: compress
  };
})();