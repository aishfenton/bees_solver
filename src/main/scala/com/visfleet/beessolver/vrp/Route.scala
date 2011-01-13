package com.visfleet.beessolver.vrp;

import scala.collection.mutable.Buffer
import scala.collection.mutable.ArrayBuffer
import scala.util.Random

class Route(depot: Location, maxCapacity: Double, maxRouteTime: Double, var jobs: Buffer[Job] = new ArrayBuffer[Job]) {

  def copy(depot: Location = this.depot,
           maxCapacity: Double = this.maxCapacity, 
           maxRouteTime: Double = this.maxRouteTime, 
           jobs: Buffer[Job] = this.jobs.clone): Route = {
    new Route(depot, maxCapacity, maxRouteTime, jobs)
  }

  def +=(job: Job): Route = { jobs += job; this }
  
  def distance = {
    var p = depot
    
    var r = jobs.foldLeft(0.0) { (r, c) => 
      val d = p.distanceTo(c.location)
      p = c.location
      r + d
    }
    // and back again
    r + p.distanceTo(depot)
  }

  def overtime = math.max(this.serviceTime - maxRouteTime, 0.0)

  def overload = math.max(this.load - maxCapacity, 0.0)
  
  def load = jobs.foldLeft(0.0)((r,c) => r + c.quantity)

  def serviceTime = jobs.foldLeft(0.0)((r,c) => r + c.serviceTime) + distance
 
  override def toString = jobs.foldLeft("R:")(_ + _.toString + ",") + "(" + distance + ") (" + load + ") (" + serviceTime + ")"
  
  def removeRandom = CollectionUtil.removeRandom(jobs)
  
  def indexOf(job: Job) = jobs.indexOf(job)

  def insert(idx: Int, job: Job) = jobs.insert(idx, job)

  def insertEitherSide(idx: Int, job: Job) = {
    if (Random.nextBoolean) {
      jobs.insert(idx, job)
    } else {
      jobs.insert(idx + 1, job)
    }
  }

  // distance increase + weight increase 
  def insertionCost(idx: Int, job: Job) = {
    // Random.nextDouble
    var a: Location = null
    var b: Location = null
    if (idx == 0) {
      a = depot
      b = jobs(idx).location
    } else if (idx == jobs.size) {
      a = jobs(idx - 1).location
      b = depot
    } else {
      a = jobs(idx - 1).location
      b = jobs(idx).location
    }
    
    val d = a.distanceTo(job.location) + b.distanceTo(job.location) - a.distanceTo(b)
    val w = math.max((this.load + job.quantity - maxCapacity), 0.0) * 4
    val t = math.max((this.serviceTime + job.serviceTime + d - maxRouteTime), 0.0) * 4
    
    d + w + t
  }

  def remove(idx: Int) = jobs.remove(idx)

  def apply(idx: Int) = jobs.apply(idx)

  def update(idx: Int, job: Job) = jobs.update(idx, job)

  def size = jobs.size
  
  def isEmpty = jobs.isEmpty
    
}
