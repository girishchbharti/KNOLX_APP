package com.knol.core.imp

case class Knol(val id: Int, val name: String, val email: String, val m_no: String) {
  def this(name: String, email: String, mNo: String) ={
    this(0, name, email, mNo)
  } 
}
