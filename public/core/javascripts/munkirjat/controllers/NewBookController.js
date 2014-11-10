app.controller('NewBookController', ['$rootScope', '$scope', '$stateParams', '$state',
    function NewBookController($rootScope, $scope, $stateParams, $state) {
        $scope.book = {
        	title: 				'',
        	language:			'',
        	pageCount:			'',
        	price:				'',
        	isRead:				'',
        	startedReading:		'',
        	finishedReading:	'',
        	isbn:				'',
        };
        
        $scope.saveBook = function() {
        	alert(JSON.stringify($scope.book));
        };
        
        $scope.setLanguage = function(language) {
        	$scope.book.language = language;
        };
        
        $scope.setLanguage = function(l) {
        	alert(l);
        	
        };
    }]);