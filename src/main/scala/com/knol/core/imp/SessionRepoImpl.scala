package com.knol.core.imp

import com.knol.db.connection.DBConnection
import com.knol.core.SessionRepo
import java.sql.Statement
import java.sql.ResultSet
import java.util.logging.Logger
import org.slf4j.LoggerFactory

class SessionRepoImpl extends SessionRepo with DBConnection {
  val logger = LoggerFactory.getLogger(this.getClass)

  /**
   * This function takes Session object and add new Session Record in database
   * Return 1 if successful 0 if error.
   */
  def create(session: Session): Int = {
    val con = getCon()

    con match {
      case Some(con) =>
        try {
          val id = session.id
          val stopic = session.topic
          val query = "insert into session values(" + id + ",'" + stopic + "','" + session.date + "'," + session.id + ");"
          val stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
          val numerno = stmt.executeUpdate()
          val rs = stmt.getGeneratedKeys()
          rs.next()
          val INSERTID = rs.getInt(1)
          INSERTID
        } catch {
          case ex: Exception =>
            logger.error("Error in creating session" + ex.printStackTrace())
            0
        }
      case None => 0
    }
  }

  /**
   * This function takes Session id as input
   * delete Session record from database
   * Return 1 if successful 0 if error
   */
  def remove(id: Int): Int = {
    val con = getCon()
    con match {
      case Some(con) =>
        try {
          val query = "delete from session where id=" + id
          val stmt = con.createStatement()
          val STATUS = stmt.executeUpdate(query)
          if (STATUS == 0)
            throw new IllegalArgumentException("arg 1 was wrong...");
          else
            STATUS
        } catch {
          case ex: Exception =>
            logger.error("Error on removing session" + ex.printStackTrace())
            0
        }
      case None => 0
    }
  }
  /**
   * This function takes session object
   * Update session details in database
   * Return 1 if successful 0 if error
   */
  def update(session: Session): Int = {
    val con = getCon()
    con match {
      case Some(con) =>
        try {
          val topic = session.topic
          val date = session.date
          val query = "update session set topic='" + topic + "', date='" + date + "', knolId=" + session.knol_id + " where id=" + session.id + ";"
          val stmt = con.createStatement()
          val STATUS = stmt.executeUpdate(query)
          if (STATUS == 0)
            throw new IllegalArgumentException("arg 1 was wrong...")
          else
            1
        } catch {
          case ex: Exception =>
            logger.error("Error on updating session" + ex.printStackTrace())
            0
        }
      case None =>
        logger.error("Error on founding connection")
        0
    }
  }
  /**
   * This function takes session id as input
   * Return session object from database
   */
  def show(id: Int): Option[Session] = {
    val con = getCon()
    con match {
      case Some(con) =>
        try {
          val query = "select * from session where id=" + id
          val stmt = con.createStatement()
          val rs: ResultSet = stmt.executeQuery(query)
          rs.next()
          val session = Session(rs.getInt("id"), rs.getString("topic"), rs.getString("date"), rs.getInt("knolId"))
          Some(session)
        } catch {
          case ex: Exception =>
            logger.error("Error fetching session info" + ex.printStackTrace())
            None
        }
      case None =>
        logger.error("Connection not found.")
        None
    }
  }
  /**
   * This function takes no arguments
   * Returns list of all sessions objects from database.
   */
  def showAll(): List[Session] = {
    val con = getCon()
    import scala.collection.mutable
    var list = List[Session]()
    con match {
      case Some(con) =>
        try {
          val query = "select * from session"
          val stmt = con.createStatement()
          val rs: ResultSet = stmt.executeQuery(query)
          if (rs.next() == false) 
            throw new IllegalArgumentException("No records found...");
          else
          {
            rs.previous()
            while (rs.next()) {
              val session = Session(rs.getInt("id"), rs.getString("topic"), rs.getString("date"), rs.getInt("knolId"))
              list = list.::(session)
            }
          }
          list
        } catch {
          case ex: Exception =>
            logger.error("Exception found on fetching info from database" + ex.printStackTrace())
            list
        }
      case None =>
        logger.error("Error on dabase connection")
        list
    }
  }
}
