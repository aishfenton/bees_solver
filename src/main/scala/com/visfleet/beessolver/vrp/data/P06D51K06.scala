package com.visfleet.beessolver.vrp.data

import com.visfleet.beessolver.vrp.Location
import com.visfleet.beessolver.vrp.Job

// Best 
// 750 34 -555.43023650 555.43023650 0.00 0.00 7 
// 
// NAME : D51K6
// COMMENT : (Christophides and Eilon, Min no of trucks: 5)
// TYPE : CVRP
// DIMENSION : 51
// EDGE_WEIGHT_TYPE : EUC_2D
// CAPACITY : 160
object P06D51K06 extends Problem {
  
  def maxVehicles = 6
  def maxCapacity = 160
  def maxRouteTime = 200
  def serviceTime = 10
  def depot = new Location(30, 40)
  
  def jobs = ImportUtil.toJobs(List(
    (2, 37, 52, 7),
    (3, 49, 49, 30),
    (4, 52, 64, 16),
    (5, 20, 26, 9),
    (6, 40, 30, 21),
    (7, 21, 47, 15),
    (8, 17, 63, 19),
    (9, 31, 62, 23),
    (10, 52, 33, 11),
    (11, 51, 21, 5),
    (12, 42, 41, 19),
    (13, 31, 32, 29),
    (14, 5, 25, 23),
    (15, 12, 42, 21),
    (16, 36, 16, 10),
    (17, 52, 41, 15),
    (18, 27, 23, 3),
    (19, 17, 33, 41),
    (20, 13, 13, 9),
    (21, 57, 58, 28),
    (22, 62, 42, 8),
    (23, 42, 57, 8),
    (24, 16, 57, 16),
    (25, 8, 52, 10),
    (26, 7, 38, 28),
    (27, 27, 68, 7),
    (28, 30, 48, 15),
    (29, 43, 67, 14),
    (30, 58, 48, 6),
    (31, 58, 27, 19),
    (32, 37, 69, 11),
    (33, 38, 46, 12),
    (34, 46, 10, 23),
    (35, 61, 33, 26),
    (36, 62, 63, 17),
    (37, 63, 69, 6),
    (38, 32, 22, 9),
    (39, 45, 35, 15),
    (40, 59, 15, 14),
    (41, 5, 6, 7),
    (42, 10, 17, 27),
    (43, 21, 10, 13),
    (44, 5, 64, 11),
    (45, 30, 15, 16),
    (46, 39, 10, 10),
    (47, 32, 39, 5),
    (48, 25, 32, 25),
    (49, 25, 55, 17),
    (50, 48, 28, 18),
    (51, 56, 37, 10)
  ), serviceTime)

  def bestKnownAnswer = 555.43

}
