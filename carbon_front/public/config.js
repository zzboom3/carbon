(function () {
  var defaultApiBaseURL = 'http://localhost:9091/'
  try {
    var key = 'apiBaseURL'
    var current = window.localStorage.getItem(key)
    if (!current) {
      window.localStorage.setItem(key, defaultApiBaseURL)
      current = defaultApiBaseURL
    }
    window.__CARBON_CONFIG__ = {
      apiBaseURL: current
    }
  } catch (e) {
    window.__CARBON_CONFIG__ = {
      apiBaseURL: defaultApiBaseURL
    }
  }
})()
