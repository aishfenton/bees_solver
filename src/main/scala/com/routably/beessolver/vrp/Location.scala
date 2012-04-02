package com.routably.beessolver.vrp;

case class Location(x: Double, y: Double) {
  
  def distanceTo(toLoc: Location): Double = {
    val dx = this.x - toLoc.x
    val dy = this.y - toLoc.y
    math.sqrt(math.pow(dx, 2) + math.pow(dy, 2))
  }
  
  def manhattanDistanceTo(toLoc: Location): Double = {
    val dx = this.x - toLoc.x
    val dy = this.y - toLoc.y
    math.pow(dx, 2) + math.pow(dy, 2)
  }
    
}
