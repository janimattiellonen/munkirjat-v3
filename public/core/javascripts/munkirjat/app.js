'use strict';

var app = angular.module('munkirjat', [
   'ngRoute',                                 
   'ngResource',
   'ui.router',
   'pascalprecht.translate',
   'ngSanitize',
   'ui.select'
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
		latestRead:			'Latest read book',
		latestAdded:		'Latest added books',
		favouriteAuthors:	'Favourite authors',
		recentlyRead:		'Recently read books',
		unread:				'Unread books',
		formAuthorFirstname:	'Firstname',
		formAuthorLastname:		'Lastname',
		formAuthorSave:		'Save',
		formBookTitle:		'Title',
		formBookPageCount:	'Page count',
		formBookAuthors:	'Authors',
		formBookPrice:		'Price',
		formBookIsRead:		'Is read',
		formBookIsbn:		'Isbn',
		formStartedReading:	'Started reading',
		formFinishedReading:'Finished reading',
		formStartReading:	'Start reading',
		formFinishReading:	'Finish reading',
		formBookLanguage:	'Language',
		formBookFinnish:	'Finnish',	
		formBookSwedish:	'Swedish',	
		formBookEnglish:	'English',	
		formBookSave:		'Save',
		formLoginUsername:	'Username',
		formLoginPassword:	'Password',
		formLoginLogin:		'Login'
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
            }).state('login', {
        		url: '/login',
                templateUrl: '/templates/login'
            }).state('new-author', {
        		url: '/new-author',
                templateUrl: '/templates/author-form',
                controller: 'AuthorController'
            }).state('edit-author', {
        		url: '/author/:authorId',
                templateUrl: '/templates/author-form',
                controller: 'AuthorController'
            }).state('new-book', {
        		url: '/new-book',
                templateUrl: '/templates/book-form',
                controller: 'BookController'
            }).state('edit-book', {
        		url: '/book/:bookId',
                templateUrl: '/templates/book-form',
                controller: 'BookController'
            });
}]);

app.factory('Stats', ['$resource', function($resource) {
    return $resource('/stats', 
    	{}, 
    	{
    		query: { method: 'GET', isArray: true },
    		currentlyReading: { method: 'GET', url: '/stats/currently-reading', isArray: true},
    		latestRead: { method: 'GET', url: '/stats/latest-read', isArray: true},
    		latestAdded: { method: 'GET', url: '/stats/latest-added', isArray: true },
    		favouriteAuthors: { method: 'GET', url: '/stats/favourite-authors', isArray: true },
    		recentlyRead: { method: 'GET', url: '/stats/recently-read', isArray: true},
    		unread: { method: 'GET', url: '/stats/unread', isArray: true}
    	}
    );
}]);

app.factory('Authors', ['$resource', function($resource) {
	return $resource('/authors/:authorId', 
			{authorId: '@id'},
			{
				save: { method: 'POST', isArray: true},
				update: { method: 'PUT', params: {authorId: '@id'}, isArray: true},
			}
		);
}]);

app.factory('Books', ['$resource', function($resource) {
	return $resource('/books/:bookId', 
			{bookId: '@id'},
			{
				save: { method: 'POST', isArray: true},
				update: { method: 'PUT', params: {bookId: '@id'}, isArray: true},
			}
		);
}]);
