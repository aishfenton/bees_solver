package com.visfleet.beessolver

trait Domain {

  def randomPosition : Domain
  def explore(distance: Double, i: Int): Domain
  def fitness : Double
  def positionHash: String
  
  def myCopy: Domain
  
}

