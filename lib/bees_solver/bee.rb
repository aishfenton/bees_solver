
module BeesSolver
  class Bee
    attr_reader :domain_proxy, :age
    
    def initialize(domain_proxy)
      @domain_proxy = domain_proxy
      @dirty = true
      @age = 0
    end
    
    def initialize_copy(orig)
      @domain_proxy = orig.domain_proxy.dup
    end
    
    def die
      puts "BANG!"
      @age = 0
      self.random_position
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
        
    def explore(distance, i)      
      # old_pos = @domain_proxy.dup
      old_fitness = fitness
      
      @dirty = true
      @domain_proxy.explore(distance, i)
      
      # move_back, old one better
      if fitness <= old_fitness
        @age += 1
        # @domain_proxy = old_pos
        # @fitness = old_fitness
      else
        @age = 0
      end
      
    end

  end
end