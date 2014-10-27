package models
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = scala.slick.driver.MySQLDriver
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: scala.slick.driver.JdbcProfile
  import profile.simple._
  import scala.slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import scala.slick.jdbc.{GetResult => GR}
  
  /** DDL for all tables. Call .create to execute. */
  lazy val ddl = Author.ddl ++ Book.ddl ++ BookAuthor.ddl ++ Genre.ddl ++ PlayEvolutions.ddl ++ User.ddl ++ XiTag.ddl ++ XiTagging.ddl
  
  /** Entity class storing rows of table Author
   *  @param id Database column id DBType(INT), AutoInc, PrimaryKey
   *  @param firstname Database column firstname DBType(VARCHAR), Length(45,true)
   *  @param lastname Database column lastname DBType(VARCHAR), Length(45,true) */
  case class AuthorRow(id: Int, firstname: String, lastname: String)
  /** GetResult implicit for fetching AuthorRow objects using plain SQL queries */
  implicit def GetResultAuthorRow(implicit e0: GR[Int], e1: GR[String]): GR[AuthorRow] = GR{
    prs => import prs._
    AuthorRow.tupled((<<[Int], <<[String], <<[String]))
  }
  /** Table description of table author. Objects of this class serve as prototypes for rows in queries. */
  class Author(_tableTag: Tag) extends Table[AuthorRow](_tableTag, "author") {
    def * = (id, firstname, lastname) <> (AuthorRow.tupled, AuthorRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, firstname.?, lastname.?).shaped.<>({r=>import r._; _1.map(_=> AuthorRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column id DBType(INT), AutoInc, PrimaryKey */
    val id: Column[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column firstname DBType(VARCHAR), Length(45,true) */
    val firstname: Column[String] = column[String]("firstname", O.Length(45,varying=true))
    /** Database column lastname DBType(VARCHAR), Length(45,true) */
    val lastname: Column[String] = column[String]("lastname", O.Length(45,varying=true))
  }
  /** Collection-like TableQuery object for table Author */
  lazy val Author = new TableQuery(tag => new Author(tag))
  
  /** Entity class storing rows of table Book
   *  @param id Database column id DBType(INT), AutoInc, PrimaryKey
   *  @param title Database column title DBType(VARCHAR), Length(128,true)
   *  @param languageId Database column language_id DBType(VARCHAR), Length(3,true)
   *  @param pageCount Database column page_count DBType(INT)
   *  @param isRead Database column is_read DBType(BIT)
   *  @param isbn Database column isbn DBType(VARCHAR), Length(40,true), Default(None)
   *  @param createdAt Database column created_at DBType(DATETIME)
   *  @param updatedAt Database column updated_at DBType(DATETIME)
   *  @param startedReading Database column started_reading DBType(DATETIME), Default(None)
   *  @param finishedReading Database column finished_reading DBType(DATETIME), Default(None)
   *  @param rating Database column rating DBType(DOUBLE), Default(None)
   *  @param price Database column price DBType(DECIMAL), Default(None) */
  case class BookRow(id: Int, title: String, languageId: String, pageCount: Int, isRead: Boolean, isbn: Option[String] = None, createdAt: java.sql.Timestamp, updatedAt: java.sql.Timestamp, startedReading: Option[java.sql.Timestamp] = None, finishedReading: Option[java.sql.Timestamp] = None, rating: Option[Double] = None, price: Option[scala.math.BigDecimal] = None)
  /** GetResult implicit for fetching BookRow objects using plain SQL queries */
  implicit def GetResultBookRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Boolean], e3: GR[Option[String]], e4: GR[java.sql.Timestamp], e5: GR[Option[java.sql.Timestamp]], e6: GR[Option[Double]], e7: GR[Option[scala.math.BigDecimal]]): GR[BookRow] = GR{
    prs => import prs._
    BookRow.tupled((<<[Int], <<[String], <<[String], <<[Int], <<[Boolean], <<?[String], <<[java.sql.Timestamp], <<[java.sql.Timestamp], <<?[java.sql.Timestamp], <<?[java.sql.Timestamp], <<?[Double], <<?[scala.math.BigDecimal]))
  }
  /** Table description of table book. Objects of this class serve as prototypes for rows in queries. */
  class Book(_tableTag: Tag) extends Table[BookRow](_tableTag, "book") {
    def * = (id, title, languageId, pageCount, isRead, isbn, createdAt, updatedAt, startedReading, finishedReading, rating, price) <> (BookRow.tupled, BookRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, title.?, languageId.?, pageCount.?, isRead.?, isbn, createdAt.?, updatedAt.?, startedReading, finishedReading, rating, price).shaped.<>({r=>import r._; _1.map(_=> BookRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6, _7.get, _8.get, _9, _10, _11, _12)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column id DBType(INT), AutoInc, PrimaryKey */
    val id: Column[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column title DBType(VARCHAR), Length(128,true) */
    val title: Column[String] = column[String]("title", O.Length(128,varying=true))
    /** Database column language_id DBType(VARCHAR), Length(3,true) */
    val languageId: Column[String] = column[String]("language_id", O.Length(3,varying=true))
    /** Database column page_count DBType(INT) */
    val pageCount: Column[Int] = column[Int]("page_count")
    /** Database column is_read DBType(BIT) */
    val isRead: Column[Boolean] = column[Boolean]("is_read")
    /** Database column isbn DBType(VARCHAR), Length(40,true), Default(None) */
    val isbn: Column[Option[String]] = column[Option[String]]("isbn", O.Length(40,varying=true), O.Default(None))
    /** Database column created_at DBType(DATETIME) */
    val createdAt: Column[java.sql.Timestamp] = column[java.sql.Timestamp]("created_at")
    /** Database column updated_at DBType(DATETIME) */
    val updatedAt: Column[java.sql.Timestamp] = column[java.sql.Timestamp]("updated_at")
    /** Database column started_reading DBType(DATETIME), Default(None) */
    val startedReading: Column[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("started_reading", O.Default(None))
    /** Database column finished_reading DBType(DATETIME), Default(None) */
    val finishedReading: Column[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("finished_reading", O.Default(None))
    /** Database column rating DBType(DOUBLE), Default(None) */
    val rating: Column[Option[Double]] = column[Option[Double]]("rating", O.Default(None))
    /** Database column price DBType(DECIMAL), Default(None) */
    val price: Column[Option[scala.math.BigDecimal]] = column[Option[scala.math.BigDecimal]]("price", O.Default(None))
  }
  /** Collection-like TableQuery object for table Book */
  lazy val Book = new TableQuery(tag => new Book(tag))
  
  /** Entity class storing rows of table BookAuthor
   *  @param bookId Database column book_id DBType(INT)
   *  @param authorId Database column author_id DBType(INT) */
  case class BookAuthorRow(bookId: Int, authorId: Int)
  /** GetResult implicit for fetching BookAuthorRow objects using plain SQL queries */
  implicit def GetResultBookAuthorRow(implicit e0: GR[Int]): GR[BookAuthorRow] = GR{
    prs => import prs._
    BookAuthorRow.tupled((<<[Int], <<[Int]))
  }
  /** Table description of table book_author. Objects of this class serve as prototypes for rows in queries. */
  class BookAuthor(_tableTag: Tag) extends Table[BookAuthorRow](_tableTag, "book_author") {
    def * = (bookId, authorId) <> (BookAuthorRow.tupled, BookAuthorRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (bookId.?, authorId.?).shaped.<>({r=>import r._; _1.map(_=> BookAuthorRow.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column book_id DBType(INT) */
    val bookId: Column[Int] = column[Int]("book_id")
    /** Database column author_id DBType(INT) */
    val authorId: Column[Int] = column[Int]("author_id")
    
    /** Primary key of BookAuthor (database name book_author_PK) */
    val pk = primaryKey("book_author_PK", (bookId, authorId))
    
    /** Foreign key referencing Author (database name FK_9478D345F675F31B) */
    lazy val authorFk = foreignKey("FK_9478D345F675F31B", authorId, Author)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Book (database name FK_9478D34516A2B381) */
    lazy val bookFk = foreignKey("FK_9478D34516A2B381", bookId, Book)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table BookAuthor */
  lazy val BookAuthor = new TableQuery(tag => new BookAuthor(tag))
  
  /** Entity class storing rows of table Genre
   *  @param id Database column id DBType(INT), AutoInc, PrimaryKey
   *  @param name Database column name DBType(VARCHAR), Length(45,true) */
  case class GenreRow(id: Int, name: String)
  /** GetResult implicit for fetching GenreRow objects using plain SQL queries */
  implicit def GetResultGenreRow(implicit e0: GR[Int], e1: GR[String]): GR[GenreRow] = GR{
    prs => import prs._
    GenreRow.tupled((<<[Int], <<[String]))
  }
  /** Table description of table genre. Objects of this class serve as prototypes for rows in queries. */
  class Genre(_tableTag: Tag) extends Table[GenreRow](_tableTag, "genre") {
    def * = (id, name) <> (GenreRow.tupled, GenreRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, name.?).shaped.<>({r=>import r._; _1.map(_=> GenreRow.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column id DBType(INT), AutoInc, PrimaryKey */
    val id: Column[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name DBType(VARCHAR), Length(45,true) */
    val name: Column[String] = column[String]("name", O.Length(45,varying=true))
  }
  /** Collection-like TableQuery object for table Genre */
  lazy val Genre = new TableQuery(tag => new Genre(tag))
  
  /** Entity class storing rows of table PlayEvolutions
   *  @param id Database column id DBType(INT), PrimaryKey
   *  @param hash Database column hash DBType(VARCHAR), Length(255,true)
   *  @param appliedAt Database column applied_at DBType(TIMESTAMP)
   *  @param applyScript Database column apply_script DBType(TEXT), Length(65535,true), Default(None)
   *  @param revertScript Database column revert_script DBType(TEXT), Length(65535,true), Default(None)
   *  @param state Database column state DBType(VARCHAR), Length(255,true), Default(None)
   *  @param lastProblem Database column last_problem DBType(TEXT), Length(65535,true), Default(None) */
  case class PlayEvolutionsRow(id: Int, hash: String, appliedAt: java.sql.Timestamp, applyScript: Option[String] = None, revertScript: Option[String] = None, state: Option[String] = None, lastProblem: Option[String] = None)
  /** GetResult implicit for fetching PlayEvolutionsRow objects using plain SQL queries */
  implicit def GetResultPlayEvolutionsRow(implicit e0: GR[Int], e1: GR[String], e2: GR[java.sql.Timestamp], e3: GR[Option[String]]): GR[PlayEvolutionsRow] = GR{
    prs => import prs._
    PlayEvolutionsRow.tupled((<<[Int], <<[String], <<[java.sql.Timestamp], <<?[String], <<?[String], <<?[String], <<?[String]))
  }
  /** Table description of table play_evolutions. Objects of this class serve as prototypes for rows in queries. */
  class PlayEvolutions(_tableTag: Tag) extends Table[PlayEvolutionsRow](_tableTag, "play_evolutions") {
    def * = (id, hash, appliedAt, applyScript, revertScript, state, lastProblem) <> (PlayEvolutionsRow.tupled, PlayEvolutionsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, hash.?, appliedAt.?, applyScript, revertScript, state, lastProblem).shaped.<>({r=>import r._; _1.map(_=> PlayEvolutionsRow.tupled((_1.get, _2.get, _3.get, _4, _5, _6, _7)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column id DBType(INT), PrimaryKey */
    val id: Column[Int] = column[Int]("id", O.PrimaryKey)
    /** Database column hash DBType(VARCHAR), Length(255,true) */
    val hash: Column[String] = column[String]("hash", O.Length(255,varying=true))
    /** Database column applied_at DBType(TIMESTAMP) */
    val appliedAt: Column[java.sql.Timestamp] = column[java.sql.Timestamp]("applied_at")
    /** Database column apply_script DBType(TEXT), Length(65535,true), Default(None) */
    val applyScript: Column[Option[String]] = column[Option[String]]("apply_script", O.Length(65535,varying=true), O.Default(None))
    /** Database column revert_script DBType(TEXT), Length(65535,true), Default(None) */
    val revertScript: Column[Option[String]] = column[Option[String]]("revert_script", O.Length(65535,varying=true), O.Default(None))
    /** Database column state DBType(VARCHAR), Length(255,true), Default(None) */
    val state: Column[Option[String]] = column[Option[String]]("state", O.Length(255,varying=true), O.Default(None))
    /** Database column last_problem DBType(TEXT), Length(65535,true), Default(None) */
    val lastProblem: Column[Option[String]] = column[Option[String]]("last_problem", O.Length(65535,varying=true), O.Default(None))
  }
  /** Collection-like TableQuery object for table PlayEvolutions */
  lazy val PlayEvolutions = new TableQuery(tag => new PlayEvolutions(tag))
  
  /** Entity class storing rows of table User
   *  @param id Database column id DBType(INT), AutoInc, PrimaryKey
   *  @param username Database column username DBType(VARCHAR), Length(255,true)
   *  @param usernameCanonical Database column username_canonical DBType(VARCHAR), Length(255,true)
   *  @param email Database column email DBType(VARCHAR), Length(255,true)
   *  @param emailCanonical Database column email_canonical DBType(VARCHAR), Length(255,true)
   *  @param enabled Database column enabled DBType(BIT)
   *  @param salt Database column salt DBType(VARCHAR), Length(255,true)
   *  @param password Database column password DBType(VARCHAR), Length(255,true)
   *  @param lastLogin Database column last_login DBType(DATETIME), Default(None)
   *  @param locked Database column locked DBType(BIT)
   *  @param expired Database column expired DBType(BIT)
   *  @param expiresAt Database column expires_at DBType(DATETIME), Default(None)
   *  @param confirmationToken Database column confirmation_token DBType(VARCHAR), Length(255,true), Default(None)
   *  @param passwordRequestedAt Database column password_requested_at DBType(DATETIME), Default(None)
   *  @param roles Database column roles DBType(LONGTEXT), Length(2147483647,true)
   *  @param credentialsExpired Database column credentials_expired DBType(BIT)
   *  @param credentialsExpireAt Database column credentials_expire_at DBType(DATETIME), Default(None) */
  case class UserRow(id: Int, username: String, usernameCanonical: String, email: String, emailCanonical: String, enabled: Boolean, salt: String, password: String, lastLogin: Option[java.sql.Timestamp] = None, locked: Boolean, expired: Boolean, expiresAt: Option[java.sql.Timestamp] = None, confirmationToken: Option[String] = None, passwordRequestedAt: Option[java.sql.Timestamp] = None, roles: String, credentialsExpired: Boolean, credentialsExpireAt: Option[java.sql.Timestamp] = None)
  /** GetResult implicit for fetching UserRow objects using plain SQL queries */
  implicit def GetResultUserRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Boolean], e3: GR[Option[java.sql.Timestamp]], e4: GR[Option[String]]): GR[UserRow] = GR{
    prs => import prs._
    UserRow.tupled((<<[Int], <<[String], <<[String], <<[String], <<[String], <<[Boolean], <<[String], <<[String], <<?[java.sql.Timestamp], <<[Boolean], <<[Boolean], <<?[java.sql.Timestamp], <<?[String], <<?[java.sql.Timestamp], <<[String], <<[Boolean], <<?[java.sql.Timestamp]))
  }
  /** Table description of table user. Objects of this class serve as prototypes for rows in queries. */
  class User(_tableTag: Tag) extends Table[UserRow](_tableTag, "user") {
    def * = (id, username, usernameCanonical, email, emailCanonical, enabled, salt, password, lastLogin, locked, expired, expiresAt, confirmationToken, passwordRequestedAt, roles, credentialsExpired, credentialsExpireAt) <> (UserRow.tupled, UserRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, username.?, usernameCanonical.?, email.?, emailCanonical.?, enabled.?, salt.?, password.?, lastLogin, locked.?, expired.?, expiresAt, confirmationToken, passwordRequestedAt, roles.?, credentialsExpired.?, credentialsExpireAt).shaped.<>({r=>import r._; _1.map(_=> UserRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9, _10.get, _11.get, _12, _13, _14, _15.get, _16.get, _17)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column id DBType(INT), AutoInc, PrimaryKey */
    val id: Column[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column username DBType(VARCHAR), Length(255,true) */
    val username: Column[String] = column[String]("username", O.Length(255,varying=true))
    /** Database column username_canonical DBType(VARCHAR), Length(255,true) */
    val usernameCanonical: Column[String] = column[String]("username_canonical", O.Length(255,varying=true))
    /** Database column email DBType(VARCHAR), Length(255,true) */
    val email: Column[String] = column[String]("email", O.Length(255,varying=true))
    /** Database column email_canonical DBType(VARCHAR), Length(255,true) */
    val emailCanonical: Column[String] = column[String]("email_canonical", O.Length(255,varying=true))
    /** Database column enabled DBType(BIT) */
    val enabled: Column[Boolean] = column[Boolean]("enabled")
    /** Database column salt DBType(VARCHAR), Length(255,true) */
    val salt: Column[String] = column[String]("salt", O.Length(255,varying=true))
    /** Database column password DBType(VARCHAR), Length(255,true) */
    val password: Column[String] = column[String]("password", O.Length(255,varying=true))
    /** Database column last_login DBType(DATETIME), Default(None) */
    val lastLogin: Column[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("last_login", O.Default(None))
    /** Database column locked DBType(BIT) */
    val locked: Column[Boolean] = column[Boolean]("locked")
    /** Database column expired DBType(BIT) */
    val expired: Column[Boolean] = column[Boolean]("expired")
    /** Database column expires_at DBType(DATETIME), Default(None) */
    val expiresAt: Column[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("expires_at", O.Default(None))
    /** Database column confirmation_token DBType(VARCHAR), Length(255,true), Default(None) */
    val confirmationToken: Column[Option[String]] = column[Option[String]]("confirmation_token", O.Length(255,varying=true), O.Default(None))
    /** Database column password_requested_at DBType(DATETIME), Default(None) */
    val passwordRequestedAt: Column[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("password_requested_at", O.Default(None))
    /** Database column roles DBType(LONGTEXT), Length(2147483647,true) */
    val roles: Column[String] = column[String]("roles", O.Length(2147483647,varying=true))
    /** Database column credentials_expired DBType(BIT) */
    val credentialsExpired: Column[Boolean] = column[Boolean]("credentials_expired")
    /** Database column credentials_expire_at DBType(DATETIME), Default(None) */
    val credentialsExpireAt: Column[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("credentials_expire_at", O.Default(None))
    
    /** Uniqueness Index over (usernameCanonical) (database name UNIQ_957A647992FC23A8) */
    val index1 = index("UNIQ_957A647992FC23A8", usernameCanonical, unique=true)
    /** Uniqueness Index over (emailCanonical) (database name UNIQ_957A6479A0D96FBF) */
    val index2 = index("UNIQ_957A6479A0D96FBF", emailCanonical, unique=true)
  }
  /** Collection-like TableQuery object for table User */
  lazy val User = new TableQuery(tag => new User(tag))
  
  /** Entity class storing rows of table XiTag
   *  @param id Database column id DBType(INT), AutoInc, PrimaryKey
   *  @param name Database column name DBType(VARCHAR), Length(50,true)
   *  @param slug Database column slug DBType(VARCHAR), Length(50,true)
   *  @param createdAt Database column created_at DBType(DATETIME)
   *  @param updatedAt Database column updated_at DBType(DATETIME) */
  case class XiTagRow(id: Int, name: String, slug: String, createdAt: java.sql.Timestamp, updatedAt: java.sql.Timestamp)
  /** GetResult implicit for fetching XiTagRow objects using plain SQL queries */
  implicit def GetResultXiTagRow(implicit e0: GR[Int], e1: GR[String], e2: GR[java.sql.Timestamp]): GR[XiTagRow] = GR{
    prs => import prs._
    XiTagRow.tupled((<<[Int], <<[String], <<[String], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table xi_tag. Objects of this class serve as prototypes for rows in queries. */
  class XiTag(_tableTag: Tag) extends Table[XiTagRow](_tableTag, "xi_tag") {
    def * = (id, name, slug, createdAt, updatedAt) <> (XiTagRow.tupled, XiTagRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, name.?, slug.?, createdAt.?, updatedAt.?).shaped.<>({r=>import r._; _1.map(_=> XiTagRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column id DBType(INT), AutoInc, PrimaryKey */
    val id: Column[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column name DBType(VARCHAR), Length(50,true) */
    val name: Column[String] = column[String]("name", O.Length(50,varying=true))
    /** Database column slug DBType(VARCHAR), Length(50,true) */
    val slug: Column[String] = column[String]("slug", O.Length(50,varying=true))
    /** Database column created_at DBType(DATETIME) */
    val createdAt: Column[java.sql.Timestamp] = column[java.sql.Timestamp]("created_at")
    /** Database column updated_at DBType(DATETIME) */
    val updatedAt: Column[java.sql.Timestamp] = column[java.sql.Timestamp]("updated_at")
    
    /** Uniqueness Index over (name) (database name UNIQ_AD374A565E237E06) */
    val index1 = index("UNIQ_AD374A565E237E06", name, unique=true)
    /** Uniqueness Index over (slug) (database name UNIQ_AD374A56989D9B62) */
    val index2 = index("UNIQ_AD374A56989D9B62", slug, unique=true)
  }
  /** Collection-like TableQuery object for table XiTag */
  lazy val XiTag = new TableQuery(tag => new XiTag(tag))
  
  /** Entity class storing rows of table XiTagging
   *  @param id Database column id DBType(INT), AutoInc, PrimaryKey
   *  @param tagId Database column tag_id DBType(INT), Default(None)
   *  @param resourceType Database column resource_type DBType(VARCHAR), Length(50,true)
   *  @param resourceId Database column resource_id DBType(VARCHAR), Length(50,true)
   *  @param createdAt Database column created_at DBType(DATETIME)
   *  @param updatedAt Database column updated_at DBType(DATETIME) */
  case class XiTaggingRow(id: Int, tagId: Option[Int] = None, resourceType: String, resourceId: String, createdAt: java.sql.Timestamp, updatedAt: java.sql.Timestamp)
  /** GetResult implicit for fetching XiTaggingRow objects using plain SQL queries */
  implicit def GetResultXiTaggingRow(implicit e0: GR[Int], e1: GR[Option[Int]], e2: GR[String], e3: GR[java.sql.Timestamp]): GR[XiTaggingRow] = GR{
    prs => import prs._
    XiTaggingRow.tupled((<<[Int], <<?[Int], <<[String], <<[String], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table xi_tagging. Objects of this class serve as prototypes for rows in queries. */
  class XiTagging(_tableTag: Tag) extends Table[XiTaggingRow](_tableTag, "xi_tagging") {
    def * = (id, tagId, resourceType, resourceId, createdAt, updatedAt) <> (XiTaggingRow.tupled, XiTaggingRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (id.?, tagId, resourceType.?, resourceId.?, createdAt.?, updatedAt.?).shaped.<>({r=>import r._; _1.map(_=> XiTaggingRow.tupled((_1.get, _2, _3.get, _4.get, _5.get, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))
    
    /** Database column id DBType(INT), AutoInc, PrimaryKey */
    val id: Column[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    /** Database column tag_id DBType(INT), Default(None) */
    val tagId: Column[Option[Int]] = column[Option[Int]]("tag_id", O.Default(None))
    /** Database column resource_type DBType(VARCHAR), Length(50,true) */
    val resourceType: Column[String] = column[String]("resource_type", O.Length(50,varying=true))
    /** Database column resource_id DBType(VARCHAR), Length(50,true) */
    val resourceId: Column[String] = column[String]("resource_id", O.Length(50,varying=true))
    /** Database column created_at DBType(DATETIME) */
    val createdAt: Column[java.sql.Timestamp] = column[java.sql.Timestamp]("created_at")
    /** Database column updated_at DBType(DATETIME) */
    val updatedAt: Column[java.sql.Timestamp] = column[java.sql.Timestamp]("updated_at")
    
    /** Foreign key referencing XiTag (database name FK_431075D2BAD26311) */
    lazy val xiTagFk = foreignKey("FK_431075D2BAD26311", tagId, XiTag)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    
    /** Uniqueness Index over (tagId,resourceType,resourceId) (database name tagging_idx) */
    val index1 = index("tagging_idx", (tagId, resourceType, resourceId), unique=true)
  }
  /** Collection-like TableQuery object for table XiTagging */
  lazy val XiTagging = new TableQuery(tag => new XiTagging(tag))
}