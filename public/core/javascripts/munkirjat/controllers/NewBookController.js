app.controller('NewBookController', ['$rootScope', '$scope', '$stateParams', '$state', 'Books',
    function NewBookController($rootScope, $scope, $stateParams, $state, Books) {
		var errorizer = new Munkirjat.Errorizer($('#book-creation'), 'form-group');
		
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
           	alert(JSON.stringify($scope.book));
           	return;
        	Books.save($scope.book, function(result) {
        		alert("OK: " + JSON.stringify(result));
        	}, function(result) {
        		errorizer.errorize(result.data[0].errors);
        	});
        };
        
        $scope.next = function() {
        	console.log("cc1: " + $('#authors').val());
        }
    }]);