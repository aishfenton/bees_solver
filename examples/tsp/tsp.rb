$LOAD_PATH.unshift(File.expand_path(File.dirname(__FILE__) + '/../../lib')) unless $LOAD_PATH.include?(File.expand_path(File.dirname(__FILE__) + '/../../lib'))
require "bees_solver"

require "./route"
require "./city"
require "./location"
require "./ext"
require "./data"

module TSP
  
  class Monitor
    def starting_step(i, fittest, answer)
      @start ||= Time.now
      puts "#{i}, #{Time.now - @start}, #{fittest.fitness}, #{answer}" if i % 10 == 0
    end
  end
  
end

monitor = TSP::Monitor.new

solver = BeesSolver::Solver.new(100_000, 70, 0, 5, 10, 0, 0.5, monitor, TSP::Route, TSP::Berlin52.cities)
# solver = BeesSolver::Solver.new(5000, 50, 5, 3, 5, 3, 0.2, monitor, TSP::Route, TSP::Simple.cities)
solution = solver.solve

