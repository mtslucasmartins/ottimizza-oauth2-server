export var URIService = (function () {

  function removeTrailingSlash(url) {
    return url.replace(/^(\/*)/, '');
  }

  function encodeData(data) {
    return Object.keys(data).map(function (key) {
      return [key, data[key]].map(encodeURIComponent).join("=");
    }).join("&");
  }

  function getPathParams(url, pattern) {
    url = removeTrailingSlash(url);
    pattern = removeTrailingSlash(pattern);

    const urlParts = url.split('/');
    const patternParts = pattern.split('/');

    let urlParams = {};

    if (urlParts.length === patternParts.length) {
      for (let i = 0; i < patternParts.length; i++) {
        let urlPart = urlParts[i];
        let patternPart = patternParts[i];

        if (patternPart.indexOf(':') === 0) {
          let paramName = patternPart.replace(/^(\:)/, '');
          let paramValue = urlPart;
          urlParams[paramName] = paramValue;
        }
      }
    } else {
      console.log('URL does not match');
    }
    return urlParams;
  }

  function getLocationPathnameParams(pattern) {
    let url = removeTrailingSlash(window.location.pathname);
    pattern = removeTrailingSlash(pattern);

    const urlParts = url.split('/');
    const patternParts = pattern.split('/');

    let urlParams = {};

    if (urlParts.length === patternParts.length) {
      for (let i = 0; i < patternParts.length; i++) {
        let urlPart = urlParts[i];
        let patternPart = patternParts[i];

        if (patternPart.indexOf(':') === 0) {
          let paramName = patternPart.replace(/^(\:)/, '');
          let paramValue = urlPart;
          urlParams[paramName] = paramValue;
        }
      }
    } else {
      console.log('URL does not match');
    }
    return urlParams;
  }

  return {
    encodeData: encodeData,
    getPathParams: getPathParams,
    getLocationPathnameParams: getLocationPathnameParams
  };
})();