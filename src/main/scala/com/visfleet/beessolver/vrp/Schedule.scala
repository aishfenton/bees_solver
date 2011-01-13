package com.visfleet.beessolver.vrp

import com.visfleet.beessolver.Domain
import scala.collection.mutable.Buffer
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.HashMap
import scala.util.Random

class Schedule(maxVehicles: Int, depot: Location, maxCapacity: Double, 
               maxRouteTime: Double, jobs: Array[Job], routes: Buffer[Route] = new ArrayBuffer[Route]) extends Object with Domain {

  type Position = (Int, Int)

  val Penalty = 4
    
  val distance = routes.foldLeft(0.0)((r,c) => r + c.distance)

  val overtime = routes.foldLeft(0.0)((r,c) => r + c.overtime)

  val overload = routes.foldLeft(0.0)((r,c) => r + c.overload)

  val load = routes.foldLeft(0.0)((r,c) => r + c.load)
  
  val serviceTime = routes.foldLeft(0.0)((r,c) => r + c.serviceTime)

  val fitness: Double = {
    // - ( this.distance )
    - ( this.distance + (this.overtime * Penalty) + (this.overload * Penalty) )
  }

  Schedule.initNearMatrix(jobs)
  
  // FIXME how the fuck does this work!!?!??!?
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
      newRoutes += new Route(depot, maxCapacity, maxRouteTime)
      newRoutes.last += CollectionUtil.selectRandomAndUnused(jobs, usedJobs)
    }

    while (usedJobs.size < jobs.size) {
      insertNearestRandomly(newRoutes, CollectionUtil.selectRandomAndUnused(jobs, usedJobs), 0.02)
      // var route = CollectionUtil.rand(newRoutes)
      // route += CollectionUtil.selectRandomAndUnused(jobs, usedJobs)
    }

    this.copy(routes = newRoutes)
  }

  def explore(eDistance: Double, i: Int): Domain = {
    var newRoutes = routes.map(_.copy())
    lns(newRoutes, eDistance)
    this.copy(routes = newRoutes)
  }
  
  override def toString = routes.foldLeft("")( _ + _.toString + "\n" )

  // def mate(domain: Domain) {
  //   p2 = domain.asInstanceOf[Schedule]
  //   
  //   var newRoutes = routes.map(_.copy())
  //   newRoutes += routes.map(_.copy)
  //   newRoutes.addAndRemove(p2.routes)
  //   
  //   newRoutes.repair
  //   
  //   this.copy(routes = newRoutes)
  // }

// -----------
// Private
// -----------

  private def lns(routes: Buffer[Route], eDistance: Double) = {
    
    // remove some random job
    var removed = new ArrayBuffer[Job]
    
    if (Random.nextDouble < 1.0) randomRemove else seqRemove
    
    // RANDOM REMOVE
    def randomRemove = {
      for (i <- 0 until Random.nextInt((jobs.size * 0.4).toInt)) {
        var route = CollectionUtil.rand(routes)
        if (route.isEmpty) {
          // empty? then replensish
          if (!removed.isEmpty)
            route += CollectionUtil.removeRandom(removed)
        } else {
          removed += route.removeRandom
        }
      }
    }

    // SEQ REMOVE
    def seqRemove = {
      var route = CollectionUtil.rand(routes)
      var start: Int = 0
      for (i <- 0 until Random.nextInt((jobs.size * 0.4).toInt)) {
        if (route.isEmpty) {
          // empty? then replensish and select another
          if (!removed.isEmpty) {
            route += CollectionUtil.removeRandom(removed)
            route = CollectionUtil.rand(routes)
          }
        } else {
          start = Random.nextInt(route.size)
          removed += route.remove(math.min(start, route.size - 1))
          if (Random.nextBoolean)
            route = CollectionUtil.rand(routes)
        }
      }
    }
    
    if (Random.nextDouble < 0.0) {
      insertNearest2(routes, removed, eDistance)
    } else {
      while (removed.nonEmpty) {
        var job = CollectionUtil.removeRandom(removed)
        insertNearest(routes, job, eDistance)
      }
    }
    
  }

  private def insertNearest2(routes: Buffer[Route], removed: Buffer[Job], eDistance: Double) = {
        
    def nearestJobIndex(job: Job, offset: Int) = {
      indexOf(routes, nearestJob(job, offset))
    }

    def getCost(job: Job, offset: Int): (Double, Position) = {
      val idxs = nearestJobIndex(job, offset)
    
      if (idxs._1 == -1)
        return null;
      
      val l = routes(idxs._1).insertionCost(idxs._2, job)
      val r = routes(idxs._1).insertionCost(idxs._2 + 1, job)
      
      if (l < r) (l, idxs) else (r, (idxs._1, idxs._2 + 1))
    }

    def findMinCostPos(job: Job, searchSize: Int) = {
      var minCost: (Double, Position) = null

      for (offset <- 0 until searchSize) {
        val cost = getCost(job, offset)
            
        if (minCost == null || (cost != null && cost._1 < minCost._1))
          minCost = cost
      }

      minCost
    }

    def calcCosts(removed: Buffer[Job], searchSize: Int) = {
      var costs = new ArrayBuffer[(Double, Position, Job)]
      
      for (job <- removed) {
        var cost = findMinCostPos(job, searchSize)
        // if (costs == null)
        //   cost = findMinCostPos(job, jobs.size-1)

        costs += Tuple3(cost._1, cost._2, job)
      }
      
      costs.sortWith { _._1 < _._1 }
    }
    
    // def updateCosts(route: Route, costs: ArrayBuffer[(Double, Position, Job)], searchSize: Int) = {
    //   val entries = routeMap(route)
    //   for (entry <- entries)
    //     costs(entry) = findMinCostPos(entry, searchSize)
    //   
    //   costs.sortsWith { _._1 < _._1 }
    // }

    val searchSize = jobs.size / 2

    while (removed.nonEmpty) {
      val c = calcCosts(removed, searchSize).head
      val bestIdxs = c._2
      routes(bestIdxs._1).insert(bestIdxs._2, c._3)
      removed.remove(removed.indexOf(c._3))
    }

  }

  private def insertNearest(routes: Buffer[Route], job: Job, eDistance: Double) = {
        
    def findNJ(offset: Int) = {
      indexOf(routes, nearestJob(job, offset))
    }
    
    def findNJWithCost(offset: Int): (Position, Double) = {
      val idxs = findNJ(offset)

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
    // val searchSize = (jobs.size * eDistance).toInt
    place(math.max((jobs.size * eDistance).toInt, 3))
        
    // Retry with larger expanded search if fails
    if (bestPos == null) {
      place(jobs.size)
      // println("OMG!!! IT HAPPENED")
    }

    val bestIdxs = bestPos._1
    routes(bestIdxs._1).insert(bestIdxs._2, job)
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
    
    routes(idxs._1).insertEitherSide(idxs._2, job)
    // routes(idxs._1).insert(idxs._2, job)
  }
  
  private def nearestJob(job: Job, offset: Int) = { Schedule.nearMatrix(job)(offset % Schedule.nearMatrix(job).size) }

  private def indexOf(routes: Buffer[Route], job: Job): (Int, Int) = { 
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
