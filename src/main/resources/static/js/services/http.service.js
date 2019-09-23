export var HttpClient = (function () {

  var State = (function () {
    return {
      UNSENT: 0,           // Client has been created. open() not called yet.
      OPENED: 1,           // open() has been called.
      HEADERS_RECEIVED: 2, // send() has been called, and headers and status are available.
      LOADING: 3,          // Downloading; responseText holds partial data.
      DONE: 4              // The operation is complete.
    }
  })();

  var HttpMethod = (function () {
    return {
      GET: 'GET',
      POST: 'POST',
      PUT: 'PUT',
      PATCH: 'PATCH',
      DELETE: 'DELETE',
      OPTIONS: 'OPTIONS'
    }
  })();

  function Http(url, data, headers, options = {}) {
    this.url = url;
    this.data = data;
    this.headers = headers;
    this.options = options;

    this.method = options.method || HttpMethod.GET;
    this.isAsync = options.isAsync || true;

    this.HttpRequest = new XMLHttpRequest();

    this.setHeaders = function (headers) {
      for (let name in headers) {
        this.HttpRequest.setRequestHeader(name, headers[name]);
      }
    }

    this.subscribe = function () {
      const that = this;
      return new Promise((resolve, reject) => {
        if (that.options.responseType) {
          that.HttpRequest.responseType = that.options.responseType;
        }
        that.HttpRequest.open(that.method, that.url, that.isAsync);
        this.setHeaders(this.headers);
        that.HttpRequest.onreadystatechange = () => {
          switch (that.HttpRequest.readyState) {
            case State.DONE:
              if (that.HttpRequest.status == 200) {
                if (that.HttpRequest.responseType === 'json') {
                  resolve(that.HttpRequest.response);
                } else {
                  resolve(that.HttpRequest.response);
                }
              } else {
                if (that.HttpRequest.responseType === 'json') {
                  resolve(that.HttpRequest.response);
                } else {
                  resolve(that.HttpRequest.response);
                }
              }
              break;
          }
        };
        that.HttpRequest.send(that.data);
      });
    }
  }

  return {
    get: function (url, headers = {}, options = {}) {
      options['method'] = HttpMethod.GET;
      return new Http(url, null, headers, options);
    },
    post: function (url, data, headers = {}, options = {}) {
      options['method'] = HttpMethod.POST;
      if ((headers['Content-Type'] || '').indexOf('application/json') > 0) {
        data = typeof data === 'object' ? JSON.stringify(data) : data;
      }
      return new Http(url, data, headers, options);
    },
    patch: function (url, data, headers = {}, options = {}) {
      options['method'] = HttpMethod.PATCH;
      if ((headers['Content-Type'] || '').indexOf('application/json') >= 0) {
        data = typeof data === 'object' ? JSON.stringify(data) : data;
      }
      return new Http(url, data, headers, options);
    }
  }
})();
