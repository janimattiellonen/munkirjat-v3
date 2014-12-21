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
import play.api.data.validation.Constraint

object BookController extends BaseController {
	def create = Action { implicit request =>
	    val data	 = new ListBuffer[Map[String, JsValue]]()
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
			    
			    data += Map(
			    	"id" -> Json.toJson(getBookService().createBook(bookData))
			    )
	    
				Ok(Json.toJson(data))					 
			}
		)
	}
	
	def update(bookId:Int) = Action { implicit request =>
	    val data	 = new ListBuffer[Map[String, JsValue]]()
	    
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
			    
			    getBookService().updateBook(bookId, bookData)
			   			    
				Ok(Json.toJson(data))					 
			}
		)
	}
	
	def get(bookId:Int) = Action {
	    val data						= new ListBuffer[Map[String, JsValue]]()
	    val result:(Option[models.Tables.BookRow], List[(Int, String, String)])= getBookService().getBook(bookId)
	    
	    val book:models.Tables.BookRow = result._1.getOrElse(null)
	    
	    if (null != book) {
	        
	        val authors	= new ListBuffer[Map[String, JsValue]]()
	        
	        for (author <- result._2) {
	            authors += Map(
	            	"id" -> Json.toJson(author._1),
	            	"firstname" -> Json.toJson(author._2),
	            	"lastname" -> Json.toJson(author._3)
	            )
	            
	        }
	        
	    	data += Map(
	    		"id" 				-> Json.toJson(book.id),
	    		"title"				-> Json.toJson(book.title),
	    		"languageId"		-> Json.toJson(book.languageId),
	    		"pageCount"			-> Json.toJson(book.pageCount),
	    		"price"				-> Json.toJson(book.price),
	    		"isRead"			-> Json.toJson(book.isRead),
	    		"startedReading"	-> Json.toJson(book.startedReading),
	    		"finishedReading"	-> Json.toJson(book.finishedReading),
	    		"isbn"				-> Json.toJson(book.isbn),
	    		"authors"			-> Json.toJson(authors)
	    	)  
	    }
	    
	    
		Ok(Json.toJson(data))
	}
	
	def createBookForm(): Form[Book] = {
		val bookForm = Form(
			mapping(
		        "title" 			-> text.verifying("Title is required", {!_.isEmpty}),
		        "authors" 			-> list(number).verifying("foo", e => e.size >= 1),
		        "price"  			-> bigDecimal.verifying("The price cannot be negative", {_ >= 0.0}),
		        "languageId" 		-> text.verifying("Language is required", {!_.isEmpty}),
		        "pageCount"			-> number.verifying("A book must have at least 1 page", {_ >= 1}),
		        "isRead"			-> optional(boolean),
		        "startedReading" 	-> optional(text),
		        "finishedReading" 	-> optional(text),
		        "isbn"				-> text
		    )(Book.apply)(Book.unapply)
		)
		
		return bookForm
	}
	
	def bookFormTemplate = Action {
	    Ok(views.html.bookform(""))
	}
}