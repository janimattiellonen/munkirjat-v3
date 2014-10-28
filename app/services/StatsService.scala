package services

import scala.slick.driver.MySQLDriver.simple._

import models.Tables._

import collection.mutable.Stack
import play.api.libs.json.Json
class StatsService(val books: TableQuery[Book], val authors: TableQuery[Author], db: Database) {
	    
    def getAuthorCount(): Int = {
        db.withSession { implicit session =>            
            authors.length.run
        }
    }
    
    def getBookCount(): Int = {
        db.withSession { implicit session =>            
            books.length.run
        }
    }
    
    def getUnreadBookCount(): Int = {
        db.withSession { implicit session =>            
            books.filter(_.isRead === false).length.run
        }
    }
    
    
    def getPageCount(): Int = {
        db.withSession { implicit session =>            
            books.map(_.pageCount).sum.run.getOrElse(0)
        }
    }
    
    def getMoneySpentOnBooks(): BigDecimal = {
        db.withSession { implicit session =>            
            books.map(_.price).sum.run.getOrElse(0.0).asInstanceOf[BigDecimal]
        }
    }
    
    def getReadPageCount(): Int = {
        db.withSession { implicit session =>            
            books.filter(_.isRead === true).map(_.pageCount).sum.run.getOrElse(0)
        }
    }
    
    def getPageCount2(): Stack[BookRow] = {
	    val stack = new Stack[BookRow]
	    
	    db.withSession { implicit session =>
	        books foreach { case (row: BookRow) =>
	            stack.push(row)
	        }
	    }

	    stack
	}
}