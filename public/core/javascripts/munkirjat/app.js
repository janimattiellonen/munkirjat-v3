var app = angular.module('app', [
   'ngRoute',                                 
   'ngResource',
   'ui.router',
   'pascalprecht.translate'
]);
	
app.config(function ($translateProvider) {
	$translateProvider.translations('en', {
		bookCount: 			'Books in bookshelf',
		readPageCount: 		'Pages read so far',
		fastestReadTime: 	'Fastest read time',
		pageCount: 			'Pages in the bookshelf',
		moneySpent: 		'money spent on books',
		books:				"{{books}} books",
		avgBookPrice: 		'Average book price',
		avgReadTime: 		'Average read time',
		timeToReadAll: 		'Estimated time to read all unread books',
		slowestReadTime: 	'Slowest read time',
		authorCount: 		'Authors in bookshelf',
		unreadBookCount:	'Unread books'
	});
	  
	$translateProvider.preferredLanguage('en');
});

app.controller('Ctrl', function ($scope, $translate) {
	  $scope.changeLanguage = function (key) {
		  
	    $translate.use(key);
	  };
	});

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


app.directive('statsSidebar', ['$compile', '$translate', function($compile, $translate) {
    return {
        restrict: 'E',
        replace: true,
        template: '<div class="stats"><h2>Statistics</h2><div class="item" ng-repeat="i in items"><h3 translate="{{ i.title }}">{{ i.title }}</h3><p>{{ format(i.title, i.value) }}</p></div></div>',
        controller: function($scope, $element, $attrs, $location, Stats, $translate) {
        	Stats.query({"foo": 1}, function(result) {
        		$scope.items = result;
        	});
        	
        	$scope.format = function(title, value) {
        		if (title === "moneySpent") {
        			return value.value + " (" + value.title + " books" + ")";
        		}
        		
        		return value;
        	};
        }
    };
}]);


app.factory('Stats', ['$resource', function($resource) {
    return $resource('/stats', {}, {'query': {method: 'GET', isArray: true}});
}]);