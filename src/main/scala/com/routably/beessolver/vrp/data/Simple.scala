// // Fitness: 12
// // ---
// // (0,0) 1,0  2,0  3,0
// //  0,1            3,1
// //  0,2            3,2
// //  0,3  1,3  2,3  3,3
//             
// package com.routably.beessolver.vrp.data
// 
// import com.routably.beessolver.vrp.Location
// import com.routably.beessolver.vrp.Job
// 
// object Simple extends Problem {
//   
//   def maxVehicles = 1
//   def maxCapacity = 100
//   def maxRouteTime = 999999
//   def depot = new Location(0, 0)
//   
//   def jobs = ImportUtil.toJobs(List(
//     (2,  1, 0, 0),
//     (3,  2, 0, 0),
//     (4,  3, 0, 0),
//     (5,  3, 1, 0),
//     (6,  3, 2, 0),
//     (7,  3, 3, 0),
//     (8,  2, 3, 0),
//     (9,  1, 3, 0),
//     (10, 0, 3, 0),
//     (11, 0, 2, 0),
//     (12, 0, 1, 0)
//   ))
// 
//   def solution = 12
// 
//   def solutionFull = ImportUtil.toSolution(this, List(
//     List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)
//   ))
//   
// 
// }
