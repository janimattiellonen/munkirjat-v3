package controllers

import scala.slick.driver.MySQLDriver.simple._
import play.api._
import play.api.mvc._
import play.api.data.Forms._
import models.Tables._
import play.api.libs.json.Json
import play.api.libs.json.JsValue
import scala.math.ScalaNumber
import scala.collection.mutable.ListBuffer
import services.AuthorService
import models.Author
import play.api.data.Form
import play.api.libs.json.Writes
import play.api.data.FormError

object AuthorController extends BaseController {
	def create = Action { implicit request =>
	    val data	 = new ListBuffer[Map[String, JsValue]]()
		val authorForm = createAuthorForm()
		
        implicit val errorWrites = new Writes[FormError] {
        	def writes(formError: FormError) = Json.obj(
        		"key" -> formError.key,
        		"message" -> formError.message
        	)
		}
	    
		authorForm.bindFromRequest.fold(
			formWithErrors => {
	            data += Map("errors" -> Json.toJson(formWithErrors.errors))
		        	
		        BadRequest(Json.toJson(data))	
			},
			authorData => {
			    	 
			    data += Map(
			    	"id"  -> Json.toJson(getAuthorService().createAuthor(authorData))
			    )
			    
				Ok(Json.toJson(data))					 
			}
		)
	}
	
	def update(authorId: Int) = Action { implicit request =>
	    val data	 = new ListBuffer[Map[String, JsValue]]()
	    
		val authorForm = createAuthorForm()
		
        implicit val errorWrites = new Writes[FormError] {
        	def writes(formError: FormError) = Json.obj(
        		"key" -> formError.key,
        		"message" -> formError.message
        	)
		}
	    
		authorForm.bindFromRequest.fold(
			formWithErrors => {
	            data += Map("errors" -> Json.toJson(formWithErrors.errors))
		        	
		        BadRequest(Json.toJson(data))	
			},
			authorData => {
			    
			    getAuthorService().updateAuthor(authorId, authorData)
			   			    
				Ok(Json.toJson(data))					 
			}
		)
	}	
    	
	def get(authorId:Int) = Action {
	    val data						= new ListBuffer[Map[String, JsValue]]()
	    val result:Option[models.Tables.AuthorRow] = getAuthorService().getAuthor(authorId)
	    
	    val author:models.Tables.AuthorRow = result.getOrElse(null)
	    
	    if (null != author) {

	    	data += Map(
	    		"id" 			-> Json.toJson(author.id),
	    		"firstname"		-> Json.toJson(author.firstname),
	    		"lastname"		-> Json.toJson(author.lastname)
	    	)  
	    }
	    
		Ok(Json.toJson(data))
	}
	
	def createAuthorForm(): Form[Author] = {
		val authorForm = Form(
			mapping(
		        "firstname" -> text.verifying("Firstname is required", {!_.isEmpty}),
		        "lastname" 	-> text.verifying("Lastname is required", {!_.isEmpty})
		    )(Author.apply)(Author.unapply)
		)
		
		return authorForm
	}	
	
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
	
	def authorFormTemplate = Action {
	    Ok(views.html.authorform(""))
	}
}