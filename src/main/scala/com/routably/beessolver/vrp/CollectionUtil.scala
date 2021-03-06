package com.routably.beessolver.vrp;

import scala.util.Random
import scala.collection.mutable.Map
import scala.collection.mutable.Buffer

// TODO Is there a nicer way to mix this into the Seq class without a performance penality ?
object CollectionUtil {

  def selectRandomAndUnused[A](seq: Seq[A], used: Map[A, Boolean]): A = {
    var item = seq(Random.nextInt(seq.length))
    while (used.contains(item)) {
      item = seq(Random.nextInt(seq.size))
    }
    used(item) = true
    item
  }

  def rand[A](seq: Seq[A]) = seq(Random.nextInt(seq.size))
 
  def removeRandom[A](seq: Buffer[A]) = seq.remove(Random.nextInt(seq.length))
  
}