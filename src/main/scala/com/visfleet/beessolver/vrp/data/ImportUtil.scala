package com.visfleet.beessolver.vrp.data

import scala.util.Random
import scala.collection.mutable.HashMap
import scala.collection.mutable.ArrayBuffer
import com.visfleet.beessolver.vrp.Schedule
import com.visfleet.beessolver.vrp.Route
import com.visfleet.beessolver.vrp.Job
import com.visfleet.beessolver.vrp.Location

object ImportUtil {
  
  def toJobsWithoutId(data: List[(Int, Int, Int)], serviceTime: Double = 0.0) = {
    var count = 0
    this.toJobs(data.map( (a) => {
      count += 1
      (count, a._1, a._2, a._3)
    }), serviceTime)
  }

  def toJobs(data: List[(Int, Int, Int, Int)], serviceTime: Double = 0.0) = {
    val arr = new Array[Job](data.size)
    for (i <- 0 until arr.size) {
      var d = data(i)
      arr(i) = new Job(d._1.toString, new Location(d._2, d._3), d._4, serviceTime)
    }
    arr
  }
  
  def toSolution(problem: Problem, solution: List[List[Int]]) = {
    
    val jobMap = problem.jobs.foldLeft(new HashMap[String, Job])( (r, c) => { r(c.id) = c; r } )
    
    var routes = new ArrayBuffer[Route]
    
    for (r <- solution) {
      var jobs = r.map( (id: Int) => jobMap((id + 1).toString) ).toBuffer
      routes += new Route(problem.depot, problem.maxCapacity, problem.maxRouteTime, jobs)
    }
    
    new Schedule(problem.maxVehicles, problem.depot, problem.maxCapacity, problem.maxRouteTime, problem.jobs, routes)
  }

//  (\d+)\s+(\d+)\s+(\d+)\s+(\d+)
// ($1, $2, $3, $4),

}



