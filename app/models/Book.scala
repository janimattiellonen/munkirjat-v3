package models

case class Book (title: String, price: BigDecimal, languageId: String, pageCount: Int, isRead:Option[Boolean], isbn:String)