package com.visfleet.beessolver

import com.visfleet.beessolver.vrp._
import com.visfleet.beessolver.vrp.data._

class Solver(iterations: Int, noSites: Int, noWorkerBees: Int, noMoves: Int, exploreDistance: Double, domainFunc: => Domain, monitorFunc: (Int, Double, Domain) => Unit) {

  val sites = new Array[Site](noSites)
  
  def solve = {
    this.seed
    
    for (i <- 1 to iterations)
      step(i)

    // return the best fitness
    sites.sortWith { (a,b) => a.bestBee.fitness > b.bestBee.fitness }(0).bestBee
  }

  def step(i: Int) = {

    if (i % 100 == 0) {
      println("---------------")
      sites.map { _.bestBee }.sortWith { _.fitness > _.fitness }.foreach( (bee: Bee) => monitorFunc(i, bee.fitness, bee.domain) )
    }

    World.clear
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
  
  // val problem = Simple
  val problem = AN32K5
  
  def main(args: Array[String]) {
    val solver = new Solver(100000, 25, 3, 2, 0.2, new Schedule(
      problem.MaxVehicles,
      problem.Depot,
      problem.MaxCapacity,
      problem.MaxRouteTime,
      problem.Jobs
      ),
      (i: Int, fitness: Double, domain: Domain) => {
        val schedule = domain.asInstanceOf[Schedule]
        println(i, fitness, schedule.distance, World.size)
      }
    )
    var solution = solver.solve
    println("Final answer: " + solution, solution.fitness)
  }
  
}
