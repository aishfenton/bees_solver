package com.routably.beessolver.vrp;

case class Job(id: String, location: Location, quantity: Double, serviceTime: Double) {
  override def toString = id
}
