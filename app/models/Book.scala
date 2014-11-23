package models

case class Book (title: String, price: BigDecimal, languageId: String, pageCount: Int, isRead:Option[Boolean], startedReading:Option[String], finishedReading:Option[String], isbn:String)