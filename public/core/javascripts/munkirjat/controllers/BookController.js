app.controller('BookController', ['$rootScope', '$scope', '$stateParams', '$state', 'Books',
    function BookController($rootScope, $scope, $stateParams, $state, Books) {
		var errorizer = new Munkirjat.Errorizer($('#book-creation'), 'form-group', [{key: 'authors[0]', alias: 'authors'}, {key: 'languageId', alias: 'language'}]);
		
        $scope.book = {
        	id:					undefined !== $stateParams.bookId ? $stateParams.bookId : null,
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

           	var method = null !== $scope.book.id ? Books.update : Books.save;
           	
           	method($scope.book, function(result) {
           		$scope.book.id = result[0].id;
        	}, function(result) {
        		errorizer.errorize(result.data[0].errors);
        	});
        };
        
        $scope.getSelectedAuthors = function() {
        	return $('#authors').val().split(',');
        }
        
        $scope.setSelectedAuthors = function(authors) {
			var selectedAuthors = [];
			var selectedAuthorIds = [];
			
			_.each(authors, function(item) {
				selectedAuthors.push(item);
				selectedAuthorIds.push(item.id);
			});
			
			$('#authors').select2("data", selectedAuthors);
			$('#authors').select2("val", selectedAuthorIds);
        };
        
        $scope.loadBookDetails = function(bookId) {
        	Books.query({bookId: bookId}, function(data) {
        		var book = data[0];
        		        		
        		if (book.startedReading != null) {
        			book.startedReading = moment(book.startedReading).format("D.M.YYYY")
        		}
        		
        		if (book.finishedReading != null) {
        			book.finishedReading = moment(book.finishedReading).format("D.M.YYYY")
        		}
        		
        		if (book.authors.length > 0) {
        			$scope.setSelectedAuthors(book.authors);
        		}
        		
        		$scope.book = book;
        	}, function(data) {
        		alert("NOT OK: " + JSON.stringify(data));
        	});
        }
        
        if(undefined !== $stateParams.bookId) {
        	$scope.loadBookDetails($stateParams.bookId);        
        }
    }]);