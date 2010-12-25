package com.visfleet.beessolver.vrp

import com.visfleet.beessolver.Domain
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.HashMap
import scala.util.Random

class Schedule(maxVehicles: Int, depot: Location, maxCapacity: Double, 
               maxRouteTime: Double, jobs: Array[Job], routes: ArrayBuffer[Route] = new ArrayBuffer[Route]) extends Object with Domain {
    
  val distance = routes.foldLeft(0.0)((r,c) => r + c.distance)

  val overtime = routes.foldLeft(0.0)((r,c) => r + c.overtime)

  val overload = routes.foldLeft(0.0)((r,c) => r + c.overload)

  val fitness: Double = {
    - ( this.distance )
    // - ( this.distance + (this.overtime * 2) + (this.overload * 2) )
  }

  Schedule.initNearMatrix(jobs)
  
  def copy(maxVehicles: Int = this.maxVehicles, 
           depot: Location = this.depot, 
           maxCapacity: Double = this.maxCapacity, 
           maxRouteTime: Double = this.maxRouteTime, 
           jobs: Array[Job] = this.jobs, 
           routes: ArrayBuffer[Route] = routes.map(_.copy())): Schedule = {
    new Schedule(maxVehicles, depot, maxCapacity, maxRouteTime, jobs, routes)
  }

  def positionHash = this.fitness.hashCode
  
  def randomPosition: Domain = {
    
    var usedJobs = new HashMap[Job, Boolean]
    var newRoutes = new ArrayBuffer[Route]
    
    for (i <- 0 to maxVehicles) {
      newRoutes += new Route(depot, maxRouteTime, maxCapacity)
      newRoutes.last += CollectionUtil.selectRandomAndUnused(jobs, usedJobs)
    }

    while (usedJobs.size < jobs.length) {
      var route = CollectionUtil.rand(newRoutes)
      route += CollectionUtil.selectRandomAndUnused(jobs, usedJobs)
    }

    this.copy(routes = newRoutes)
  }

  def explore(distance: Double, i: Int): Domain = {
    var newRoutes = routes.map(_.copy())
    lns(newRoutes, distance)
    this.copy(routes = newRoutes)
  }

// -----------
// Private
// -----------

  private def lns(routes: ArrayBuffer[Route], distance: Double) = {
    
    // remove some random job
    var removed = new ArrayBuffer[Job]
    
    for (i <- 0 until Random.nextInt((jobs.size * distance).toInt))
      removed += CollectionUtil.rand(routes).removeRandom
    
    while (removed.nonEmpty) {
      var job = CollectionUtil.removeRandom(removed)
      insertNearest(routes, job, distance)
    }
    
  }

  private def insertNearest(routes: ArrayBuffer[Route], job: Job, distance: Double) = {
    
    def nearestIndex(job: Job, offset: Int) = {
      indexOf(routes, nearestJob(job, offset))
    }
    
    var offset = Random.nextInt((jobs.size * distance * 2).toInt)    
    var idxs = nearestIndex(job, offset)

    while (idxs._1 != -1) {
      offset += 1
      idxs = nearestIndex(job, offset)
    }
    
    routes(idxs._1).insert(idxs._2, job)
  }
  
  private def nearestJob(job: Job, offset: Int) = Schedule.nearMatrix(job)(offset % Schedule.nearMatrix(job).size)

  private def indexOf(routes: ArrayBuffer[Route], job: Job): (Int, Int) = { 
    var r_idx = -1
    var j_idx = -1
    
    for(i <- 0 until routes.length) {
      r_idx = i
      j_idx = routes(i).indexOf(job)
      if (j_idx != -1) return (r_idx, j_idx)
    }
    (-1, -1)
  }


}


object Schedule {
  
  val nearMatrix = new HashMap[Job, Array[Job]]

  protected def initNearMatrix(jobs: Array[Job]) = {
    if (nearMatrix.isEmpty) {
      for (job <- jobs) {
        nearMatrix(job) = jobs.sortWith ( _.location.distanceTo(job.location) < _.location.distanceTo(job.location) )
      }
    }
  }
  
}


