// Fitness: 12
// ---
// (0,0) 1,0  2,0  3,0
//  0,1            3,1
//  0,2            3,2
//  0,3  1,3  2,3  3,3
            
package com.visfleet.beessolver.vrp.data

import com.visfleet.beessolver.vrp.Location
import com.visfleet.beessolver.vrp.Job

object Simple {
  
  val MaxVehicles = 1
  val MaxCapacity = 100
  val MaxRouteTime = 999999
  val ServiceTime = 0
  val Depot = new Location(0, 0)
  
  private val data = List(
    ("A", 1, 0),
    ("B", 2, 0),
    ("C", 3, 0),
    ("D", 3, 1),
    ("E", 3, 2),
    ("F", 3, 3),
    ("G", 2, 3),
    ("H", 1, 3),
    ("I", 0, 3),
    ("J", 0, 2),
    ("K", 0, 1)
  )
  
  val Jobs = {
    val arr = new Array[Job](data.size)
    for (i <- 0 until arr.size) {
      var d = data(i)
      arr(i) = new Job(d._1.toString, new Location(d._2, d._3), 0, 0)
    }
    arr
  }

}
