package com.knol.core.imp

case class Session(id: Int, topic: String, date: String, knol_id: Int) {
  def this(topic:String,date:String,knol_id:Int)={
    this(0,topic,date,knol_id)
  }
}
