package com.routably.beessolver.vrp.data

import com.routably.beessolver.vrp.Location
import com.routably.beessolver.vrp.Job
import com.routably.beessolver.vrp.Schedule


trait Problem {
  
  def maxVehicles: Int
  def maxCapacity: Double
  def maxRouteTime: Double
  def depot: Location
  
  def jobs: Array[Job]
  
  def bestKnownAnswer: Double
  
}
