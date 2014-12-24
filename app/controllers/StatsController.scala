package controllers

import scala.slick.driver.MySQLDriver.simple._
import play.api._
import play.api.mvc._
import services.StatsService
import models.Tables._
import play.api.libs.json.Json
import play.api.libs.json.JsValue
import scala.math.ScalaNumber
import scala.collection.mutable.ListBuffer

object StatsController extends BaseController {

	import play.api.Play.current

	def stats = Action {
		
		val data = new ListBuffer[Map[String, JsValue]]()
		val stats: Map[String, Any] = getStatsService().getStatistics()

		stats.foreach { 
		    case(key, value:(Int, BigDecimal)) 
		    	=> data += Map(
	    	        "title" -> Json.toJson(key.toString()), 
	    	        "value" -> Json.toJson(Map("title" ->value._1.toString(), 
	    	        "value" -> round(value._2, 2).toString())))
		    case(key, value:BigDecimal) => data += Map("title" -> Json.toJson(key), "value" -> Json.toJson(round(value, 2)))
		    case(key, value:Int) => data += Map("title" -> Json.toJson(key), "value" -> Json.toJson(value))
		}

		Ok(Json.toJson(data))
	}
	
	def round(value: ScalaNumber, scale: Int = 2): BigDecimal = {
    	BigDecimal(BigDecimal(value.toString()).toDouble).setScale(scale, BigDecimal.RoundingMode.HALF_UP)
    }
	
	def currentlyReading = Action {
	    
	    val data = new ListBuffer[Map[String, JsValue]]()
	    val results: Seq[(Int, String, Option[java.sql.Timestamp], Option[java.sql.Timestamp], Boolean)] = getStatsService().getCurrentlyReadBooks()
	    
	    for (result <- results) {
	        data += Map(
	        	"id" -> Json.toJson(result._1.toString()), 
	        	"title" -> Json.toJson(result._2), 
	        	"started_reading" -> Json.toJson(result._3), 
	        	"is_read" -> Json.toJson(result._5.toString()))
	    }
	    
	    Ok(Json.toJson(data))
	}
	
	def latestRead = Action {
	    val result: (Int, String, Option[java.sql.Timestamp], Option[java.sql.Timestamp], Boolean) = getStatsService().getLatestReadBook()
	    val data = new ListBuffer[Map[String, JsValue]]()
	    
        data += Map(
        	"id" -> Json.toJson(result._1.toString()), 
        	"title" -> Json.toJson(result._2), 
        	"started_reading" -> Json.toJson(result._3), 
        	"finished_reading" -> Json.toJson(result._4), 
        	"is_read" -> Json.toJson(result._5.toString()))
	    
	    Ok(Json.toJson(data))
	}
	
	def latestAdded = Action {
	    
	    val results: Seq[(Int, String, java.sql.Timestamp)] = getStatsService().getLatestAddedBooks(3)
	    val data = new ListBuffer[Map[String, JsValue]]()
	    
	    for (result <- results) {
	        data += Map(
	        	"id" -> Json.toJson(result._1.toString()), 
	        	"title" -> Json.toJson(result._2.toString()), 
	        	"created_at" -> Json.toJson(result._3.toString())
	        )
	    }
	    
	    Ok(Json.toJson(data))
	}
	
	def favouriteAuthors = Action {
	    val results:List[(Int, String, String, Int)] = getStatsService().getFavouriteAuthors()
	    val data = new ListBuffer[Map[String, JsValue]]()
	    
	    for (result <- results) {
	        data += Map(
	        	"id" -> Json.toJson(result._1.toString()), 
	        	"firstname" -> Json.toJson(result._2.toString()), 
	        	"lastname" -> Json.toJson(result._3.toString()), 
	        	"amount" -> Json.toJson(result._4.toString())
	        )
	    }
	   	
		Ok(Json.toJson(data))
	}
	
	def recentlyRead() = Action {
	    
		val results:Seq[(Int, String, Option[java.sql.Timestamp], Option[java.sql.Timestamp], Boolean)] = getStatsService().getRecentlyReadBooks(10)
	    val data = new ListBuffer[Map[String, JsValue]]()
	     
	    for(result <- results) {
	        data += Map(
	        	"id" -> Json.toJson(result._1.toString()), 
	        	"title" -> Json.toJson(result._2)
	        )     
	    }
	    
	    Ok(Json.toJson(data))
	}
	
	def getUnread() = Action {
	    val results:Seq[(Int, String)] = getStatsService().getUnreadBooks(10)
	    
	    val data = new ListBuffer[Map[String, JsValue]]()
	    
	    for(result <- results) {
	        data += Map(
	        	"id" -> Json.toJson(result._1.toString()), 
	        	"title" -> Json.toJson(result._2)     
	        )
	    }
	    
	    Ok(Json.toJson(data))
	}
	
	def getStatsService(): StatsService = {
		val db = getDatabase()

		new StatsService(TableQuery[Book], TableQuery[Author], TableQuery[BookAuthor], db)
	}
}