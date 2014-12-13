package services
import scala.slick.driver.MySQLDriver.simple._
import models.Tables.{Book => BookTable}
import models.Book
import scala.slick.jdbc.StaticQuery.interpolation
import scala.slick.jdbc.GetResult
import models.Tables._
import scala.math.ScalaNumber
import scala.slick.lifted.Query
import scala.slick.lifted.Column
import java.sql.Timestamp
import java.util.Date
import java.text.SimpleDateFormat
import scala.collection.mutable.ListBuffer

class BookService(val books: TableQuery[BookTable], db: Database, authorService: AuthorService) {
    
    def getBook(bookId: Int):(Option[BookRow], List[(Int, String, String)]) = {
        (db.withSession { implicit session =>    
        	books.filter{ _.id === bookId }.firstOption
        }, getAuthorsForBook(bookId))
    }
    
    def getAuthorsForBook(bookId: Int):List[(Int, String, String)] = {
        
        val data = new ListBuffer[(Int, String, String)]()
        
        db.withSession { implicit session =>            
            val authorList:List[BookAuthorRow] = TableQuery[BookAuthor].filter{ _.bookId === bookId }.list
            	
            for (bookAuthorRow <- authorList) {
                
                val author = authorService.getAuthor(bookAuthorRow.authorId).getOrElse(null)
                val tuple = (author.id, author.firstname, author.lastname);
                data += tuple
            }	
            
            data.toList
        }
    }
    
	def createBook(bookData: Book):Int = {
	    
	    db.withSession { implicit session =>                  
	        val bookId:Int = (books returning books.map(_.id)) += createBookRowForInsert(bookData)
	        
	        addAuthorsToBook(bookId, bookData.authors)
	        
	        bookId
        }
	}
	
	def updateBook(bookId: Int, bookData: Book) = {
	    db.withSession { implicit session => 
	        books.where(_.id === bookId).update(createBookRowForUpdate(bookId, bookData, getBook(bookId)._1.getOrElse(null)))
	        
	        deleteBooks(bookId)
	        
	        addAuthorsToBook(bookId, bookData.authors);
	    }
	}
		
	def addAuthorsToBook(bookId:Int, authorIds:List[Int]) = {
	    
	    val bookAuthors = TableQuery[BookAuthor]
	    
	    db.withSession { implicit session => 
		    for(authorId <- authorIds) {
		        bookAuthors += BookAuthorRow(bookId, authorId)
		    }
	    }
	}
	
	def deleteBooks(bookId:Int) = {
	    val bookAuthors = TableQuery[BookAuthor]
	    
	    db.withSession { implicit session =>
	    	bookAuthors.where(_.bookId === bookId).delete
	    }
	}
	
	def createBookRowForInsert(bookData:Book):BookRow = {
        val now = new Timestamp(new Date().getTime())
            
        createBookRow(0, now, now, bookData)
  	}
	
	def createBookRowForUpdate(bookId:Int, bookData:Book, oldBookData: BookRow):BookRow = {
        val createdAt 				= oldBookData.createdAt
        val updatedAt 				= oldBookData.updatedAt
        
        createBookRow(bookId, createdAt, updatedAt, bookData)
  	}
	
	def createBookRow(bookId:Int, createdAt:Timestamp, updatedAt:Timestamp, bookData:Book):BookRow = {
        val startedReadingTs 	= toTimestamp(bookData.startedReading)
        val finishedReadingTs 	= toTimestamp(bookData.finishedReading)
        
        BookRow(
        	bookId,
        	bookData.title,
        	bookData.languageId,
        	bookData.pageCount,
        	bookData.isRead.getOrElse(false),
        	Some(bookData.isbn),
        	createdAt,
        	updatedAt,
        	startedReadingTs,
        	finishedReadingTs,
        	Some(0.0),
        	Some(bookData.price)
        )
	}
	
	def toTimestamp(date:Option[String]):Option[Timestamp] = {
	    val value:String 	= date.getOrElse(null)
	    val format 			= new SimpleDateFormat("dd.MM.yyyy")
	    
	    if(value != null) Some(new Timestamp(format.parse(value).getTime())) else Some(null)
	}	
}