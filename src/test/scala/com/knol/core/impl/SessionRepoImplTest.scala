package com.knol.core.impl

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import com.knol.db.connection.DBConnection
import com.knol.core.imp.SessionRepoImpl
import com.knol.core.imp.Session

class SessionRepoImplTest extends FunSuite  with BeforeAndAfter with DBConnection {
  val con=getCon()
  
    before {
    con match {
      case Some(con) =>
        try {
          var query = "create table knol(id int  primary key auto_increment,name varchar(20),email varchar(30),mNo varchar(10));"
          val stmt = con.createStatement()
          stmt.execute(query)
          query = "insert into knol values(null,'name','email','1234')"
          stmt.execute(query)
          stmt.execute(query)

          query = "create table session(id int auto_increment primary key,topic varchar(30),date Date unique,knolId int);"
          stmt.execute(query)
          query = "insert into session values(null,'topic1','2015-01-01',1);"
          stmt.execute(query);
        } catch {
          case ex: Exception =>
            println(ex.printStackTrace())
        }
      case None =>
        println("Connection Not Found")
    }
  }
    /**
   *
   * Test case for SessionRepoImpl
   */

  val sessionRepo=new SessionRepoImpl()
  
  test("Create session") {
    val session = new Session("Topic2", "2015-01-02", 2)
    assert(sessionRepo.create(session) === 2)
  }

  test("Delete Session") {
    assert(sessionRepo.remove(1) === 1)
  }

  test("Update Session") {
    val session = Session(1, "Topic2", "2015-01-02", 2)
    assert(sessionRepo.update(session) === 1)
  }

  test("Show Session") {
    val session = Session(1, "topic1", "2015-01-01", 1)
    assert(sessionRepo.show(1).get === session)
  }

  test("Show all sessions") {
    import scala.collection.mutable
    val session1 = Session(1, "topic1", "2015-01-01", 1)
    val list = List(session1)
    assert(sessionRepo.showAll() === list)
  }
  
  /**
   * Negative test cases.
   * 
   */
  test("Create session with wrong input") {
    val session = Session(0,"topic1","2015-01-01",1)
    assert(sessionRepo.create(session) === 0)

  }

  test("Delete Session with wrong session id") {
    assert(sessionRepo.remove(5) === 0)
  }

  test("Update Session with wrong input") {
    val session = Session(6, "Topic2", "2015-01-02", 2)
    assert(sessionRepo.update(session) === 0)
  }

  test("Show Session negative") {
    val session = Session(1, "topic1", "2015-01-01", 1)
    assert(sessionRepo.show(5) === None)
  }

  test("Show all sessions negative") {
    val con = getCon()
    con match {
      case Some(con) =>
        var query = "delete from session"
        val stmt = con.createStatement()
        stmt.execute(query)
      case None =>
    }
    import scala.collection.mutable
    val list:List[Session] = List()
    assert(sessionRepo.showAll() === list)
  }
    after {
    val con = getCon()
    con match {
      case Some(con) =>
        try {
          var query = "drop table knol"
          val stmt = con.createStatement()
          stmt.execute(query)
          query = "drop table session"
          stmt.execute(query)
        } catch {
          case ex: Exception =>
            println(ex.printStackTrace())
        }
      case None =>
        println("Connection Not Found")
    }
  }
  
}