package com.visfleet.beessolver.vrp;

import scala.util.Random
import scala.collection.mutable.Map

// TODO Is there a nicer way to mix this into the Seq class without a performance penality ?

object CollectionUtil {

  def selectRandomAndUnused[A](seq: IndexedSeq[A], used: Map[A, Boolean], rand: Random): A = {
    var item = seq(rand.nextInt(seq.length))
    while (used.contains(item)) {
      item = seq(rand.nextInt(seq.length))
    }
    used(item) = true
    item
  }
  
}