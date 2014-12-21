app.controller('AuthorController', ['$rootScope', '$scope', '$stateParams', '$state', 'Authors',
    function AuthorController($rootScope, $scope, $stateParams, $state, Authors) {
		var errorizer = new Munkirjat.Errorizer($('#author-creation'), 'form-group', []);
		
        $scope.author = {
        	id:			undefined !== $stateParams.authorId ? $stateParams.authorId : null,
        	firstname: 	"",
        	lastname: 	"",

        };
        
        $scope.saveAuthor = function() {
        	
        	var method = null !== $scope.author.id ? Authors.update : Authors.save;
        	
        	method($scope.author, function(result) {
        		$scope.author.id = result[0].id;     	
        	}, function(result) {
        		errorizer.errorize(result.data[0].errors);
        	});
        };
        
        $scope.loadAuthorDetails = function(authorId) {
        	Authors.query({authorId: authorId}, function(data) {
        		$scope.author = data[0];
        	}, function(data) {
        		alert("NOT OK: " + JSON.stringify(data));
        	});
        }
        
        if(undefined !== $stateParams.authorId) {
        	$scope.loadAuthorDetails($stateParams.authorId);
        }
    }]);