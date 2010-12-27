package com.visfleet.beessolver

import scala.collection.mutable.HashMap
import scala.util.Random

class Bee(_domain: Domain) {
  
  val MAX_RETRIES = 300000
  
  val domain = _domain

  val fitness = domain.fitness  
    
  def exploreIfUsed(exploreDistance: Double, i: Int): Bee = {
    if (World.isUsed(this.domain))
      return this.explore(exploreDistance: Double, i: Int)

    World.at(this.domain)
    this
  }

  def explore(exploreDistance: Double, i: Int): Bee = {
    var aDomain = domain.explore(exploreDistance, i)

    var count = 0
    while (World.isUsed(aDomain) && count < MAX_RETRIES) {
      count += 1
      aDomain = domain.explore(exploreDistance, i)
    }

    World.at(aDomain)
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
