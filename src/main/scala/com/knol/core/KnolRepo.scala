package com.knol.core

import com.knol.core.imp._;

trait KnolRepo {
  def add(knol: Knol): Option[Int]
  def remove(id: Int): Int
  def update(knol: Knol): Int
  def showKnol(id: Int): Option[Knol]
  def showAll(): List[Knol]

}
