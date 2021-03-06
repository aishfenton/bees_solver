package com.routably.beessolver.vrp.monitor

import com.routably.beessolver.World
import com.routably.beessolver.Site
import com.routably.beessolver.Domain
import com.routably.beessolver.Monitor
import com.routably.beessolver.vrp._

class Debug extends Monitor {
  
  val header = "%d %d %.8f --------------------------- %n"
  val detail = "\t %.8f %.8f %.2f %.2f %.2f %d %n"

  def starting = {
    println("========= STARTING ========")
  }
  
  def finished = {}

  def bestSites(iteration: Int, duration: Long, bestSoFar: Domain, sites: Seq[Site]) = {
    
    val bestBees = sites.map { _.bestBee.theDomain }.sortWith { _.fitness > _.fitness }
    
    print(header.format(duration, iteration, bestSoFar.fitness))

    sites.foreach ( (site) => {
      val sch = site.bestBee.theDomain.asInstanceOf[Schedule]
      print(detail.format(sch.fitness, sch.distance, sch.overload, sch.overtime, site.avgFitness, site.theAge)) 
    })
    
    if (duration % 30 == 0) {
      println("========= BEST SO FAR ============")
      println(sites.first.bestBee)
    }
    
  }

}