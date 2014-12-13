app.controller('NewBookController', ['$rootScope', '$scope', '$stateParams', '$state', 'Books',
    function NewBookController($rootScope, $scope, $stateParams, $state, Books) {
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
        };
        
        $scope.saveBook = function() {
        	
        	$scope.book.authors = $scope.getSelectedAuthors();
           	alert(JSON.stringify($scope.book));

        	Books.save($scope.book, function(result) {
        		alert("OK: " + JSON.stringify(result));
        	}, function(result) {
        		errorizer.errorize(result.data[0].errors);
        	});
        };
        
        $scope.getSelectedAuthors = function() {
        	return $('#authors').val().split(',');
        }
    }]);