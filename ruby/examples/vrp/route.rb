
module VRP
  
  class Route
    include Enumerable
  
    attr_reader :visits
  
    ##
    ## Solver methods
    ## 

    def initialize(depot, max_route_time, capacity)
      @depot = depot
      @capacity = capacity
      @max_route_time = max_route_time
      @visits = []
    end
    
    def initialize_copy(orig)
      @visits = orig.visits.dup
    end
    
    def delete_rand
      @visits.delete_rand
    end
          
    def <<(customer)
      raise "Nil city passed in" if customer == nil

      @visits << customer
      self
    end
    
    def [](index)
      @visits[index]
    end

    def []=(index, customer)
      raise "Index outside of bounds" if index < 0 || index >= size
      raise "Nil customer passed in" if customer == nil
    
      @visits[index] = customer
    end
  
    def index(customer)
      @visits.index(customer)
    end
    
    def size
      @visits.size
    end

    def insert(index, *values)
      raise Exception.new('Inserted past end of visits array:' + index.to_s) if index > @visits.size
      @visits.insert(index, *values)
    end

    # Returns the total distance travelled on route
    def distance
      sum = 0.0
      prev_loc = @depot
      @visits.each do |customer|
        sum += prev_loc.distance_to customer.location
        prev_loc = customer.location
      end
      sum += prev_loc.distance_to @depot
            
      sum 
    end

    def overtime
      [service_time - @max_route_time, 0].max
    end

    def overload
      [load - @capacity, 0].max
    end

    def service_time
      @visits.inject(0) { |sum, customer| sum + customer.service_time }
    end

    def load
      @visits.inject(0) { |sum, customer| sum + customer.quantity }
    end
    
    # def ==(other)
    #   return false if !other.instance_of?(Route)
    #   return false if other.size != self.size
    # 
    #   0.upto(size - 1) do |i|
    #     if self[i] != other[i]
    #       return false
    #     end
    #   end
    #   
    #   return true
    # end
      
    def inspect
      # '[' << @visits.join(",") << ']'
      '[' << overload.to_s << ']'
    end
    
    def to_s
      inspect
    end
        
  end

end
