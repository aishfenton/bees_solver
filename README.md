# Bees VRP Solver
By Aish Fenton

	 o
		    
		   //       .'''.
		 O(_))) ...'     `.
			\\             `.    .'''
		                     `..'

An algorithm for solving the Vehicle Routing Problem (VRP) using a bees inspired meta-heuristic. See [here](http://www.scribd.com/fullscreen/87621958?access_key=key-xpfqkadpu870u9xkebt) for an in-depth description of the algorithm and its results. 


## Install

Prerequisites are:

  - Scala 2.9.x
  - Buildr (http://buildr.apache.org/)

## Usage
Can be run across any of the canonic benchmark problems found in /src/main/scala/com/routably/beessolver/vrp/data. Pass the problem name as an argument to BeeSolver:run task. For example: 
	
	buildr BeesSolver:run[P01E51K05]

## Copyright

Copyright (c) 2010 Aish Fenton. See LICENSE for details.
