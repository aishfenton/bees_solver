package com.visfleet.beessolver

import com.visfleet.beessolver.vrp._

class Solver(iterations: Int, noSites: Int, noWorkerBees: Int, noMoves: Int, exploreDistance: Double, domainFunc: => Domain) {

  val sites = new Array[Site](noSites)
  
  def solve = {
    this.seed
    
    for (i <- 1 to iterations)
      step(i)

    // return the best fitness
    sites.sortWith { (a,b) => a.bestBee.fitness > b.bestBee.fitness }(0).bestBee.fitness
  }

  def step(i: Int) = {

    if (i % 5 == 0) {
      println("Iteration:" + i)
      println("Best bees: " + (sites map { _.bestBee.fitness } toList))
    }

    for (site <- sites) {
      site.explore(noMoves, exploreDistance, i)
    }
    
  }
  
  def seed() = {
    for (i <- 0 until noSites) {
      sites(i) = Site.randomPosition(noWorkerBees, domainFunc)
    }
  }
  
}

object Solver {
  def main(args: Array[String]) {
    val solver = new Solver(30000, 2, 5, 3, 0.5, new Schedule(0, new Location(0,0), 10, 10, 
      Array(new Job("job 1", new Location(1,1), 1.0, 1.0))))
    println("Final answer: " + solver.solve)
  }
}

