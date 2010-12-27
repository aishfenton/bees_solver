package com.visfleet.beessolver

import scala.collection.mutable.HashMap

object World {
  
  private val usedPositions = new HashMap[Int, Boolean]

  def isUsed(d: Domain) = usedPositions.contains(d.positionHash) 

  def at(d: Domain) = usedPositions(d.positionHash) = true 

  def clear = usedPositions.clear
  
  def size = usedPositions.size
  
}