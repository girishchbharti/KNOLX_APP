package com.knol.core

import com.knol.core.imp._

trait SessionRepo {
  def create(knolx: Session): Int
  def remove(id: Int): Int
  def update(knolx: Session): Int
  def show(id: Int): Option[Session]
  def showAll(): List[Session]

}
