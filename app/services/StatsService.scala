package services

import scala.slick.driver.MySQLDriver.simple._
import scala.slick.jdbc.StaticQuery.interpolation
import scala.slick.jdbc.GetResult
import models.Tables._
import collection.mutable.Stack
import play.api.libs.json.Json
import scala.math.ScalaNumber

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
    
    def test(): (Int, BigDecimal) = {
        
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
    
    /*
     *         $qb->select('b')
            ->from('MunKirjat\BookBundle\Entity\Book', 'b')
            ->where('b.isRead = 1')
            ->orderBy('b.finishedReading', 'DESC')
            ->setMaxResults(1);
     */
    
    def getCurrentlyReadBooks(limit:Int = 3): Seq[(Int, String, Option[java.sql.Timestamp], Option[java.sql.Timestamp], Boolean)] = {
        db.withSession { implicit session =>            
            books
            	.filter(b => b.isRead === false && b.startedReading.isNotNull && b.finishedReading.isNull)
            	.take(limit)
            	.sortBy(b => b.startedReading.asc)
            	.map(b => (b.id, b.title, b.startedReading, b.finishedReading, b.isRead))
            	.run
        }
    }
    
    def getLatestReadBook(): (Int, String, Option[java.sql.Timestamp], Option[java.sql.Timestamp], Boolean) = {
        db.withSession { implicit session =>            
            books
            	.filter(b => b.isRead === true && b.finishedReading.isNotNull)
            	.take(1)
            	.sortBy(b => b.finishedReading.desc)
            	.map(b => (b.id, b.title, b.startedReading, b.finishedReading, b.isRead))
            	.first
            	.run
            	
        }
    }
    
    def round(value: ScalaNumber, scale: Int = 2): BigDecimal = {
    	BigDecimal(BigDecimal(value.toString()).toDouble).setScale(scale, BigDecimal.RoundingMode.HALF_UP)
    }
}