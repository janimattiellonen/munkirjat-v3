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

app.directive('latestAdded', ['$compile', function($compile) {
	return {
		restrict: 'E',
		replace: true,
		template: '<div class="box h_tallest"><h2 translate="latestAdded"></h2><ul><li ng-repeat="item in latestAdded"><a href="{{ item.id }}">{{ item.title }}<br/>{{formatDate(item.createdAt) }}</a></li></ul></div>',
		controller: function($scope, $element, $attrs, $location, Stats) {
			Stats.latestAdded({}, function(result) {
				$scope.latestAdded = result;
			});
		}
	}
}]);

app.directive('favouriteAuthors', ['$compile', function($compile) {
	return {
		restrict: 'E',
		replace: true,
		template: '<div class="box h_tallest"><h2 translate="favouriteAuthors"></h2><ul><li ng-repeat="item in favouriteAuthors"><a href="{{ item.id }}">{{ item.firstname }} {{ item.lastname}} ({{ item.amount }})</a></li></ul></div>',
		controller: function($scope, $element, $attrs, $location, Stats) {
			Stats.favouriteAuthors({}, function(result) {
				$scope.favouriteAuthors = result;
			});
		}
	}
}]);

app.directive('recentlyRead', ['$compile', function($compile) {
	return {
		restrict: 'E',
		replace: true,
		template: '<div class="box h_tallest"><h2 translate="recentlyRead"></h2><ul><li ng-repeat="item in recentlyReadBooks"><a href="{{ item.id }}">{{ item.title }}</a></li></ul></div>',
		controller: function($scope, $element, $attrs, $location, Stats) {
			Stats.recentlyRead({}, function(result) {
				$scope.recentlyReadBooks = result;
			});
		}
	}
}]);

app.directive('unread', ['$compile', function($compile) {
	return {
		restrict: 'E',
		replace: true,
		template: '<div class="box h_tallest"><h2 translate="unread"></h2><ul><li ng-repeat="item in unreadBooks"><a href="{{ item.id }}">{{ item.title }}</a></li></ul></div>',
		controller: function($scope, $element, $attrs, $location, Stats) {
			Stats.unread({}, function(result) {
				$scope.unreadBooks = result;
			});
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

app.directive('datepicker', function() {
    return {
        restrict: 'AE',
        require : 'ngModel',
        link : function (scope, element, attrs, ngModelCtrl) {
            $(function(){
                element.datepicker({
                    dateFormat: "dd.mm.yy",
                    onSelect:function (date) {
                        scope.$apply(function () {
                            ngModelCtrl.$setViewValue(date);
                        });
                    }
                });
            });
        }
    }
});

app.directive('authorSelector', ['$compile', function($compile) {
	return {
		restrict: 'A',
		link: function(scope, element, attrs) { 
			$(document).ready(function() {
				$('#authors').select2({
					width: "300px",
	                minimumInputLength: 2,
	                multiple: true,
	                allowClear: true,
	                ajax: {
	                    url: "/authors/search",
	                    dataType: 'json',
	                    data: function(term, page) {
	                        return {
	                            q: term
	                        }
	                    },
	                    results: function(data, page) {
	                        return {results: data};
	                    }
	                },

	                formatSelection: function(author) {
	                    return author.firstname + " " + author.lastname;
	                },

	                formatResult: function(author) {
	                	return author.firstname + " " + author.lastname;
	                }
				});
			});
		}
	}
}])
