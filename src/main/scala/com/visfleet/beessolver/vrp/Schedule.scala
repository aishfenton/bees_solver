package com.visfleet.beessolver.vrp;

import com.visfleet.beessolver.Domain
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.HashMap
import scala.util.Random

class Schedule(maxVehicles: Int, depot: Location, maxCapacity: Double, 
               maxRouteTime: Double, jobs: Array[Job], routes: ArrayBuffer[Route] = new ArrayBuffer[Route]) extends Object with Domain {
    
  val rand = new Random

  val distance = routes.foldLeft(0.0)((r,c) => r + c.distance)

  val overtime = routes.foldLeft(0.0)((r,c) => r + c.overtime)

  val overload = routes.foldLeft(0.0)((r,c) => r + c.overload)

  val positionHash = this.fitness.to_s.hashCode

  val fitness: Double = {
    - ( this.distance )
    // - ( this.distance + (this.overtime * 2) + (this.overload * 2) )
  }

  // init_near_matrix
  
  def copy(maxVehicles: Int = this.maxVehicles, depot: Location = this.depot, 
           maxCapacity: Double = this.maxCapacity, maxRouteTime: Double = this.maxRouteTime, 
           jobs: Array[Job] = this.jobs, routes: ArrayBuffer[Route] = this.routes): Schedule = {
    new Schedule(maxVehicles, depot, maxCapacity, maxRouteTime, jobs, routes)
  }

  def randomPosition: Domain = {
    val rand = new Random
    var usedJobs = new HashMap[Job, Boolean]
    var newRoutes = new ArrayBuffer[Route]
    
    for (i <- 0 to maxVehicles) {
      newRoutes += new Route(depot, maxRouteTime, maxCapacity)
      newRoutes.last += CollectionUtil.selectRandomAndUnused(jobs, usedJobs, rand)
    }

    var route: Route = null
    while (usedJobs.size < jobs.length) {
      route = newRoutes(rand.nextInt(newRoutes.size))
      route += CollectionUtil.selectRandomAndUnused(jobs, usedJobs, rand)
    }

    this.copy(routes = newRoutes)
  }

  def explore(distance: Double, i: Int): Domain = {
    this
  }



}



//     def random_position
//       tbd_customers = @customers.dup
//       @routes.clear
// 
//       # create and seed routes
//       @max_vehicles.times do
//         @routes << (route = Route.new(@depot, @max_route_time, @capacity))
//         route << tbd_customers.delete_rand
//       end
// 
//       @count = 0
//       until tbd_customers.empty?
//         customer = tbd_customers.delete_rand
//         insert_nearest(customer, 0)
//         # @routes[@count % @routes.size] << old_customers.delete_rand
//         # @count += 1
//       end
//     end
//   
//     def explore(distance, i)
//       removed = []
//       [rand(@customers.size * distance), 4].max.times { removed << @routes.rand.delete_rand }
//       
//       removed.compact!
//       
//       # until removed.empty?
//       #   @cities.insert_rand(removed.delete_rand)
//       # end
//       
//       until removed.empty?
//         customer = removed.delete_rand
//         insert_nearest(customer, distance)
//       end
//     end
//     
//     ##
//     ## Other methods
//     ## 
// 
//     def insert_nearest(customer, distance)
//       offset = rand(@customers.size * distance)
//       n_customer = @@near_matrix[customer][offset]
//       
//       r_idx, v_idx = self.index(n_customer)
//       
//       while r_idx.nil?
//         offset += 1
//         n_customer = @@near_matrix[customer][offset % @@near_matrix[customer].size]
//         r_idx, v_idx = self.index(n_customer)
//       end
//       
//       @routes[r_idx].insert(v_idx, customer)
//     end
//       
//     def index(customer)
//       r_idx = nil
//       v_idx = nil
// 
//       @routes.each_with_index do |route, i|
//         v_idx = route.index(customer)
//         if v_idx
//           r_idx = i
//           break
//         end
//       end
// 
//       [r_idx, v_idx]
//     end
//       // 
//     # Returns the total distance travelled on route
//     def distance
//       @routes.inject(0) { |sum, route| sum += route.distance }
//     end
// 
//     def overtime
//       @routes.inject(0) { |sum, route| sum += route.overtime }
//     end
// 
//     def overload
//       @routes.inject(0) { |sum, route| sum += route.overload }
//     end
//     
//     def inspect
//       "{\n\t" << @routes.join("\n\t") << "}"
//     end
//   
//   protected
// 
//     def init_near_matrix
//       if @@near_matrix.empty?
//         @customers.each do |customer|
//           @@near_matrix[customer] = @customers.sort do |a,b|
//             (a.location.distance_to customer.location) <=> (b.location.distance_to customer.location)
//           end
//         end
//       end
//     end
//       
//   end
// 
// end
