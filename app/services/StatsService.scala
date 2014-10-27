package services

import scala.slick.driver.MySQLDriver.simple._

import models.Tables._

import collection.mutable.Stack
import play.api.libs.json.Json
class StatsService(val books: TableQuery[Book], db: Database) {
	def getPageCount(): Stack[BookRow] = {
	    val stack = new Stack[BookRow]
	    
	    
	    db.withSession { implicit session =>
	        
	        
	        
	        books foreach { case (row: BookRow) =>
	        	//stack.push(Map("id" -> row.id, "title" -> row.title))
	            stack.push(row)
	        }
	    }

	    stack
	}
}