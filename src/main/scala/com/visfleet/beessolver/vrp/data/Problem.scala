package com.visfleet.beessolver.vrp.data

import com.visfleet.beessolver.vrp.Location
import com.visfleet.beessolver.vrp.Job
import com.visfleet.beessolver.vrp.Schedule


trait Problem {
  
  def maxVehicles: Int
  def maxCapacity: Double
  def maxRouteTime: Double
  def depot: Location
  
  def jobs: Array[Job]
  
  def bestKnownAnswer: Double
  
}
