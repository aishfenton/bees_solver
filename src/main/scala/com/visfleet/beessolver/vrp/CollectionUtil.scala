package com.visfleet.beessolver.vrp;

import scala.util.Random
import scala.collection.mutable.Map
import scala.collection.mutable.Buffer

// TODO Is there a nicer way to mix this into the Seq class without a performance penality ?

object CollectionUtil {

  def selectRandomAndUnused[A](seq: IndexedSeq[A], used: Map[A, Boolean]): A = {
    var item = seq(Random.nextInt(seq.length))
    while (used.contains(item)) {
      item = seq(Random.nextInt(seq.length))
    }
    used(item) = true
    item
  }

  def rand[A](seq: IndexedSeq[A]) = seq(Random.nextInt(seq.length))
 
  def removeRandom[A](seq: Buffer[A]) = seq.remove(Random.nextInt(seq.length))
  
}