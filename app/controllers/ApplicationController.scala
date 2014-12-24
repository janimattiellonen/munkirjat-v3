package controllers

import com.mohiva.play.silhouette.core.LogoutEvent
import play.api._
import play.api.mvc._

import models.User
import com.mohiva.play.silhouette.core.{LogoutEvent, Environment, Silhouette}
import com.mohiva.play.silhouette.contrib.services.CachedCookieAuthenticator
import scala.concurrent.Future
import javax.inject.Inject
import forms._

import scala.slick.driver.MySQLDriver.simple._

/**
 * The basic application controller.
 *
 * @param env The Silhouette environment.
 */
class ApplicationController @Inject() (implicit val env: Environment[User, CachedCookieAuthenticator])
	extends Silhouette[User, CachedCookieAuthenticator] {

	def index = SecuredAction.async { implicit request =>

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

		Future.successful(Ok(views.html.index("s")))
	}


	/**
	 * Handles the Sign In action.
	 *
	 * @return The result to display.
	 */
	def signIn = UserAwareAction.async { implicit request =>
		request.identity match {
			case Some(user) => Future.successful(Redirect(routes.ApplicationController.index))
			case None => Future.successful(Ok(views.html.signIn(SignInForm.form)))
		}
	}

	/**
	 * Handles the Sign Up action.
	 *
	 * @return The result to display.
	 */
	def signUp = UserAwareAction.async { implicit request =>
		request.identity match {
			case Some(user) => Future.successful(Redirect(routes.ApplicationController.index))
			case None => Future.successful(Ok(views.html.signUp(SignUpForm.form)))
		}
	}

	/**
	 * Handles the Sign Out action.
	 *
	 * @return The result to display.
	 */
	def signOut = SecuredAction.async { implicit request =>
		env.eventBus.publish(LogoutEvent(request.identity, request, request2lang))
		Future.successful(env.authenticatorService.discard(Redirect(routes.ApplicationController.index)))
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