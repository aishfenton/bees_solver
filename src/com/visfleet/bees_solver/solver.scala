package com.visfleet.bee_solver

class Solver(iterations: Int, no_sites: Int, no_worker_bees: Int, no_moves :Int, explore_distance :Double, monitor :Monitor, domain_proxy :Domain_Proxy, *args) {
  private var sites: List[Site]
  private var answer: Int
  
  def solve = {
    seed
    
    for (i <- 1 to 10)
      step(i)
    
    answer
  }

  def step(i:Int) = println(i)
  
  
}