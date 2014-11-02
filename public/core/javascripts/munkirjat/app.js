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
		currentlyReading:	'Currently reading',
		latestRead:			'Latest read book'
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
		template: '<div class="box h_taller"><h2 translate="currentlyReading"></h2><ul><li ng-repeat="item in currentlyReadBooks"><a href="">{{ item.title }}<br/>{{formatDate(item.started_reading) }} ({{ daysRead(item.started_reading) }} days)</a></li></ul></div>',
		controller: function($scope, $element, $attrs, $location, Stats) {
			Stats.currentlyReading({}, function(result) {
				$scope.currentlyReadBooks = result;
			});
			
			$scope.formatDate = function(date) {
				return moment(date).format("D.M.YYYY")
			};
			
			$scope.daysRead = function(startDate, endDate) {
				var startedReading = moment(startDate);
	            var now = endDate == null ? moment() : moment(endDate);
	            return now.diff(startedReading, 'days') + 1;
			}
		}
	}
}]);

app.directive('latestRead', ['$compile', function($compile) {
	return {
		restrict: 'E',
		replace: true,
		template: '<div class="box h_taller"><h2 translate="latestRead"></h2><ul><li ng-repeat="item in latestReadBook"><a href="">{{ item.title }}<br/>{{formatDate(item.started_reading) }} - {{formatDate(item.finished_reading) }} ({{ daysRead(item.started_reading, item.finished_reading) }} days)</a></li></ul></div>',
		controller: function($scope, $element, $attrs, $location, Stats) {
			Stats.latestRead({}, function(result) {
				$scope.latestReadBook = result;
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
    		currentlyReading: { method: 'GET', url: '/stats/currently-reading', isArray: true},
    		latestRead: { method: 'GET', url: '/stats/latest-read', isArray: true}
    	}
    );
}]);