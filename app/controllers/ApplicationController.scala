package controllers

import play.api._
import play.api.mvc._
import scala.slick.driver.MySQLDriver.simple._
import models._

object ApplicationController extends Controller {

	def index = Action {
	    
	    val driver = Play.current.configuration.getString("db.default.driver").getOrElse("")
	    val url = Play.current.configuration.getString("db.default.url").getOrElse("")
	    val db = Database.forURL(url, driver = "com.mysql.jdbc.Driver")
	    
	    /*
	     // the code below creates the tables and inserts some example code
		val suppliers: TableQuery[Suppliers] = TableQuery[Suppliers]

		// the query interface for the Coffees table
        val coffees: TableQuery[Coffees] = TableQuery[Coffees]
		
		// find a way to get the settings from application.conf instead
		val db = Database.forURL("jdbc:mysql://localhost/slicktest?user=root&characterEncoding=UTF-8", driver = "com.mysql.jdbc.Driver")
		
		
		db.withSession { implicit session =>
			// commented out, this creates the both tables, can be run only once
			//(suppliers.ddl ++ coffees.ddl).create
		    suppliers += (101, "Acme, Inc.", "99 Market Street", "Groundsville", "CA", "95199")
		    suppliers += ( 49, "Superior Coffee", "1 Party Place", "Mendocino", "CA", "95460")
		    suppliers += (150, "The High Ground", "100 Coffee Lane", "Meadows", "CA", "93966")
		}
		*/

		Ok(views.html.index(""))
	}
	
	def homeTemplate = Action {
	   /*
	    scala.slick.codegen.SourceCodeGenerator.main(
	    	Array("scala.slick.driver.MySQLDriver", "com.mysql.jdbc.Driver", "jdbc:mysql://localhost/munkirjat_scala?user=root&password=&characterEncoding=UTF-8", "models", "model", "root", "")
	    )
	    */
	    Ok(views.html.home(""))
	}
	
	def aboutTemplate = Action {
	    Ok(views.html.about(""))
	}
	
	def loginTemplate = Action {
	    Ok(views.html.loginform(""))
	}
}