package services

import scala.slick.driver.MySQLDriver.simple._
import scala.slick.jdbc.StaticQuery.interpolation
import scala.slick.jdbc.GetResult
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
    
    /**
     * Only includes books that have a price.
     */
    def getAverageBookPrice(): BigDecimal = {
        db.withSession { implicit session =>            
            books.filter(_.price > BigDecimal(0.0)).map(_.price).avg.run.getOrElse(0.0).asInstanceOf[BigDecimal]
        }
    }
    
    def getReadPageCount(): Int = {
        db.withSession { implicit session =>            
            books.filter(_.isRead === true).map(_.pageCount).sum.run.getOrElse(0)
        }
    }
    
    def getSlowestReadTime(): BigDecimal = {
        
        val query = sql"""
			SELECT
	          MAX(DATEDIFF(finished_Reading, started_reading)) AS pace
	        FROM
	          book
	        WHERE
	          is_read = 1
	          AND started_reading IS NOT NULL
	          AND finished_reading IS NOT NULL""".as[(BigDecimal)]
        
        val diff = db.withSession { implicit session => 
        	query.first
        }
        	
        diff
    }
    
    def getFastestReadTime(): BigDecimal = {
        
        val query = sql"""
			SELECT
	          MIN(DATEDIFF(finished_Reading, started_reading)) AS pace
	        FROM
	          book
	        WHERE
	          is_read = 1
	          AND started_reading IS NOT NULL
	          AND started_reading > '2000-01-01 00:00:00'
	          AND finished_reading IS NOT NULL
	          AND finished_reading > '2000-01-01 00:00:00'""".as[(BigDecimal)]
        
	    val diff = db.withSession { implicit session => 
        	query.first
        }
        	
        diff
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
    
    def getStatistics(): Map[String, Any] = {
        Map(
        	"authorCount" 		-> getAuthorCount().toString(),
        	"bookCount" 		-> getBookCount(),
        	"unreadBookCount" 	-> getUnreadBookCount(),
        	"pageCount"			-> getPageCount(),
        	"readPageCount"		-> getReadPageCount(),
        	"moneySpent"		-> getMoneySpentOnBooks(),
        	"avgBookPrice"		-> getAverageBookPrice(),
        	"slowestReadTime"	-> getSlowestReadTime(),
        	"fastestReadTime"	-> getFastestReadTime()
        )
    }
}