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
		unreadBookCount:	'Unread books',
		currentlyReading:	'Currently reading'
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

app.directive('currentlyReading', ['$compile', function($compile) {
	return {
		restrict: 'E',
		replace: 'true',
		template: '<div class="box h125"><h2 translate="currentlyReading"></h2><div ng-repeat="item in readBooks"><a href="">{{ item.title }} - {{formatDate(item.started_reading) }}</a></div></div>',
		controller: function($scope, $element, $attrs, $location, Stats) {
			Stats.currentlyRead({}, function(result) {
				$scope.readBooks = result;
			});
			
			$scope.formatDate = function(date) {
				return moment(date).format("D.M.YYYY")
			};
		}
	}
}]);

app.directive('statsSidebar', ['$compile', function($compile) {
    return {
        restrict: 'E',
        replace: true,
        template: '<div class="stats"><h2>Statistics</h2><div class="item" ng-repeat="item in items"><h3 translate="{{ item.title }}">{{ item.title }}</h3><p>{{ format(item.title, item.value) }}</p></div></div>',
        controller: function($scope, $element, $attrs, $location, Stats) {
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
    return $resource('/stats', 
    	{}, 
    	{
    		query: { method: 'GET', isArray: true },
    		currentlyRead: { method: 'GET', url: '/stats/currently-reading', isArray: true}
    	}
    );
}]);