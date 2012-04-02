package com.routably.beessolver.vrp

import com.routably.beessolver.World
import com.routably.beessolver.vrp.data.Problem
import com.routably.beessolver.vrp.monitor.Debug

object Runner {
  
  var problem: Problem = null

  def main(args: Array[String]) {

    println("Starting...")
    
    val clazz = Class.forName("com.routably.beessolver.vrp.data." + args(0) + "$")
    problem = clazz.getField("MODULE$").get(null).asInstanceOf[Problem]

    val solver = new World(2000000, 60 * 50, 2, 1, 1, 0.4, new Schedule(
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