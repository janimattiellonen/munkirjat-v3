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

class StatsService(val books: TableQuery[Book], val authors: TableQuery[Author], val persons: TableQuery[Person], val tasks: TableQuery[Task], val jobs:TableQuery[Job], val bookAuthors: TableQuery[BookAuthor], db: Database) {
	    
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
    
    def getMoneySpentOnBooks(): (Int, BigDecimal) = {
        
        val query = sql"""
            SELECT
            COUNT(*) AS amount,
            SUM(price) AS book_sum
        FROM
            book
        WHERE
            price > 0""".as[(Int, BigDecimal)]
            
        db.withSession { implicit session =>            
            query.first
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
        
        db.withSession { implicit session => 
        	query.first
        }
    }
    
    
    def getAverageReadTime(): BigDecimal = {
        
        val query = sql"""
			SELECT
	          AVG(DATEDIFF(finished_Reading, started_reading)) AS pace
	        FROM
	          book
	        WHERE
	          is_read = 1
	          AND started_reading IS NOT NULL
	          AND finished_reading IS NOT NULL
        """.as[(BigDecimal)]
        
        db.withSession { implicit session => 
        	query.first
        }
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
        
	    db.withSession { implicit session => 
        	query.first
        }
    }
        
    def getEstimatedTimeToReadAllUnreadBooks(): BigDecimal = {
        round(getAverageReadTime() * getUnreadBookCount() / 365, 2)
    }
    
    def getStatistics(): Map[String, Any] = {
        Map(
        	"authorCount" 		-> getAuthorCount(),
        	"bookCount" 		-> getBookCount(),
        	"unreadBookCount" 	-> getUnreadBookCount(),
        	"pageCount"			-> getPageCount(),
        	"readPageCount"		-> getReadPageCount(),
        	"moneySpent"		-> getMoneySpentOnBooks(),
        	"avgBookPrice"		-> getAverageBookPrice(),
        	"slowestReadTime"	-> getSlowestReadTime(),
        	"fastestReadTime"	-> getFastestReadTime(),
        	"avgReadTime"		-> getAverageReadTime(),
        	"timeToReadAll"		-> getEstimatedTimeToReadAllUnreadBooks()
        )
    }
    
    def getCurrentlyReadBooks(limit:Int = 3): Seq[(Int, String, Option[java.sql.Timestamp], Option[java.sql.Timestamp], Boolean)] = {
        db.withSession { implicit session =>            
            books
            	.filter(b => b.isRead === false && b.startedReading.isNotNull && b.finishedReading.isNull)
            	.sortBy(b => b.startedReading.desc)
            	.take(limit)
            	.map(b => (b.id, b.title, b.startedReading, b.finishedReading, b.isRead))
            	.run
        }
    }
    
    def getLatestReadBook(): (Int, String, Option[java.sql.Timestamp], Option[java.sql.Timestamp], Boolean) = {
        db.withSession { implicit session =>            
            books
            	.filter(b => b.isRead === true && b.finishedReading.isNotNull)
            	.sortBy(b => b.finishedReading.desc)
            	.take(1)
            	.map(b => (b.id, b.title, b.startedReading, b.finishedReading, b.isRead))
            	.first
            	.run
            	
        }
    }
    
    def getLatestAddedBooks(limit:Int = 3): Seq[(Int, String, java.sql.Timestamp)] = {
        db.withSession { implicit session =>            
            books
            	.sortBy(_.createdAt.desc)
            	.map(b => (b.id, b.title, b.createdAt))
            	.take(5)
            	.run
        }
    }
    
    def getFavouriteAuthors(): List[(Int, String, String, Int)] = {
        
        val query = sql"""
            SELECT
				a.id,
				a.firstname,
				a.lastname,
				count(b.id) AS amount
        	FROM
        		book b LEFT JOIN book_author ba ON b.id = ba.book_id
        		LEFT JOIN author a ON a.id = ba.author_id
			GROUP BY a.id
			HAVING amount > 3
			ORDER BY amount DESC
        	LIMIT 10""".as[(Int, String, String, Int)]
            
        db.withSession { implicit session =>            
            query.list
        }
    }
    
    def round(value: ScalaNumber, scale: Int = 2): BigDecimal = {
    	BigDecimal(BigDecimal(value.toString()).toDouble).setScale(scale, BigDecimal.RoundingMode.HALF_UP)
    }
}