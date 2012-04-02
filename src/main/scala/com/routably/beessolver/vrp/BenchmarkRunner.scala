package com.routably.beessolver.vrp

import com.routably.beessolver.World
import com.routably.beessolver.vrp.data._
import com.routably.beessolver.vrp.monitor.DatOut
import com.routably.beessolver.vrp.monitor.Debug

object BenchmarkRunner {
  
  def main(args: Array[String]) {

    val maxTime = args(0).toLong
    val setName = args(1)
    val problems = args(2).split(",")
   
    for (problem <- problems) {
      runProblem(problem, setName, maxTime)
    }

  }
  
  def runProblem(problemStr: String, setName: String, maxTime: Long) = {

    val clazz = Class.forName("com.routably.beessolver.vrp.data." + problemStr + "$")
    val problem = clazz.getField("MODULE$").get(null).asInstanceOf[Problem]

    val solver = new World(1000000, maxTime, 100, 3, 1, 0.4, new Schedule(
      problem.maxVehicles,
      problem.depot,
      problem.maxCapacity,
      problem.maxRouteTime,
      problem.jobs
      ),
      // new Debug
      new DatOut(setName, problem)
    )
    var solution = solver.start
  }

}