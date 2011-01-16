package com.visfleet.beessolver

import scala.collection.mutable.HashMap
import scala.collection.mutable.LinkedHashMap
import java.util.Date
import com.visfleet.beessolver.vrp._
import com.visfleet.beessolver.vrp.data._
import com.visfleet.beessolver.monitor._

class World(iterations: Int, maxTime: Long, noSites: Int, noWorkerBees: Int, noMoves: Int, exploreDistance: Double, domainFunc: => Domain, monitor: Monitor) {

  var sites = new Array[Site](noSites)
  
  var startTime: Date = null
  var alreadyTicked = -1l

  var answer: Domain = null

  // FIXME. Yuky sideeffects everywhere!!
  def reset = {
    Schedule.clear
    World.clear
  }

  def start = {
    this.reset
    
    startTime = new Date()
    this.seed

    // initialize answer with random site (gets updated once we've completed 1 iteration)
    answer = sites.head.bestBee.theDomain
        
    monitor.starting

    def loop: Unit = for (i <- 0 to iterations) {
      val t = (new Date().getTime - startTime.getTime) / 1000
      if (t > maxTime)
        return

      doMonitor(i, t)
      step(i)
    }
    loop
    
    monitor.finished
  }

  def step(i: Int) = {
    
    World.clear

    val threads = for (i <- 0 until sites.size)
      yield new Thread() {
        override def run() {
          sites(i) = sites(i).explore(noMoves, exploreDistance, i)
        }
      }
    
    threads.foreach(t => t.start())
    threads.foreach(t => t.join())
    
    sites = sites.sortWith { _.fitness > _.fitness }

    // // Killing off worse site
    if (i % 3 == 0 && sites.size > 1)
      sites = sites.slice(0, (sites.size * 0.99).toInt).toArray
    
    // replacing worse site with bread one
    if (i % 1 == 0) {
      sites(sites.size - 1) = sites(sites.size - 1).mate(sites.head)
    }
    
    // // True Bees Algorithm
    // if (i % 1 == 0) {
    //   for (j <- (sites.size / 5) until sites.size)
    //     sites(j) = Site.randomPosition(noWorkerBees, domainFunc)
    // }

    if (answer.fitness < sites.head.fitness && sites.head.isFeasible)
      answer = sites.head.bestBee.theDomain
  }
    
  private def doMonitor(i: Int, t: Long): Unit = {
    val quanta = 5
    var interval = (t / quanta).toInt
    if (interval == alreadyTicked) {
      return
    }
    else {
      alreadyTicked = interval
      monitor.bestSites(i, interval * quanta, answer, sites.sortWith { _.fitness > _.fitness })
    }    
  }
  
  private def seed() = {
    for (i <- 0 until noSites) {
      sites(i) = Site.randomPosition(noWorkerBees, domainFunc)
    }    
  } 
  
}

object World {
  
  private val TabuThreshold = 1
  
  private val usedPositions = new LinkedHashMap[String, Boolean]

  private val tabu = new HashMap[String, Int]

  def isTabu(d: Domain) = tabu.contains(d.positionHash) // && tabu(d.positionHash) > TabuThreshold

  def isUsed(d: Domain) = usedPositions.contains(d.positionHash)

  def makeTabu(d: Domain) = tabu(d.positionHash) = 1

  def at(d: Domain) = { 
    usedPositions(d.positionHash) = true;
    // if (!tabu.contains(d.positionHash))
    //   tabu(d.positionHash) = 0    
    // tabu(d.positionHash) += 1
    // 
    // if (tabu.size > 100000)
    //   tabu.remove(tabu.head._1)
  }

  def clear = usedPositions.clear
  
  def usedSize = usedPositions.size
  def tabuSize = tabu.filter( (h) => h._2 > TabuThreshold ).size
  
  override def toString = usedPositions.toString
  
}
