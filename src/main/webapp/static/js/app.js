var productApp = angular.module('productApp', ['ngCookies', 'ngRoute', 'angular-md5', 'cgBusy', 'ngTouch']);

//var API_URL_ROOT = 'http://www.autoask.com/autoask';
//productApp.constant('API_URL_ROOT', 'http://www.autoask.com/autoask');
var API_URL_ROOT = 'http://localhost:8080/autoask';
productApp.constant('API_URL_ROOT', 'http://localhost:8080/autoask');

String.prototype.csFormat = function() {
    if (arguments.length == 0) return this;
    for (var s = this, i = 0; i < arguments.length; i++)
        s = s.replace(new RegExp("\\{" + i + "\\}", "g"), arguments[i]);
    return s;
};

productApp.constant('BUTTON_BG_COLOR', '#ccc');