package com.knol.core

import com.knol.core.imp.Session
import com.knol.core.imp.Knolx

trait KnolxRepo {
  def getSessionById(id: Int): List[Session]
  def getSessionByDate(date: String): List[Knolx]
  def getAllKnolx(): List[Knolx]
}
