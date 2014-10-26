package controllers

import play.api._
import play.api.mvc._

object Stats extends Controller {
	def stats = Action {
		import play.api.libs.json.Json
		
		val foo = Map("doo" -> Json.toJson("grill"))
		
		val objMap = Map("id" -> Json.toJson("id"), "tags" -> Json.toJson(foo))
		
		
		val data = collection.mutable.Stack(
			Map("title" -> Json.toJson("Books in bookshelf"), "value" -> Json.toJson("313")),
			Map("title" -> Json.toJson("Books in bookshelf"), "value" -> Json.toJson("118")),
			Map("title" -> Json.toJson("Unread books in bookshelf"), "value" -> Json.toJson("63")),
			Map("title" -> Json.toJson("Pages read so far"), "value" -> Json.toJson("114375"))
		)

		Ok(Json.toJson(data))
		
	}
}