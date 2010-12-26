package com.visfleet.beessolver

import scala.collection.mutable.HashMap

class Bee(aDomain: Domain) {
      
  val fitness = domain.fitness
  
  def domain = aDomain
  
  def explore(exploreDistance: Double, i: Int): Bee = {
    var aDomain = domain.explore(exploreDistance, i)

    var count = 0
    while (Bee.usedPositions.contains(aDomain.positionHash) && count < 30) {
      count += 1
      // print(".")
      aDomain = domain.explore(exploreDistance, i)
    }

    Bee.usedPositions(aDomain.positionHash) = true

    new Bee(aDomain)
  }
  
  override def toString = domain.toString
  
  // def self.purge_positions
  //   puts "purge"
  //   while @@positions.size > (MAX_POSITIONS / 2)
  //     max = @@positions.keys.max
  //     min = @@positions.keys.min
  //     puts "    min:#{min}, max:#{max}, mid:#{(min+max)/2}"
  //     @@positions.delete_if { |key, value| key < ((min + max) / 2) } # delete if over halfway point
  //   end
  // end
      
}

object Bee {  
  var usedPositions = new HashMap[Int, Boolean]

  def randomPosition(domainFunc: => Domain) = {
    var domain: Domain = domainFunc
    new Bee(domain.randomPosition)
  }
  
}
