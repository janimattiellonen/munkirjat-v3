package controllers

import scala.slick.driver.MySQLDriver.simple._
import play.api._
import play.api.mvc._

import services.StatsService
import models.Tables._


object Stats extends Controller {
	def stats = Action {
		import play.api.libs.json.Json
		
	    val driver = Play.current.configuration.getString("db.default.driver").getOrElse("")
	    val url = Play.current.configuration.getString("db.default.url").getOrElse("")
	    val db = Database.forURL(url, driver = "com.mysql.jdbc.Driver")
		
	
		val foo = Map("doo" -> Json.toJson("grill"))
		
		val objMap = Map("id" -> Json.toJson("id"), "tags" -> Json.toJson(foo))
		
		
		val data = collection.mutable.Stack(
			Map("title" -> Json.toJson("Books in bookshelf"), "value" -> Json.toJson("313")),
			Map("title" -> Json.toJson("Books in bookshelf"), "value" -> Json.toJson("118")),
			Map("title" -> Json.toJson("Unread books in bookshelf"), "value" -> Json.toJson("63")),
			Map("title" -> Json.toJson("Pages read so far"), "value" -> Json.toJson("114375"))
		)
		
		data.push(Map("title" -> Json.toJson("Pages read so far"), "value" -> Json.toJson("114375")))
		
		val service: StatsService = new StatsService(TableQuery[Book], db)
		val stash = service.getPageCount()

		Ok(Json.toJson(data))
		
	}
}