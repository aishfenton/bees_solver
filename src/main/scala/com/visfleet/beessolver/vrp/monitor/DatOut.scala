package com.visfleet.beessolver.vrp.monitor

import com.visfleet.beessolver.Site
import com.visfleet.beessolver.Domain
import com.visfleet.beessolver.Monitor
import com.visfleet.beessolver.vrp.Schedule
import com.visfleet.beessolver.vrp.Runner
import com.visfleet.beessolver.vrp.data.Problem
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
    val line = format.format(duration, iteration, schedule.distance, problem.bestKnownAnswer / schedule.distance)
    out.write(line)
    print(line)
  }
  
  def finished = {
    out.close
  }
  
}