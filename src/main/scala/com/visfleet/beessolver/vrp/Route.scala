package com.visfleet.beessolver.vrp;

import scala.collection.mutable.ArrayBuffer

class Route(depot: Location, maxRouteTime: Double, maxCapacity: Double, var jobs:ArrayBuffer[Job] = new ArrayBuffer[Job]) {

  def copy(depot: Location = this.depot,
           maxRouteTime: Double = this.maxRouteTime, 
           maxCapacity: Double = this.maxCapacity, 
           jobs: ArrayBuffer[Job] = this.jobs.clone): Route = {
    new Route(depot, maxRouteTime, maxCapacity, jobs)
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

  def serviceTime = jobs.foldLeft(0.0)((r,c) => r + c.serviceTime)
 
  override def toString = jobs.foldLeft("R:")(_ + _.toString + ",")
  
  def removeRandom = CollectionUtil.removeRandom(jobs)
  
  def indexOf(job: Job) = jobs.indexOf(job)

  def insert(idx: Int, job: Job) = jobs.insert(idx, job)

  def apply(idx: Int) = jobs.apply(idx)

  def update(idx: Int, job: Job) = jobs.update(idx, job)

  def size = jobs.size
  
  def isEmpty = jobs.isEmpty
    
}
