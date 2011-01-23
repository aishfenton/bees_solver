package com.visfleet.beessolver.vrp;

import scala.collection.mutable.Buffer
import scala.collection.mutable.HashMap
import scala.collection.mutable.ArrayBuffer
import scala.util.Random

class Route(depot: Location, maxCapacity: Double, maxRouteTime: Double, jobs: IndexedSeq[Job] = Vector.empty) {

  // 
  // Immutable functions
  // 

  val distance = {
    var p = depot
    
    var r = jobs.foldLeft(0.0) { (r, c) => 
      val d = p.distanceTo(c.location)
      p = c.location
      r + d
    }
    // and back again
    r + p.distanceTo(depot)
  }
  val load = jobs.foldLeft(0.0)((r,c) => r + c.quantity)
  val serviceTime = jobs.foldLeft(0.0)((r,c) => r + c.serviceTime) + this.distance
  val overtime = math.max(this.serviceTime - maxRouteTime, 0.0)
  val overload = math.max(this.load - maxCapacity, 0.0)


  val indexMap = jobs.zipWithIndex.foldLeft(new HashMap[Job, Int]) { (r, c) => r(c._1) = c._2; r }
  
  val size = jobs.size
  
  val isEmpty = jobs.isEmpty

  def indexOf(job: Job) = indexMap.getOrElse(job, -1)

  def apply(idx: Int) = jobs.apply(idx)

  def copy(depot: Location = this.depot,
           maxCapacity: Double = this.maxCapacity, 
           maxRouteTime: Double = this.maxRouteTime, 
           jobs: IndexedSeq[Job] = this.jobs): Route = {
    new Route(depot, maxCapacity, maxRouteTime, jobs)
  }

  override def toString = jobs.foldLeft("R:")(_ + _.toString + ",") + "(" + distance + ") (" + load + ") (" + serviceTime + ")"

  // def exchangeCost(idx: Int, job: Job) = {
  //   var a: Location = null
  //   var b: Location = null
  //   if (idx == 0) {
  //     a = depot
  //     b = jobs(idx).location
  //   } else if (idx == jobs.size) {
  //     a = jobs(idx - 1).location
  //     b = depot
  //   } else {
  //     a = jobs(idx - 1).location
  //     b = jobs(idx).location
  //   }
  //   
  //   val existingDist = a.distanceTo(tj.location) + b.distanceTo(tj.location)
  //   val newDist = a.distanceTo(job.location) + c.distanceTo(job.location)
  //   
  //   val w = (this.load - tj.quantity + job.quantity) - this.load
  //   val t = (this.serviceTime - tj.sericeTime + job.serviceTime + newDist)
  //   
  //   newDist - existingDist + w + t
  // }

  def insertionCost(idx: Int, job: Job) = { 
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

  // 
  // Mutable functions
  // 

  def u(changeFunc: => IndexedSeq[Job]): Route = {
    this.copy(jobs = changeFunc)
  }

  def append(job: Job) = u(jobs :+ job)
  def update(idx: Int, job: Job) = u(jobs.updated(idx, job))
  def insert(idx: Int, job: Job) = u((jobs.slice(0, idx) :+ job) ++ jobs.slice(idx, jobs.size) )
  def insertEitherSide(idx: Int, job: Job) = if (Random.nextBoolean) this.insert(idx, job) else this.insert(idx+1, job)
  
  def remove(idx: Int) = {
    val nj = jobs.slice(0, idx) ++ jobs.slice(idx + 1, jobs.size)
    (this.copy(jobs = nj), jobs(idx))
  }

}
