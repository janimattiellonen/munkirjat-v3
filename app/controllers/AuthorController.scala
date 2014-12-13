package controllers

import scala.slick.driver.MySQLDriver.simple._
import play.api._
import play.api.mvc._
import models.Tables._
import play.api.libs.json.Json
import play.api.libs.json.JsValue
import scala.math.ScalaNumber
import scala.collection.mutable.ListBuffer
import services.AuthorService

object AuthorController extends BaseController {
	def searchAuthors(q:Option[String]) = Action { implicit request =>
	    val data = new ListBuffer[Map[String, JsValue]]()
	    
	    val results:Seq[(Int, String, String)] = getAuthorService().searchAuthors(q.getOrElse(""))
	    
	    for (result <- results) {
	        data += Map(
	        	"id" -> Json.toJson(result._1),
	        	"firstname" -> Json.toJson(result._2),
	        	"lastname" -> Json.toJson(result._3)
	        )
	    }

	    Ok(Json.toJson(data))
	}
}