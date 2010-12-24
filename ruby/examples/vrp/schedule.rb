
module VRP
  
  class Schedule
  
    attr_reader :routes
  
    ##
    ## Solver methods
    ## 

    @@near_matrix = {}

    def initialize(max_vehicles, depot, capacity, max_route_time, customers = Array.new)
      @max_vehicles = max_vehicles
      @customers = customers
      @capacity = capacity
      @depot = depot
      @max_route_time = max_route_time
      init_near_matrix
      @routes = []
    end

    def initialize_copy(orig)
      @routes = orig.routes.map { |r| r.dup }
    end

    def random_position
      tbd_customers = @customers.dup
      @routes.clear

      # create and seed routes
      @max_vehicles.times do
        @routes << (route = Route.new(@depot, @max_route_time, @capacity))
        route << tbd_customers.delete_rand
      end

      @count = 0
      until tbd_customers.empty?
        customer = tbd_customers.delete_rand
        insert_nearest(customer, 0)
        # @routes[@count % @routes.size] << old_customers.delete_rand
        # @count += 1
      end
    end
  
    def explore(distance, i)
      removed = []
      [rand(@customers.size * distance), 4].max.times { removed << @routes.rand.delete_rand }
      
      removed.compact!
      
      # until removed.empty?
      #   @cities.insert_rand(removed.delete_rand)
      # end
      
      until removed.empty?
        customer = removed.delete_rand
        insert_nearest(customer, distance)
      end
    end
    
    ##
    ## Other methods
    ## 

    def insert_nearest(customer, distance)
      offset = rand(@customers.size * distance)
      n_customer = @@near_matrix[customer][offset]
      
      r_idx, v_idx = self.index(n_customer)
      
      while r_idx.nil?
        offset += 1
        n_customer = @@near_matrix[customer][offset % @@near_matrix[customer].size]
        r_idx, v_idx = self.index(n_customer)
      end
      
      @routes[r_idx].insert(v_idx, customer)
    end
      
    def index(customer)
      r_idx = nil
      v_idx = nil

      @routes.each_with_index do |route, i|
        v_idx = route.index(customer)
        if v_idx
          r_idx = i
          break
        end
      end

      [r_idx, v_idx]
    end
      
    def fitness
      -( self.distance + (self.overload * 2) + (self.overtime * 2) )
      # -( self.overload ** 2 )
      # -( self.distance )
    end

    # Returns the total distance travelled on route
    def distance
      @routes.inject(0) { |sum, route| sum += route.distance }
    end

    def overtime
      @routes.inject(0) { |sum, route| sum += route.overtime }
    end

    def overload
      @routes.inject(0) { |sum, route| sum += route.overload }
    end
    
    def inspect
      "{\n\t" << @routes.join("\n\t") << "}"
    end
  
  protected

    def init_near_matrix
      if @@near_matrix.empty?
        @customers.each do |customer|
          @@near_matrix[customer] = @customers.sort do |a,b|
            (a.location.distance_to customer.location) <=> (b.location.distance_to customer.location)
          end
        end
      end
    end
      
  end

end
