package com.routably.beessolver

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

class Site(noWorkerBees: Int, domainFunc: => Domain, age: Int = 0, bees: Array[Bee]) {

  val theAge = age

  val fitness = bestBee.fitness

  val avgFitness = bees.foldLeft(0.0)( _ + _.fitness ) / bees.size
  
  def copy(noWorkerBees: Int = this.noWorkerBees,
           // FIXME causes huge memory leak, WTF?? 
           // domainFunc: => Domain = this.domainFunc,
           age: Int = this.age,
           bees: Array[Bee] = this.bees.map(_.copy())): Site = {
    new Site(noWorkerBees, this.domainFunc, age, bees)
  }
  
  def isFeasible = bestBee.isFeasible
  
  def explore(noMoves: Int, exploreDistance: Double, i: Int): Site = {
    
    // we keep an array of all the best positions from this explore
    var positions = new ArrayBuffer[Bee]
        
    for (bee <- bees) {
      for (j <- 0 to noMoves) {
        positions += bee.explore(exploreDistance, i)
      }
    }
        
    val avg = positions.foldLeft(0.0)(_ + _.fitness) / positions.size
    if (Random.nextDouble < ( (fitness - avg) / 50 ) || age < 100) {
      positions += bestBee.exploreIfUsed(age / 1000, i)
    } 
    
    positions = positions.sortWith( (a,b) => a.fitness > b.fitness )

    var aBees = positions.slice(0, noWorkerBees).toArray
    val aAge = if (aBees.first.fitness <= fitness) age + 1 else 0

    this.copy(bees = aBees, age = aAge)
  }
   
  def bestBee = bees(0)

  def mate(seedSite: Site) = {
    var aBees = new ArrayBuffer[Bee]
    aBees ++= bees.slice(0, (this.bees.size / 2.0).floor.toInt)
    aBees ++= bees.slice(0, (seedSite.theBees.size / 2.0).ceil.toInt)
    this.copy(bees = aBees.toArray, age = 0)
  }

  protected def theBees = bees
   
}

object Site {
  
  def randomPosition(noWorkerBees: Int, domainFunc: => Domain) = {
    val bees: Array[Bee] = Array.fill(noWorkerBees) {
      Bee.randomPosition(domainFunc)
    }
    new Site(noWorkerBees, domainFunc, 0, bees)
  }
    
}