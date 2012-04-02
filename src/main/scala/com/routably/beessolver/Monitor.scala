package com.routably.beessolver

trait Monitor {
  
  def starting
  def finished
  def bestSites(iteration: Int, duration: Long, bestSoFar: Domain, sites: Seq[Site])
  
}