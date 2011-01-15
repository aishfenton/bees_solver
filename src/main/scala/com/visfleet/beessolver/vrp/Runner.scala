package com.visfleet.beessolver.vrp

import com.visfleet.beessolver.World
import com.visfleet.beessolver.vrp.data.Problem
import com.visfleet.beessolver.vrp.monitor.Debug

object Runner {
  
  // val problems = List(Simple, AN32K5, AN55K9)
  var problem: Problem = null

  def main(args: Array[String]) {

    println("Starting...")
    
    val clazz = Class.forName("com.visfleet.beessolver.vrp.data." + args(0) + "$")
    problem = clazz.getField("MODULE$").get(null).asInstanceOf[Problem]

    val solver = new World(200000, 60 * 50, 25, 2, 1, 0.4, new Schedule(
      problem.maxVehicles,
      problem.depot,
      problem.maxCapacity,
      problem.maxRouteTime,
      problem.jobs
      ),
      new Debug
    )
    solver.start
  }

}