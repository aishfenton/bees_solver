
module BeesSolver
  class Bee
    
    MAX_POSITIONS = 2_000_000
    
    @@positions = {}
    
    attr_reader :domain_proxy
    
    def initialize(domain_proxy)
      @domain_proxy = domain_proxy
      @dirty = true
      self.random_position
    end
    
    def initialize_copy(orig)
      @domain_proxy = orig.domain_proxy.dup
    end

    def random_position
      @domain_proxy.random_position
      @dirty = true
    end

    def fitness
      if @dirty
        @fitness = @domain_proxy.fitness
        @dirty = false
      end
      @fitness
    end
    
    def self.purge_positions
      puts "purge"
      while @@positions.size > (MAX_POSITIONS / 2)
        max = @@positions.keys.max
        min = @@positions.keys.min
        puts "    min:#{min}, max:#{max}, mid:#{(min+max)/2}"
        @@positions.delete_if { |key, value| key < ((min + max) / 2) } # delete if over halfway point
      end
    end
        
    def explore(distance, i)
      old_fitness = fitness
      
      explore_domain_proxy(distance, i)

      # if already been here, go some place new
      while @@positions.has_key?(fitness)
        puts "."
        explore_domain_proxy(distance, i)
      end
      
      @@positions[fitness] = true
      Bee.purge_positions if @@positions.size > MAX_POSITIONS
    end

    def explore_domain_proxy(distance, i)
      @dirty = true
      @domain_proxy.explore(distance, i)
    end

  end
end