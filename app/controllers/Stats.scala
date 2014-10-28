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

		val data = new collection.mutable.Stack[Map[String, play.api.libs.json.JsValue]]
		
		val service: StatsService = new StatsService(TableQuery[Book], TableQuery[Author], db)
		
		val authorCount: Int = service.getAuthorCount()
		val bookCount: Int = service.getBookCount()
		val unreadBookCount: Int = service.getUnreadBookCount()
		val pageCount: Int = service.getPageCount()
		val readPageCount : Int = service.getReadPageCount
		val moneySpent: BigDecimal = service.getMoneySpentOnBooks()
		
		val stats: Map[String, Any] = service.getStatistics()

		stats.foreach { case(key, value) =>
			data.push(Map("title" -> Json.toJson(key), "value" -> Json.toJson(value.toString())))
		}
		
		Ok(Json.toJson(data))
	}
}