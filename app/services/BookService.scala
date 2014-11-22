package services
import scala.slick.driver.MySQLDriver.simple._
import scala.slick.jdbc.StaticQuery.interpolation
import scala.slick.jdbc.GetResult
import models.Tables._
import collection.mutable.Stack
import play.api.libs.json.Json
import scala.math.ScalaNumber
import scala.slick.lifted.Query
import scala.slick.lifted.Column


class BookService(val books: TableQuery[Book], db: Database) {
	def createBook() = {
	    val books = TableQuery[Book]
	    
	    db.withSession { implicit session =>            

	        
	        val d = new java.util.Date()
	        
	        books += BookRow(
	        	0,
	        	"Test title",
	        	"en",
	        	666,
	        	true,
	        	Some("34593479534795"),
	        	new java.sql.Timestamp(d.getTime),
	        	new java.sql.Timestamp(d.getTime),
	        	Some(new java.sql.Timestamp(d.getTime)),
	        	Some(new java.sql.Timestamp(d.getTime)),
	        	Some(3.5),
	        	Some(123.45)
	        )
	        
	        
        }
	}
}