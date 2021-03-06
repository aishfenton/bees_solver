package com.routably.beessolver.vrp.monitor

import com.routably.beessolver.Site
import com.routably.beessolver.Domain
import com.routably.beessolver.Monitor
import com.routably.beessolver.vrp.Schedule
import com.routably.beessolver.vrp.Runner
import com.routably.beessolver.vrp.data.Problem
import scala.util.matching.Regex
import java.io.FileWriter

class DatOut(directory: String, problem: Problem) extends Monitor {
  
  val format = "%d %d %.10f %.4f %n"
  var out: FileWriter = null

  def starting = {
    // var sdf = new java.text.SimpleDateFormat("yyyyMMddHHmmss")
      // + "_" + sdf.format(new java.util.Date())
    new java.io.File(directory).mkdir
    val Clazz = """[\w\.]+\.(\w+)\$""".r
    val Clazz(filename) = problem.getClass.getName

    out = new java.io.FileWriter(directory + "/" + filename)
  }

  def bestSites(iteration: Int, duration: Long, bestSoFar: Domain, sites: Seq[Site]): Unit = {      
    val schedule = bestSoFar.asInstanceOf[Schedule]
    val percent = if (schedule.isFeasible) problem.bestKnownAnswer / schedule.distance else 0.0
    
    val line = format.format(duration, iteration, schedule.distance, percent)
    out.write(line)
    print(line)
  }
  
  def finished = {
    out.close
  }
  
}