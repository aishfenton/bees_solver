package com.routably.beessolver.vrp

import com.routably.beessolver.Domain
import scala.collection.mutable.Buffer
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.HashMap
import scala.util.Random

class Schedule(maxVehicles: Int, depot: Location, maxCapacity: Double, 
               maxRouteTime: Double, jobs: Array[Job], routes: Buffer[Route] = new ArrayBuffer[Route]) extends Object with Domain {

  type Position = (Int, Int)

  val Penalty = 6
    
  val distance = routes.foldLeft(0.0)((r,c) => r + c.distance)

  val overtime = routes.foldLeft(0.0)((r,c) => r + c.overtime)

  val overload = routes.foldLeft(0.0)((r,c) => r + c.overload)

  val load = routes.foldLeft(0.0)((r,c) => r + c.load)
  
  val serviceTime = routes.foldLeft(0.0)((r,c) => r + c.serviceTime)

  val fitness: Double = {
    - ( this.distance + (this.overtime * Penalty) + (this.overload * Penalty) )
  }

  Schedule.initNearMatrix(jobs)
  
  def myCopy() = this.copy()
  
  def copy(maxVehicles: Int = this.maxVehicles, 
           depot: Location = this.depot,
           maxCapacity: Double = this.maxCapacity, 
           maxRouteTime: Double = this.maxRouteTime, 
           jobs: Array[Job] = this.jobs, 
           routes: Buffer[Route] = routes.map(_.copy())): Schedule = {
    new Schedule(maxVehicles, depot, maxCapacity, maxRouteTime, jobs, routes)
  }

  def positionHash = this.fitness.toString
  
  def randomPosition: Domain = {
    
    var usedJobs = new HashMap[Job, Boolean]
    var newRoutes = new ArrayBuffer[Route]
    
    // seed randomly
    for (i <- 0 until maxVehicles) {
      var route = new Route(depot, maxCapacity, maxRouteTime)
      newRoutes += (route.append(CollectionUtil.selectRandomAndUnused(jobs, usedJobs)))
    }

    while (usedJobs.size < jobs.size) {
      insertNearestRandomly(newRoutes, CollectionUtil.selectRandomAndUnused(jobs, usedJobs), 0.02)
    }

    this.copy(routes = newRoutes)
  }

  def explore(eDistance: Double, i: Int): Domain = {
    var newRoutes = routes.map(_.copy())
    lns(newRoutes, eDistance)
    this.copy(routes = newRoutes)
  }
  
  override def toString = routes.foldLeft("")( _ + _.toString + "\n" )

  def isFeasible = (this.overtime + this.overload) == 0

// -----------
// Private
// -----------

  private def lns(routes: Buffer[Route], eDistance: Double) = {
    
    var removed = new ArrayBuffer[Job]

    // RANDOM REMOVE
    def randomRemove(number: Int) = {
      for (i <- 0 until number) {
        val r_idx = Random.nextInt(routes.size)

        if (routes(r_idx).isEmpty) {
          if (!removed.isEmpty)
            routes(r_idx) = routes(r_idx).append(CollectionUtil.removeRandom(removed))
        } else {
          var result:(Route, Job) = routes(r_idx).remove(Random.nextInt(routes(r_idx).size))
          routes(r_idx) = result._1
          removed += result._2
        }
		
      }
    } 

    def seqRemove(number: Int): Unit = {
              
      def removeEm(job: Job): Unit = for (i <- 0 until number) {
        val idxs = findNJ(routes, job, i)
        
        if (idxs._1 == -1 || Random.nextDouble < 0.2)
          return

        var result:(Route, Job) = routes(idxs._1).remove(idxs._2)
        routes(idxs._1) = result._1
        removed += result._2
        
      }
      
      while (removed.size < number) {
        var route = CollectionUtil.rand(routes)
        if (route.isEmpty)
          return
        
        var job = route(Random.nextInt(route.size))
        removeEm(job)
      }
      
    }
    
    val number = Random.nextInt((jobs.size * 0.4).toInt)
    if (Random.nextBoolean) seqRemove(number) else randomRemove(number)
    
    while (removed.nonEmpty) {
      var job = CollectionUtil.removeRandom(removed)
      insertNearest(routes, job, eDistance)
    }
    
  }

  private def findNJ(routes: Buffer[Route], job: Job, offset: Int) = {
    indexOf(routes, nearestJob(job, offset))
  }

  private def insertNearest(routes: Buffer[Route], job: Job, eDistance: Double) = {
        
    def findNJWithCost(offset: Int): (Position, Double) = {
      val idxs = findNJ(routes, job, offset)

      if (idxs._1 == -1)
        return null;
      
      val l = routes(idxs._1).insertionCost(idxs._2, job)
      val r = routes(idxs._1).insertionCost(idxs._2 + 1, job)
      
      if (l < r) (idxs, l) else ((idxs._1, idxs._2 + 1), r)
    }
    
    var bestPos: (Position, Double) = null
    
    def place(searchSize: Int) = for (offset <- 0 until searchSize) {
      val pos = findNJWithCost(offset)
            
      if (bestPos == null || (pos != null && pos._2 < bestPos._2))
        bestPos = pos
    }
    place(math.max((jobs.size * eDistance).toInt, 3))
        
    // Retry with larger expanded search if fails
    if (bestPos == null) {
      place(jobs.size)
    }
    
    val bestIdxs = bestPos._1
    routes(bestIdxs._1) = routes(bestIdxs._1).insert(bestIdxs._2, job)
  }

  private def insertNearestRandomly(routes: Buffer[Route], job: Job, eDistance: Double) = {
    
    def nearestIndex(job: Job, offset: Int) = {
      indexOf(routes, nearestJob(job, offset))
    }
    
    var offset = Random.nextInt((jobs.size * eDistance).toInt)    
    var idxs = nearestIndex(job, offset)

    while (idxs._1 == -1) {
      offset += 1
      idxs = nearestIndex(job, offset)
    }
    
    routes(idxs._1) = routes(idxs._1).insertEitherSide(idxs._2, job)
  }
  
  private def nearestJob(job: Job, offset: Int) = { Schedule.nearMatrix(job)(offset % Schedule.nearMatrix(job).size) }

  private def indexOf(routes: Buffer[Route], job: Job): (Int, Int) = { 
    var r_idx = -1
    var j_idx = -1
    
    for(i <- 0 until routes.size) {
      r_idx = i
      j_idx = routes(i).indexOf(job)
      if (j_idx != -1) return (r_idx, j_idx)
    }
    (-1, -1)
  }

}

object Schedule {
  
  val nearMatrix = new HashMap[Job, Array[Job]]

  def clear = nearMatrix.clear

  protected def initNearMatrix(jobs: Array[Job]) = {
    if (nearMatrix.isEmpty) {
      for (job <- jobs) {
        nearMatrix(job) = jobs.sortWith ( _.location.distanceTo(job.location) < _.location.distanceTo(job.location) )
        // remove selfs
        nearMatrix(job) = nearMatrix(job).slice(1, nearMatrix(job).size)
      }
    }
  }
  
}
