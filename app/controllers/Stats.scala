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
		
		
		val data = new collection.mutable.Stack[Map[String, play.api.libs.json.JsValue]]
		val data2 = collection.mutable.Stack(
			//Map("title" -> Json.toJson("Books in bookshelf"), "value" -> Json.toJson("313")),
			//Map("title" -> Json.toJson("Books in bookshelf"), "value" -> Json.toJson("118")),
			//Map("title" -> Json.toJson("Unread books in bookshelf"), "value" -> Json.toJson("63")),
			//Map("title" -> Json.toJson("Pages read so far"), "value" -> Json.toJson("114375"))
		)
		
		//data.push(Map("title" -> Json.toJson("Pages read so far"), "value" -> Json.toJson("114375")))
		
		val service: StatsService = new StatsService(TableQuery[Book], TableQuery[Author], db)
		val stash = service.getPageCount2()
		
		val authorCount: Int = service.getAuthorCount()
		val bookCount: Int = service.getBookCount()
		val unreadBookCount: Int = service.getUnreadBookCount()
		val pageCount: Int = service.getPageCount()
		val readPageCount : Int = service.getReadPageCount
		val moneySpent: BigDecimal = service.getMoneySpentOnBooks()
		
		//data.push(Map("title" -> Json.toJson("Length"), "value" -> Json.toJson(stash.length)))
		
		stash foreach { case (row: BookRow) =>
        	//data.push(Map("title" -> Json.toJson(row.title), "value" -> Json.toJson(row.id)))
        }
		
		data.push(Map("authorCount" -> Json.toJson(authorCount)))
		data.push(Map("bookCount" -> Json.toJson(bookCount)))
		data.push(Map("unreadBookCount" -> Json.toJson(unreadBookCount)))
		data.push(Map("pageCount" -> Json.toJson(pageCount)))
		data.push(Map("readPageCount" -> Json.toJson(readPageCount)))
		data.push(Map("moneySpent" -> Json.toJson(moneySpent)))
		
		Ok(Json.toJson(data))
		
	}
}