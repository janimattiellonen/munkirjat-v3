package services

import scala.slick.driver.MySQLDriver.simple._
import models.Tables.{Author => AuthorTable}
import models.Tables.BookAuthor
import models.Author

class AuthorService(val authors:TableQuery[AuthorTable], val bookAuthors:TableQuery[BookAuthor], db: Database) {
	def searchAuthors(term:String):Seq[(Int, String, String)] = {
	    db.withSession { implicit session =>            
	        
	        val query = for {
			  author <- authors if author.firstname.like(term + "%") || author.lastname.like(term + "%")
			} yield (author.id, author.firstname, author.lastname)
			
			query.run
        }
	}
	
	def getAuthor(authorId:Int):Option[models.Tables.AuthorRow] = {
	    db.withSession { implicit session => 
	        authors.filter{ _.id === authorId }.firstOption
	    }
	}
}