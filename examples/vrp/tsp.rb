$LOAD_PATH.unshift(File.expand_path(File.dirname(__FILE__) + '/../../lib')) unless $LOAD_PATH.include?(File.expand_path(File.dirname(__FILE__) + '/../../lib'))
require "bees_solver"

module TSP
       
  class Proxy < BeesSolver::Bee

    def initialize(args)
      @schedule = Schedule.new(*args)
    end
  
    def initialize_copy(obj)
      @schedule = obj.schedule.dup
    end

    def random_position
      @schedule.random_position
    end

    def fitness
      @schedule.fitness
    end
  
    def explore(distance, i)
      @schedule.explore(distance, i)
    end

  end

  class Monitor
    attr_accessor :fittest
  
    def starting_step(i)
      puts "Iteration #{i}"
    end
    
    def fittest=(fittest)
      puts "Fittest: #{fittest.fitness}"
    end
  end
  
end

monitor = TSP::Monitor.new

# load data
require './data/berlin52.rb'

solver = BeesSolver::Solver.new(0, 10, 1, 1, 10, 0, 0.2, monitor, TSP::Schedule, depot, size, jobs)
solution = solver.solve

