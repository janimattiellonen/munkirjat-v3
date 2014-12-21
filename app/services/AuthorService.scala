package services

import scala.slick.driver.MySQLDriver.simple._
import models.Tables.{Author => AuthorTable}
import models.Tables.BookAuthor
import models.Author
import models.Tables.AuthorRow

class AuthorService(val authors:TableQuery[AuthorTable], val bookAuthors:TableQuery[BookAuthor], db: Database) {
    
	def createAuthor(authorData: Author):Int = {
	    
	    db.withSession { implicit session =>                  
	        val authorId:Int = (authors returning authors.map(_.id)) += createAuthorRowForInsert(authorData)

	        authorId
        }
	}
	
	def updateAuthor(authorId: Int, authorData: Author) = {
	    db.withSession { implicit session => 
	        authors.where(_.id === authorId).update(createAuthorRowForUpdate(authorId, authorData))
	    }
	}
	
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
	
	def createAuthorRowForInsert(authorData:Author):AuthorRow = {
        createAuthorRow(0, authorData)
  	}
	
	def createAuthorRowForUpdate(authorId:Int, authorData:Author):AuthorRow = {
        createAuthorRow(authorId, authorData)
  	}
		
	def createAuthorRow(authorId:Int, authorData:Author):AuthorRow = {
        AuthorRow(
        	authorId,
        	authorData.firstname,
        	authorData.lastname
        )
	}	
}