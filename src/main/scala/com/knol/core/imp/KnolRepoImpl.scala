package com.knol.core.imp

import com.knol.core._
import com.typesafe.config.ConfigFactory
import java.sql.Connection
import com.knol.db.connection.DBConnection
import java.sql.Statement
import java.sql.ResultSet
import java.sql.PreparedStatement
import org.slf4j.LoggerFactory

class KnolRepoImpl extends KnolRepo with DBConnection {
  val logger = LoggerFactory.getLogger(this.getClass)

  /**
   * This function takes knol object as input
   * Insert new record in database
   * Returns Auto generated ID of knol object.
   */
  def add(knol: Knol): Option[Int] = {
    val conn = getCon()
    conn match {
      case Some(con) =>
        try {
          val query = "insert into knol values(null,'" + knol.name + "','" + knol.email + "','" + knol.m_no + "');"
          val stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
          val numero = stmt.executeUpdate()
          val rs: ResultSet = stmt.getGeneratedKeys();
          rs.next()
          val CREATED_ID = rs.getInt(1)
          logger.debug("Created ID:" + CREATED_ID)
          Some(CREATED_ID)
        } catch {
          case ex: Exception =>
            logger.error("Error in creating knol.", ex)
            None
        }
      case None => None
    }
  }
  /**
   * This function takes knol ID as integer
   * Deletes knol record from databse
   * Returns 1 if deleted successfully o if error.
   */
  def remove(id: Int): Int = {
    val conn = getCon()
    conn match {
      case Some(con) =>
        try {
          val query = "delete from knol where id=" + id
          val stmt = con.createStatement()
          val RES = stmt.executeUpdate(query)
          logger.debug("Removed successfully:" + id)
          if (RES == 0) {
            throw new IllegalArgumentException("arg 1 was wrong...");
          }
          RES
        } catch {
          case ex: Exception =>
            logger.error("Exception in sql connection" + ex);
            0
        }
      case None => 0
    }
  }
  /**
   * This function takes knol object
   * updates knol info
   * Return 1 if update successful 0 if error
   */
  def update(knol: Knol): Int = {
    val conn = getCon()
    conn match {
      case Some(conn) =>
        try {
          val query = "update knol set name='" + knol.name + "',email='" + knol.email + "',mNo='" + knol.m_no + "' where id=" + knol.id;
          val stmt = conn.createStatement()
          val n = stmt.executeUpdate(query)
          n
        } catch {
          case ex: Exception => 0
        }
      case None =>
        0
    }
  }
  /**
   * This function takes id of knol as integer.
   * Fetches record of the knol form database
   * Returns knol object with information.
   */

  def showKnol(id: Int): Option[Knol] = {
    val conn = getCon()
    conn match {
      case Some(conn) =>
        try {
          val query = "select * from knol where id=" + id;
          val stmt = conn.createStatement()
          val rs: ResultSet = stmt.executeQuery(query)
          rs.next()
          val knol = new Knol(rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getString("mNo"))
          Some(knol)
        } catch {
          case ex: Exception => None
        }
      case None => None
    }
  }
  /**
   * This function takes no arguments.
   * Returns List of all All knol objects from database.
   */
  def showAll(): List[Knol] = {
    val conn = getCon()
    import scala.collection.mutable
    var list = List[Knol]()
    conn match {
      case Some(conn) =>
        try {
          val query = "select * from knol;"
          val stmt = conn.createStatement()
          val rs: ResultSet = stmt.executeQuery(query)
          if (rs.next()==false)
            throw new IllegalArgumentException("No records found...");
          else{
            rs.previous()
            while (rs.next()){
              val knol = new Knol(rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getString("mNo"))
              list = list.::(knol)
            }
          }
          list
        } 
        catch {
          case ex: Exception => 
            logger.error("No records found in show all function")
            list
        }
      case None =>list
    }
  }
}
