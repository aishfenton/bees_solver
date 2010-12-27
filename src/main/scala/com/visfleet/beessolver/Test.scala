package com.visfleet.beessolver

import com.visfleet.beessolver.vrp._
import com.visfleet.beessolver.vrp.data.AN32K5

import scala.collection.mutable.HashMap
import scala.collection.mutable.ArrayBuffer

//      784.?
//      787.8082774366646
//      Route #2: 12 1 16 30
//      Route #1: 21 31 19 17 13 7 26
//      Route #3: 27 24
//      Route #4: 29 18 8 9 22 15 10 25 5 20
//      Route #5: 14 28 11 4 23 3 2 6

object Test {

  def main(args: Array[String]) {

    var depot = AN32K5.Depot
    var jobs =  AN32K5.Jobs.foldLeft(new HashMap[String, Job])( (r, c) => { r(c.id) = c; r } )

    var route2 = new Route(depot, 999999, 100, ArrayBuffer( jobs("12"), jobs("1"), jobs("16"), jobs("30") ))
    var route1 = new Route(depot, 999999, 100, ArrayBuffer( jobs("21"), jobs("31"), jobs("19"), jobs("17"), jobs("13"), jobs("7"), jobs("26") ))
    var route3 = new Route(depot, 999999, 100, ArrayBuffer( jobs("27"), jobs("24") ))
    var route4 = new Route(depot, 999999, 100, ArrayBuffer( jobs("29"), jobs("18"), jobs("8"), jobs("9"), jobs("22"), jobs("15"), jobs("10"), jobs("25"), jobs("5"), jobs("20") ))
    var route5 = new Route(depot, 999999, 100, ArrayBuffer( jobs("14"), jobs("28"), jobs("11"), jobs("4"), jobs("23"), jobs("3"), jobs("2"), jobs("6") ))

    println(route2.distance, route2.overload)
    println(route1.distance, route1.overload)
    println(route3.distance, route3.overload)
    println(route4.distance, route4.overload)
    println(route5.distance, route5.overload)

    println(route1.distance + route2.distance + route3.distance + route4.distance + route5.distance)

  }
  
}
