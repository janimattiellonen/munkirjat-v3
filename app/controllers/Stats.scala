package controllers

import scala.slick.driver.MySQLDriver.simple._
import play.api._
import play.api.mvc._

import services.StatsService
import models.Tables._
import play.api.libs.json.Json
import play.api.libs.json.JsValue

object Stats extends Controller {
	def stats = Action {
		
		val data = new collection.mutable.Stack[Map[String, JsValue]]
		
		val stats: Map[String, Any] = getService().getStatistics()

		stats.foreach { 
		    case(key, value:(Int, BigDecimal)) 
		    	=> data.push(Map(
	    	        "title" -> Json.toJson(key.toString()), 
	    	        "value" -> Json.toJson(Map("title" ->value._1.toString(), 
	    	        "value" -> value._2.toString()))))
		    case(key, value:BigDecimal) => data.push(Map("title" -> Json.toJson(key), "value" -> Json.toJson(value)))
		    case(key, value:Int) => data.push(Map("title" -> Json.toJson(key), "value" -> Json.toJson(value)))
		}

		Ok(Json.toJson(data))
	}
	
	def currentlyReading = Action {
	    
	    val data = new collection.mutable.Stack[Map[String, JsValue]]
	    
	    val results: Seq[(Int, String, Option[java.sql.Timestamp], Option[java.sql.Timestamp], Boolean)] = getService().getCurrentlyReadBooks()
	    
	    for (result <- results) {
	        data.push(Map(
	        	"id" -> Json.toJson(result._1.toString()), 
	        	"title" -> Json.toJson(result._2), 
	        	"started_reading" -> Json.toJson(result._3), 
	        	"is_read" -> Json.toJson(result._5.toString())))
	    }
	    
	    Ok(Json.toJson(data))
	}
	
	def latestRead = Action {
	    val result: (Int, String, Option[java.sql.Timestamp], Option[java.sql.Timestamp], Boolean) = getService().getLatestReadBook()
	    
	    val data = new collection.mutable.Stack[Map[String, JsValue]]
	    
        data.push(Map(
        	"id" -> Json.toJson(result._1.toString()), 
        	"title" -> Json.toJson(result._2), 
        	"started_reading" -> Json.toJson(result._3), 
        	"finished_reading" -> Json.toJson(result._4), 
        	"is_read" -> Json.toJson(result._5.toString())))
	    
	    Ok(Json.toJson(data))
	}
	
	def getService(): StatsService = {
		val driver = Play.current.configuration.getString("db.default.driver").getOrElse("")
	    val url = Play.current.configuration.getString("db.default.url").getOrElse("")
	    val db = Database.forURL(url, driver = "com.mysql.jdbc.Driver")

		new StatsService(TableQuery[Book], TableQuery[Author], db)
	}
}