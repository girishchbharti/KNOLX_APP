package com.knol.core.impl

import org.scalatest.FunSuite
import com.knol.db.connection.DBConnection
import org.scalatest.BeforeAndAfter
import org.slf4j.LoggerFactory
import com.knol.core.imp.KnolxRepoImpl
import com.knol.core.imp.Knolx
import com.knol.core.imp.Session

class KnolxRepoImplTest extends FunSuite with BeforeAndAfter with DBConnection {
  val con = getCon()
  val logger = LoggerFactory.getLogger(this.getClass)

  before {
    con match {
      case Some(con) =>
        try {
          var query = "create table knol(id int  primary key auto_increment,name varchar(20),email varchar(30),mNo varchar(10));"
          val stmt = con.createStatement()
          stmt.execute(query)
          query = "insert into knol values(null,'name','mail','1234')"
          stmt.execute(query)
          stmt.execute(query)

          query = "create table session(id int auto_increment primary key,topic varchar(30),date Date,knolId int);"
          stmt.execute(query)
          query = "insert into session values(null,'topic1','2015-01-01',1);"
          stmt.execute(query);
          query = "insert into session values(null,'topic2','2015-01-02',2);"
          stmt.execute(query);          

        } catch {
          case ex: Exception =>
            println(ex.printStackTrace())
        }
      case None =>
        println("Connection Not Found")
    }
  }
  
  val knolxRepoImpl=new KnolxRepoImpl()
  
  /**
   * Positive test cases
   */
  
  test("get knolx by id"){
    val session=Session(1,"topic1","2015-01-01",1)
    val list=List(session)
    assert(knolxRepoImpl.getSessionById(1)===list)
  }
  
  test("get knolx by date") {
    val knolx = new Knolx(1,"name","mail","1234", 1,"topic1","2015-01-01")
    val list = List(knolx)
    assert(knolxRepoImpl.getSessionByDate("2015-01-01") === list)
  }
  
  test("Get all knolx"){
    val knolx1=Knolx(1,"name","mail","1234",1,"topic1","2015-01-01");
    val knolx2=Knolx(2,"name","mail","1234",2,"topic2","2015-01-02");
    
    val list=List(knolx2,knolx1)
    assert(knolxRepoImpl.getAllKnolx()===list)
  }
 
  /**
   * Negative test cases
   */
  
  test("get knolx by id with wrong knol id"){
    val session=Session(1,"topic1","2015-01-01",1)
    val list=List()
    assert(knolxRepoImpl.getSessionById(5)===list)
  }
  
  test("get knolx by date with wrong date") {
    val knolx = new Knolx(1,"name","mail","1234", 1,"topic1","2015-01-01")
    val list = List()
    assert(knolxRepoImpl.getSessionByDate("2015-01-05") === list)
  }
  
  test("Get all knolx negative"){
    val knolx1=Knolx(1,"name","mail","1234",1,"topic1","2015-01-01");
    val knolx2=Knolx(2,"name","mail","1234",2,"topic2","2015-01-02");
    
    val list=List(knolx2,knolx1)
    assert(knolxRepoImpl.getAllKnolx()===list)
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