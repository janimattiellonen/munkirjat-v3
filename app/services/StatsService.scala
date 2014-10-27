package services

import scala.slick.driver.MySQLDriver.simple._

import models.Tables._

import collection.mutable.Stack
import play.api.libs.json.Json
class StatsService(val books: TableQuery[Book], db: Database) {
	def getPageCount(): Stack[Map[Int, String]] = {
	    val stack = new Stack[Map[Int, String]]
	    
	    
	    db.withSession { implicit session =>
	        books foreach { case (row: BookRow) =>
	        	stack.push(Map(1 -> "foo"))
	        }
	    }

	    stack
	}
}