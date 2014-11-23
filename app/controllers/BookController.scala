package controllers

import scala.slick.driver.MySQLDriver.simple._
import play.api._
import play.api.mvc._
import models.Tables._
import models.Tables.{Book => BookTable}
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
import services.BookService

object BookController extends BaseController {
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
			    
			    getBookService().createBook(bookData)
			    
			  //  data += Map("values" -> Json.toJson(bookData.title))
			    
				Ok(Json.toJson(data))					 
			}
		)
	}
	
	def createBookForm(): Form[Book] = {
		val catForm = Form(
			mapping(
		        "title" 		-> text.verifying("Title is required", {!_.isEmpty}),
		        "price"  		-> bigDecimal,
		        "languageId" 	-> text.verifying("Language is required", {!_.isEmpty}),
		        "pageCount"		-> number.verifying(min(1)),
		        "isRead"		-> optional(boolean),
		        "isbn"			-> text
		    )(Book.apply)(Book.unapply)
		)
		
		return catForm
	}
	
	def getBookService(): BookService = {
		val db = getDatabase()

		new BookService(TableQuery[BookTable], db)
	}
}