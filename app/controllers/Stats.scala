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


object Stats extends Controller {
	def stats = Action {
		
		val data = new ListBuffer[Map[String, JsValue]]()
		
		val stats: Map[String, Any] = getService().getStatistics()

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
	    
	    val results: Seq[(Int, String, Option[java.sql.Timestamp], Option[java.sql.Timestamp], Boolean)] = getService().getCurrentlyReadBooks()
	    
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
	    val result: (Int, String, Option[java.sql.Timestamp], Option[java.sql.Timestamp], Boolean) = getService().getLatestReadBook()
	    
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
	    
	    val results: Seq[(Int, String, java.sql.Timestamp)] = getService().getLatestAddedBooks(3)
	    
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
	
	def getService(): StatsService = {
		val driver = Play.current.configuration.getString("db.default.driver").getOrElse("")
	    val url = Play.current.configuration.getString("db.default.url").getOrElse("")
	    val db = Database.forURL(url, driver = "com.mysql.jdbc.Driver")

		new StatsService(TableQuery[Book], TableQuery[Author], db)
	}
}