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
import play.api.data.FormError
import play.api.libs.json.Writes
import scala.collection.Seq
import validation.Constraints._


object BookController extends Controller {
	def create = Action { implicit request =>
	    val data = new ListBuffer[Map[String, JsValue]]()
	    
		val bookForm = createBookForm()
		
        implicit val errorWrites = new Writes[FormError] {
        	def writes(formError: FormError) = Json.obj(
        		"key" -> formError.key,
        		"message" -> formError.message
        	)
		}
	    
		bookForm.bindFromRequest.fold(
			formWithErrors => {
	            data += Map("errors" -> Json.toJson(formWithErrors.errors))
		        	
		        BadRequest(Json.toJson(data))	
			},
			bookData => {
				Ok(Json.toJson(data))					 
			}
		)
	}
	
	def createBookForm(): Form[Book] = {
		val catForm = Form(
			mapping(
		        "title" -> text.verifying("Title is required", {!_.isEmpty}),
		        "price"  -> bigDecimal
		    )(Book.apply)(Book.unapply)
		)
		
		return catForm
	}
}