package com.visfleet.beessolver

import scala.collection.mutable.ArrayBuffer

class Site(noWorkerBees: Int, domainFunc: => Domain) {
  
  var bees: Array[Bee] = Array.fill(noWorkerBees) {
    Bee.randomPosition(domainFunc)
  }

  def explore(noMoves: Int, exploreDistance: Double, i: Int) = {
    // we keep an array of all the best positions from this explore
    var positions = new ArrayBuffer[Bee]

    positions += bestBee
    for (bee <- bees) {
      for (j <- 0 to noMoves) {
        positions += bee.explore(exploreDistance, i)
      }
    }
    
    positions = positions.sortWith( (a,b) => a.fitness > b.fitness )
    bees = positions.slice(0, noWorkerBees).toArray
  }
 
  def bestBee = bees(0)
   
}

object Site {
  
  def randomPosition(noWorkerBees: Int, domainFunc: => Domain) = {
    new Site(noWorkerBees, domainFunc)
  }
  
}