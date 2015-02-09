package com.knol.db.connection

import com.typesafe.config.ConfigFactory
import java.sql.DriverManager
import java.sql.Driver
import java.sql.Connection

trait DBConnection {
  val config = ConfigFactory.load()
  val url = config.getString("db.url")
  val usr = config.getString("db.user")
  val pass = config.getString("db.pass")
  val driver = config.getString("db.driver")
  def getCon(): Option[Connection] =
    {
      try {
        Class.forName(driver)
        val Con = DriverManager.getConnection(url, usr, pass)
        Some(Con)
      } catch {
        case e: Exception =>
          e.printStackTrace()
          None
      }
    }
}
