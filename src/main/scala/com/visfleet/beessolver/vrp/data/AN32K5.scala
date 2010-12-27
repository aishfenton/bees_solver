package com.visfleet.beessolver.vrp.data

import com.visfleet.beessolver.vrp.Location
import com.visfleet.beessolver.vrp.Job

// Best 792.8774366539512
//      791.5698605489763
//      791.0789918671594
//      789.1679731450745
//      787.0818888551103
// 
//      R:30,16,1,12,
//      R:26,7,13,17,19,31,21,
//      R:27,24
//      R:20,5,25,10,29,15,22,9,8,18,
//      R:6,3,2,23,4,11,28,14,
// 
//      Route #2: 12 1 16 30
//      Route #1: 21 31 19 17 13 7 26
//      Route #3: 27 24
//      Route #4: 29 18 8 9 22 15 10 25 5 20
//      Route #5: 14 28 11 4 23 3 2 6
// 
//   # NAME : A-n32-k5
//   # COMMENT : (Augerat et al, No of trucks: 5, Optimal value: 784)
//   # TYPE : CVRP
//   # DIMENSION : 32
//   # EDGE_WEIGHT_TYPE : EUC_2D 
//   # CAPACITY : 100
object AN32K5 {
  
  val MaxVehicles = 5
  val MaxCapacity = 100
  val MaxRouteTime = 999999
  val ServiceTime = 0
  val Depot = new Location(82, 76)
  
  private val data = List(
    (1, 96, 44, 19),                    
    (2, 50, 5, 21), 
    (3, 49, 8, 6), 
    (4, 13, 7, 19), 
    (5, 29, 89, 7), 
    (6, 58, 30, 12), 
    (7, 84, 39, 16), 
    (8, 14, 24, 6), 
    (9, 2, 39, 16), 
    (10, 3, 82, 8), 
    (11, 5, 10, 14), 
    (12, 98, 52, 21), 
    (13, 84, 25, 16), 
    (14, 61, 59, 3), 
    (15, 1, 65, 22), 
    (16, 88, 51, 18), 
    (17, 91, 2, 19), 
    (18, 19, 32, 1), 
    (19, 93, 3, 24), 
    (20, 50, 93, 8), 
    (21, 98, 14, 12), 
    (22, 5, 42, 4), 
    (23, 42, 9, 8), 
    (24, 61, 62, 24), 
    (25, 9, 97, 24), 
    (26, 80, 55, 2), 
    (27, 57, 69, 20), 
    (28, 23, 15, 15), 
    (29, 20, 70, 2), 
    (30, 85, 60, 14), 
    (31, 98, 5, 9)
  )
  
  val Jobs = {
    val arr = new Array[Job](data.size)
    for (i <- 0 until arr.size) {
      var d = data(i)
      arr(i) = new Job(d._1.toString, new Location(d._2, d._3), d._4, 0)
    }
    arr
  }

}
