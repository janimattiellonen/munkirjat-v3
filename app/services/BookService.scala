package services
import scala.slick.driver.MySQLDriver.simple._
import models.Tables.{Book => BookTable}
import models.Book
import scala.slick.jdbc.StaticQuery.interpolation
import scala.slick.jdbc.GetResult
import models.Tables._
import collection.mutable.Stack
import play.api.libs.json.Json
import scala.math.ScalaNumber
import scala.slick.lifted.Query
import scala.slick.lifted.Column


class BookService(val books: TableQuery[BookTable], db: Database) {
	def createBook(bookData: Book) = {
	    
	    db.withSession { implicit session =>            
	        
	        val d = new java.util.Date()
	        
	        books += BookRow(
	        	0,
	        	bookData.title,
	        	bookData.languageId,
	        	bookData.pageCount,
	        	bookData.isRead.getOrElse(false),
	        	Some(bookData.isbn),
	        	new java.sql.Timestamp(d.getTime),
	        	new java.sql.Timestamp(d.getTime),
	        	Some(null),
	        	Some(null),
	        	Some(0.0),
	        	Some(bookData.price)
	        )
        }
	}
}