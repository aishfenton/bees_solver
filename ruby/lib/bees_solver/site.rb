module BeesSolver
  
  class Site
    MAX_AGE = 2_000
    
    attr_accessor :oldness, :bees
    
    def initialize(no_worker_bees, domain_proxy, domain_proxy_args)
      @oldness = 0
      @bees = []
      @domain_proxy = domain_proxy
      @domain_proxy_args = domain_proxy_args
      @no_worker_bees = no_worker_bees
      self.random_position
    end

    def explore(no_moves, explore_distance, i)
      # self.age
      old_fitness = best_bee.fitness

      # we create new bees, rather than moving them, because it's easier
      new_bees = []
      new_bees << self.best_bee.dup

      @bees.each do |old_bee|
        no_moves.times do
          new_bees << (new_bee = old_bee.dup)
          new_bee.explore(explore_distance, i)
        end
      end
            
      new_bees.sort! { |a, b| b.fitness <=> a.fitness }
      @bees = new_bees[0, @no_worker_bees]
      
      @oldness = 0 if best_bee.fitness > old_fitness
    end

    def age
      @oldness += 1
      die if @oldness > MAX_AGE
    end

    def die
      puts "Site #{best_bee.fitness} dead!!! Starting a new one"
      self.random_position
    end
    
    def random_position
      @oldness = 0
      @bees.clear
      @no_worker_bees.times do
        @bees << Bee.new(@domain_proxy.new(*@domain_proxy_args))
      end
    end

    def best_bee
      # am assuming they've been sorted
      @bees.first
    end
    
    def to_s
      "[" << @bees.map { |b| b.fitness }.join(",") << "]"
    end
  
  end

end
