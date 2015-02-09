package com.knol.core.imp

import com.knol.db.connection.DBConnection
import com.knol.core.KnolxRepo
import org.slf4j.LoggerFactory

class KnolxRepoImpl extends KnolxRepo with DBConnection {
  val logger = LoggerFactory.getLogger(this.getClass)

  /**
   * This function takes knol id as input and return list of all sessions of knol having same id.
   */
  def getSessionById(id: Int): List[Session] = {
    import scala.collection.mutable
    var list = List[Session]()
    val con = getCon()

    con match {
      case Some(con) =>
        try {
          val query = "select * from knol k, session s where k.id=knolId and k.id=" + id
          val stmt = con.createStatement()
          val rs = stmt.executeQuery(query)
          if (rs.next() == false)
            throw new IllegalArgumentException("arg 1 was wrong...");
          else {
            rs.previous()
            while (rs.next()) {
              val session = Session(rs.getInt("id"), rs.getString("topic"), rs.getString("date"), rs.getInt("knolId"))
              list = list.::(session)
            }
          }
          list
        } catch {
          case ex: Exception =>
            logger.error("Error fetching data" + ex.printStackTrace())
            list
        }
      case None =>
        logger.error("Connection not found")
        list
    }

  }
  /**
   * This function takes date as input in format yyyy-mm-dd
   * Returns list of knolx having knolid, name, email, mobileNo,sessionid,topic,date
   */
  def getSessionByDate(datex: String): List[Knolx] = {
    import scala.collection.mutable
    var list = List[Knolx]()
    val con = getCon()

    con match {
      case Some(con) =>
        try {
          val query = "select name,email,mno,s.id,topic,date,knolId from knol k join session s on k.id=knolId where date='" + datex + "';"
          val stmt = con.createStatement()
          val rs = stmt.executeQuery(query)
          
          if (rs.next == false)
            throw new IllegalArgumentException("arg 1 was wrong...");
          else {
            rs.previous()
            while (rs.next()) {
              val knolid = rs.getInt("knolId")
              val name = rs.getString("name")

              val knolx =
                Knolx(knolid, name, rs.getString("email"), rs.getString("mno"), rs.getInt("s.id"), rs.getString("topic"), rs.getString("date"))
              list = list.::(knolx)
            }
          }
          list
        } catch {
          case ex: Exception =>
            logger.error("Error fetching data" + ex.printStackTrace())
            list
        }
      case None =>
        logger.error("Connection not found")
        list
    }

  }
  def getAllKnolx(): List[Knolx] = {
    import scala.collection.mutable
    var list = List[Knolx]()
    val con = getCon()

    con match {
      case Some(con) =>
        try {
          val query = "select k.id as knolid, name,email,mNo,s.id as sessionid,topic, date from knol k, session s where k.id=knolId"

          val stmt = con.createStatement()
          val rs = stmt.executeQuery(query)
          while (rs.next()) {
            val knolx =
              Knolx(rs.getInt("knolid"), rs.getString("name"), rs.getString("email"),
                rs.getString("mNo"), rs.getInt("sessionid"), rs.getString("topic"), rs.getString("date"))
            list = list.::(knolx)
          }
          list
        } catch {
          case ex: Exception =>
            logger.error("Error fetching data" + ex.printStackTrace())
            list
        }
      case None =>
        logger.error("Connection not found")
        list
    }
  }
}
