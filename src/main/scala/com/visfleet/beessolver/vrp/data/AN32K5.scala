package com.visfleet.beessolver.vrp.data

import com.visfleet.beessolver.vrp.Location
import com.visfleet.beessolver.vrp.Job

// Best 792.8774366539512
//      791.5698605489763
//      791.0789918671594
//      789.1679731450745
//      787.0818888551103
// 
//      R:30,16,1,12,: 72.0
//      R:14,28,11,4,23,2,3,6,: 98.0
//      R:26,7,13,17,19,31,21,: 98.0
//      R:27,24,: 44.0
//      R:20,5,25,10,29,15,22,9,8,18,: 98.0
//      ,-787.0818888551104)
// 
//   # NAME : A-n32-k5
//   # COMMENT : (Augerat et al, No of trucks: 5, Optimal value: 784)
//   # TYPE : CVRP
//   # DIMENSION : 32
//   # EDGE_WEIGHT_TYPE : EUC_2D 
//   # CAPACITY : 100
object AN32K5 extends Problem {
  
  def maxVehicles = 5
  def maxCapacity = 100
  def maxRouteTime = 999999
  def depot = new Location(82, 76)
  
  def jobs = ImportUtil.toJobs(List(
    (2, 96, 44, 19),
    (3, 50, 5, 21), 
    (4, 49, 8, 6), 
    (5, 13, 7, 19), 
    (6, 29, 89, 7), 
    (7, 58, 30, 12), 
    (8, 84, 39, 16), 
    (9, 14, 24, 6), 
    (10, 2, 39, 16), 
    (11, 3, 82, 8), 
    (12, 5, 10, 14), 
    (13, 98, 52, 21), 
    (14, 84, 25, 16), 
    (15, 61, 59, 3), 
    (16, 1, 65, 22), 
    (17, 88, 51, 18), 
    (18, 91, 2, 19), 
    (19, 19, 32, 1), 
    (20, 93, 3, 24), 
    (21, 50, 93, 8), 
    (22, 98, 14, 12), 
    (23, 5, 42, 4), 
    (24, 42, 9, 8), 
    (25, 61, 62, 24), 
    (26, 9, 97, 24), 
    (27, 80, 55, 2), 
    (28, 57, 69, 20), 
    (29, 23, 15, 15), 
    (30, 20, 70, 2), 
    (31, 85, 60, 14), 
    (32, 98, 5, 9)
  ))

  //  
  //  Route #2: 12 1 16 30
  //  Route #1: 21 31 19 17 13 7 26
  //  Route #3: 27 24
  //  Route #4: 29 18 8 9 22 15 10 25 5 20
  //  Route #5: 14 28 11 4 23 3 2 6
  // def solution = ImportUtil.toSolution(this, List(
  //   List(12, 1, 16, 30),
  //   List(21, 31, 19, 17, 13, 7, 26),
  //   List(27, 24),
  //   List(29, 18, 8, 9, 22, 15, 10, 25, 5, 20),
  //   List(14, 28, 11, 4, 23, 3, 2, 6)
  // ))

  def bestKnownAnswer = 787.0818888551103


}
