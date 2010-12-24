# 8690, 146, -2.662211251631561
# 11860, 211, -2.365901904088787
# 15850, 283, -2.3659019040878775

# 1180, 123, -265.1425164791308, -265.1425164791308

$LOAD_PATH.unshift(File.expand_path(File.dirname(__FILE__) + '/../../lib')) unless $LOAD_PATH.include?(File.expand_path(File.dirname(__FILE__) + '/../../lib'))
require "bees_solver"

require "./ext"
require "./location"
require "./customer"
require "./route"
require "./schedule"
require "./data"

module TSP
  
  class Monitor
    def starting_step(i, fittest, answer)
      @start ||= Time.now
      puts "#{i}, #{(Time.now - @start).to_i}, #{fittest.fitness}, #{fittest.domain_proxy.distance}, #{fittest.domain_proxy.overload}, #{fittest.domain_proxy.overtime}, #{answer}"
    end
  end
  
end

monitor = TSP::Monitor.new

# solver = BeesSolver::Solver.new(10_000_000, 1, 1, 1, 0.15, monitor, VRP::Schedule, 5, VRP::VRPNC1::DEPOT, VRP::VRPNC1::VEHICLE_CAPACITY, VRP::VRPNC1::MAX_ROUTE_TIME, VRP::VRPNC1.customers)
solver = BeesSolver::Solver.new(10_000_000, 5, 3, 5, 0.15, monitor, VRP::Schedule, 18, VRP::VRPNC10::DEPOT, VRP::VRPNC10::VEHICLE_CAPACITY, VRP::VRPNC10::MAX_ROUTE_TIME, VRP::VRPNC10.customers)
# solver = BeesSolver::Solver.new(10_000_000, 5, 20, 10, 0.3, monitor, VRP::Schedule, 5, VRP::A_N32_K5::DEPOT, VRP::A_N32_K5::VEHICLE_CAPACITY, VRP::A_N32_K5::MAX_ROUTE_TIME, VRP::A_N32_K5.customers)
solution = solver.solve

