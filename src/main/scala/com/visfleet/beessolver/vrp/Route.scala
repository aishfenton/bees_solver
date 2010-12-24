package com.visfleet.beessolver.vrp;

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

class Route(depot: Location, maxRouteTime: Double, maxCapacity: Double) {

  var jobs = new ArrayBuffer[Job]

  def copy = {
    //       @visits = orig.visits.dup
  }
  
  def +=(job: Job): Route = { jobs += job; this }
  
  def distance = {
    var p = depot
    
    jobs.foldLeft(0.0) { (r, c) => 
      val d = p.distanceTo(c.location)
      p = c.location
      r + d
    }
  }

  def overtime = math.min(this.serviceTime - maxRouteTime, 0.0)

  def overload = math.min(this.load - maxCapacity, 0.0)
  
  def load = jobs.foldLeft(0.0)((r,c) => r + c.quantity)

  def serviceTime = jobs.foldLeft(0.0)((r,c) => r + c.serviceTime)
 
  override def toString = jobs.toString
  
}


  //     include Enumerable
  // 
  //     def delete_rand
  //       @visits.delete_rand
  //     end
  //           
  //     def <<(customer)
  //       raise "Nil city passed in" if customer == nil
  // 
  //       @visits << customer
  //       self
  //     end
  //     
  //     def [](index)
  //       @visits[index]
  //     end
  // 
  //     def []=(index, customer)
  //       raise "Index outside of bounds" if index < 0 || index >= size
  //       raise "Nil customer passed in" if customer == nil
  //     
  //       @visits[index] = customer
  //     end
  //   
  //     def index(customer)
  //       @visits.index(customer)
  //     end
  //     
  //     def size
  //       @visits.size
  //     end
  // 
  //     def insert(index, *values)
  //       raise Exception.new('Inserted past end of visits array:' + index.to_s) if index > @visits.size
  //       @visits.insert(index, *values)
  //     end
  // 
  //   end
  // 
  // end
  