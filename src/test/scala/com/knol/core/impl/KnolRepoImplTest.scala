package com.knol.core.impl

import org.scalatest.FunSuite
import com.knol.core.imp.KnolRepoImpl
import com.knol.core.imp.Knol
import org.scalatest.BeforeAndAfter
import com.knol.core.imp.SessionRepoImpl
import com.knol.core.imp.SessionRepoImpl
import com.knol.core.imp.SessionRepoImpl
import com.knol.core.imp.Session
import com.knol.db.connection.DBConnection
import com.knol.core.imp.KnolxRepoImpl
import com.knol.core.imp.Knolx
import com.knol.core.imp.Session
import com.knol.core.imp.Knol

class KnolRepoImplTest extends FunSuite with BeforeAndAfter with DBConnection {
  val con = getCon()

  before {
    con match {
      case Some(con) =>
        try {
          var query = "create table knol(id int  primary key auto_increment,name varchar(20),email varchar(30) unique,mNo varchar(10));"
          val stmt = con.createStatement()
          stmt.execute(query)
          query = "insert into knol values(null,'girish','girish@knoldus.com','1234567890')"
          stmt.execute(query)
          query = "insert into knol values(null,'girish','girish1@knoldus.com','1234567890')"

          stmt.execute(query)

          query = "create table session(id int auto_increment primary key,topic varchar(30),date Date,knolId int);"
          stmt.execute(query)
          query = "insert into session values(null,'topic1','2015-01-01',2);"
          stmt.execute(query);

        } catch {
          case ex: Exception =>
            println(ex.printStackTrace())
        }
      case None =>
        println("Connection Not Found")
    }
  }
  val repo = new KnolRepoImpl()
  val sessionRepo = new SessionRepoImpl()

  test("Create a knol") {
    val knol = new Knol("name", "mail", "1234")
    assert(repo.add(knol).get === 3)
  }

  test("Update a knol") {
    val knol = Knol(1, "mohit", "mohit@gmail", "1234")
    assert(repo.update(knol) === 1)
  }

  test("Delete a knol") {
    assert(repo.remove(1) === 1)
  }

  test("Show a knol") {
    assert(repo.showKnol(2).get === Knol(2, "girish", "girish1@knoldus.com", "1234567890"))
  }

  test("Show List of knol") {
    import scala.collection.mutable
    val knol1 = Knol(1, "girish", "girish@knoldus.com", "1234567890")
    val knol2 = Knol(2, "girish", "girish1@knoldus.com", "1234567890")
    val list = List(knol2, knol1)
    assert(repo.showAll() === list)
  }

  /**
   * Create session with wrong input
   */

  test("Create a knol with wrong input") {
    val knol = new Knol("girish", "girish1@knoldus.com", "1234567890")
    assert(repo.add(knol) === None)
  }

  test("Update a knol with wrong input") {
    val knol = Knol(1, "mohit", "girish1@knoldus.com", "1234")
    assert(repo.update(knol) === 0)
  }

  test("Delete a knol with wrong id") {
    assert(repo.remove(4) === 0)
  }

  test("Show a knol with wrong id") {
    assert(repo.showKnol(4) === None)
  }

  test("Show List of all knols with wrong input") {
    val con = getCon()
    con match {
      case Some(con) =>
        var query = "delete from knol"
        val stmt = con.createStatement()
        stmt.execute(query)
      case None =>
    }
    import scala.collection.mutable
    val list = List()
    assert(repo.showAll() === list)
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
