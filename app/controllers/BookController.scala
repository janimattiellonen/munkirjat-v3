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
import play.api.data.Forms._
import models.Book
import play.api.data._

object BookController extends Controller {
	def create = Action { implicit request =>
	    val data = new ListBuffer[Map[String, JsValue]]()
	    
		val bookForm = createBookForm()
		
		bookForm.bindFromRequest.fold(
			formWithErrors => {
			    
	            data += Map(
		        	"status" -> Json.toJson("false"))
			    
				BadRequest(Json.toJson(data))  
			},
			bookData => {
			  
							 
			}
		)
	    
	    Ok(Json.toJson(data))
	}
	
	def createBookForm(): Form[Book] = {
		val catForm = Form(
			mapping(
		        "name" -> nonEmptyText,
		        "price"  -> bigDecimal
		    )(Book.apply)(Book.unapply)
		)
		
		return catForm
	}
}