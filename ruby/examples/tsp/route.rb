
module TSP
  
  class Route
    include Enumerable
  
    ##
    ## Solver methods
    ## 

    def initialize(cities = Array.new)
      @cities = cities.clone
      init_near_matrix
    end
    
    def init_near_matrix
      @near_matrix = {}
      @cities.each do |city|
        @near_matrix[city] = @cities.sort do |a,b| 
          (a.location.distance_to city.location) <=> (b.location.distance_to city.location)
        end
      end
    end

    def initialize_copy(orig)
      # XXX means they are sharing cities? Is this what we want?
      @cities = @cities.clone
    end

    def random_position
      old_cities = @cities.dup
      @cities.clear
      
      until old_cities.empty?
        @cities << old_cities.delete_rand
      end
    end
  
    def explore(distance, i)
      removed = []
      [rand(@cities.size * distance), 4].max.times { removed << @cities.delete_rand }
      
      # until removed.empty?
      #   @cities.insert_rand(removed.delete_rand)
      # end
      
      until removed.empty?
        city = removed.delete_rand
        insert_nearest(city, distance)
      end
    end
    
    def insert_nearest(city, distance)
      n_idx = rand(@cities.size * distance)
      n_city = @near_matrix[city][n_idx]
      idx = @cities.index(n_city)
      while idx.nil?
        n_idx += 1
        n_city = @near_matrix[city][n_idx]
        idx = @cities.index(n_city)
      end
      
      @cities.insert(idx, city)
    end

    # def insert_nearest(city)
    #   min_dist, min_idx = nil, nil
    #   @cities.each_with_index do |c2, idx|
    #     dist = city.location.distance_to c2.location
    #     if min_dist.nil? || dist < min_dist
    #       min_dist = dist
    #       min_idx = idx
    #     end
    #   end
    #   
    #   # Finally check end
    #   dist = @cities.last.location.distance_to city.location
    #   min_idx = @cities.size if dist < min_dist
    #   
    #   @cities.insert(min_idx, city)
    # end
      
    def fitness
      # raise "WTF #{self.inspect}" if self.distance < 0
      7542 - self.distance
    end

    ##
    ## Other methods
    ## 

    def <<(city)
      raise "Nil city passed in" if city == nil

      @cities << city
      self
    end

    # def random!
    #   @cities.random!
    # end
    # 
    # def first
    #   @cities.first
    # end
    # 
    # def last
    #   @cities.last
    # end

    # def clear
    #   @jobs.clear
    # end
    
    def [](index)
      @cities[index]
    end

    def []=(index, city)
      raise "Index outside of bounds" if index < 0 || index >= size
      raise "Nil job passed in" if city == nil
    
      @cities[index] = city
    end
  
    # def index(city)
    #   @cities.index(city)
    # end
  
    # def empty?
    #   @jobs.empty?
    # end
  
    def size
      @cities.size
    end

    def delete_at(idx)
      @cities.delete_at(idx)
    end

    # def pop
    #   @jobs.pop
    # end

    def each
      @cities.each { |city| yield city }
    end

    def each_index
      @cities.each_index { |idx| yield idx }
    end

    # def to_a
    #   return @cities
    # end
  
    # def include?(job)
    #   return @jobs.include?(job)
    # end

    def insert(index, *values)
      raise Exception.new('Inserted past end of cities array:' + index.to_s) if index > @cities.size
      @cities.insert(index, *values)
    end

    # Returns the total distance travelled on route
    def distance
      sum = 0.0
      @cities.each_with_index do |city, i|
        next_idx = (i + 1) % @cities.size
        sum += city.location.distance_to @cities[next_idx].location
      end
      sum
    end
    
    def ==(other)
      return false if !other.instance_of?(Route)
      return false if other.size != self.size
    
      0.upto(size - 1) do |i|
        if self[i] != other[i]
          return false
        end
      end
      
      return true
    end
    
    # Reverses job order between two given indexs (inclusive)
    # def reverse!(j_idx1, j_idx2)
    # 
    #   l_idx = Kernel::min(j_idx1, j_idx2)
    #   h_idx = Kernel::max(j_idx1, j_idx2)
    # 
    #   while (l_idx < h_idx)
    #     self.swap!(l_idx, h_idx)
    #     l_idx += 1
    #     h_idx -= 1
    #   end
    # end
    #   
    # # Swaps two jobs
    # def swap!(j_idx1, j_idx2)
    #   tmp = @jobs[j_idx1]
    #   @jobs[j_idx1] = @jobs[j_idx2]
    #   @jobs[j_idx2] = tmp
    # end
  
    def inspect
      '[' << @cities.join(",") << ']'
    end
  
    # def hash
    #   str = @jobs.inject('') { |str, job| str + job.id.to_s + ',' }
    #   str.hash
    # end
    
  protected

    def fitness=(fitness)
      @fitness = fitness
    end
      
  end

end
