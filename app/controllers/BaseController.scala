package controllers

import play.api._
import play.api.mvc._
import scala.slick.driver.MySQLDriver.simple._
import services.AuthorService
import models.Tables._
import services.BookService
import models.Tables.{Book => BookTable}

trait BaseController extends Controller {
	def getDatabaseDriver():String = {
	    Play.current.configuration.getString("db.default.driver").getOrElse("")
	}
	
	def getDatabaseUrl():String = {
	    Play.current.configuration.getString("db.default.url").getOrElse("")
	}
	
	def getDatabase(): slick.driver.MySQLDriver.backend.DatabaseDef = {
	    Database.forURL(getDatabaseUrl(), driver = "com.mysql.jdbc.Driver")
	}
	
	def getAuthorService(): AuthorService = {
		val db = getDatabase()

		new AuthorService(TableQuery[Author], TableQuery[BookAuthor], db)
	}
	
	def getBookService(): BookService = {
		val db = getDatabase()

		new BookService(TableQuery[BookTable], db, getAuthorService())
	}
}