# 8690, 146, -2.662211251631561
# 11860, 211, -2.365901904088787
# 15850, 283, -2.3659019040878775

# 1180, 123, -265.1425164791308, -265.1425164791308

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
      puts "#{i}, #{(Time.now - @start).to_i}, #{fittest.fitness}, #{answer}"
    end
  end
  
end

monitor = TSP::Monitor.new

solver = BeesSolver::Solver.new(10_000_000, 0, 20, 10, 0.2, monitor, TSP::Route, TSP::Berlin52.cities)
# solver = BeesSolver::Solver.new(5000, 50, 5, 3, 5, 3, 0.2, monitor, TSP::Route, TSP::Simple.cities)
solution = solver.solve

