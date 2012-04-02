package com.routably.beessolver

trait Domain {

  def randomPosition : Domain
  def explore(distance: Double, i: Int): Domain
  def fitness : Double
  def positionHash: String
  
  def myCopy: Domain

  def isFeasible: Boolean
  
}

