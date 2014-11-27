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

class BookService(val books: TableQuery[BookTable], db: Database) {
	def createBook(bookData: Book) = {
	    
	    db.withSession { implicit session =>            
	            	        
	        books += createBookRow(bookData)
        }
	}
		
	def createBookRow(bookData:Book):BookRow = {
        val now 				= new Timestamp(new Date().getTime())
        val startedReadingTs 	= toTimestamp(bookData.startedReading)
        val finishedReadingTs 	= toTimestamp(bookData.finishedReading)
            
        BookRow(
        	0,
        	bookData.title,
        	bookData.languageId,
        	bookData.pageCount,
        	bookData.isRead.getOrElse(false),
        	Some(bookData.isbn),
        	now,
        	now,
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