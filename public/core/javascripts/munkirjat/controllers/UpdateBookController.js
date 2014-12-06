app.controller('UpdateBookController', ['$rootScope', '$scope', '$stateParams', '$state', 'Books',
    function UpdateBookController($rootScope, $scope, $stateParams, $state, Books) {
		var errorizer = new Munkirjat.Errorizer($('#book-creation'), 'form-group', [{key: 'authors[0]', alias: 'authors'}, {key: 'languageId', alias: 'language'}]);
		
        $scope.book = {
        	title: 				'',
        	languageId:			'',
        	pageCount:			'',
        	price:				'',
        	isRead:				'',
        	startedReading:		'',
        	finishedReading:	'',
        	isbn:				'',
        	authors:			[],
        	id: 				$stateParams.bookId
        };
        
        $scope.saveBook = function() {
        	
        	$scope.book.authors = $scope.getAuthors();
           	alert(JSON.stringify($scope.book));

        	Books.update($scope.book, function(result) {
        		alert("OK: " + JSON.stringify(result));
        	}, function(result) {
        		errorizer.errorize(result.data[0].errors);
        	});
        };
        
        $scope.getAuthors = function() {
        	return $('#authors').val().split(',');
        }
    }]);