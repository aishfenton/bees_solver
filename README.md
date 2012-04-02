# Bees VRP Solver
By Aish Fenton

An algorithm for solving the Vehicle Routing Problem (VRP) using a bees inspired meta-heuristic. See [here](http://www.scribd.com/fullscreen/87621958?access_key=key-xpfqkadpu870u9xkebt) for an in-depth description of the algorithm and its results. 

	 o
		    
		   //       .'''.
		 O(_))) ...'     `.
			\\             `.    .'''
		                     `..'


## Install

Prerequisites are:

  - Scala 2.9.x
  - Buildr (http://buildr.apache.org/)

## Usage
Can be run across any of the canonically benchmark problems found in /src/main/scala/com/routably/beessolver/vrp/data. Pass the problem name as an argument as such: 
	
	buildr BeesSolver:run[P01E51K05]

## Copyright

Copyright (c) 2010 Aish Fenton. See LICENSE for details.
