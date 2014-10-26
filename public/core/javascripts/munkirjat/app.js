var app = angular.module('app', [
   'ngRoute',                                 
   'ngResource',
   'ui.router'                   
]);
	
app.config(['$stateProvider', '$urlRouterProvider',
    function($stateProvider, $urlRouterProvider) {
    	$stateProvider
	    	.state('home', {
	    		url: '',
	            templateUrl: '/templates/home'
	        }).state('about', {
        		url: '/about',
                templateUrl: '/templates/about'
            });
}]);


app.directive('statsSidebar', ['$compile', function($compile) {
    return {
        restrict: 'E',
        replace: true,
        template: '<div class="stats"><h2>Statistics</h2><div class="item" ng-repeat="i in items"><h3>{{ i.title }}</h3><p>{{ i.value }}</p></div></div>',
        controller: function($scope, $element, $attrs, $location, Stats) {
        	Stats.query({"foo": 1}, function(result) {
        		$scope.items = result;
        	});
        }
    };
}]);


app.factory('Stats', ['$resource', function($resource) {
    return $resource('/stats', {}, {'query': {method: 'GET', isArray: true}});
}]);