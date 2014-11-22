app.controller('NewBookController', ['$rootScope', '$scope', '$stateParams', '$state', 'Books',
    function NewBookController($rootScope, $scope, $stateParams, $state, Books) {
	var errorizer = new Munkirjat.Errorizer($('#book-creation'), 'form-group');
	
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
           	
        	Books.save($scope.book, function(result) {
        		alert("OK: " + JSON.stringify(result));
        	}, function(result) {
        		errorizer.errorize(result.data[0].errors);
        	});
        };
    }]);