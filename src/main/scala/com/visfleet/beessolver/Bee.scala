package com.visfleet.beessolver

import scala.collection.mutable.HashMap
import scala.util.Random

class Bee(domain: Domain) {
  
  val MAX_RETRIES = 200
  
  val fitness = domain.fitness  
    
  val theDomain = domain

  // Set position as in use
  World.at(domain)

  def copy(domain: Domain = this.domain.myCopy): Bee = {
    new Bee(domain)
  }

  // FIXME side effect is that it marks this domain as used
  def exploreIfUsed(exploreDistance: Double, i: Int): Bee = {
    if (World.isUsed(this.domain))
      return this.explore(exploreDistance: Double, i: Int)

    World.at(this.domain)
    this
  }

  // FIXME side effect is that it marks this domain as used
  def explore(exploreDistance: Double, i: Int): Bee = {
    var aDomain = domain.explore(exploreDistance, i)

    var count = 0 
    while (World.isUsed(aDomain) || World.isTabu(aDomain)) {  //  || World.isTabu(aDomain)
      count += 1
      var d = math.min(exploreDistance + (count / 100), 0.9)
    
      aDomain = domain.explore(d, i)
    
      if (count > MAX_RETRIES) 
        throw new Exception("Too many retries, stopping now")
    }

    new Bee(aDomain)
  }
  
  override def toString = domain.toString

}

object Bee {  
  
  def randomPosition(domainFunc: => Domain) = {
    var domain: Domain = domainFunc
    new Bee(domain.randomPosition)
  }
  
}
