package controllers

import play.api._
import play.api.mvc._
import scala.slick.driver.MySQLDriver.simple._

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
}