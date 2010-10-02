module BeesSolver
  class Solver
    
    def initialize(iterations, number_of_bees, sites, focused_sites, elite_bees, pleb_bees, explore_distance, monitor, domain_proxy, *args)
      @number_of_bees = number_of_bees
      @iterations = iterations
      @sites = sites
      @focused_sites = focused_sites
      @elite_bees = elite_bees
      @pleb_bees = pleb_bees
      @explore_distance = explore_distance
      @bees = []
      @monitor = monitor
      @domain_proxy = domain_proxy
      @domain_proxy_args = args
      @answer = nil
    end
    
    def solve
      # Create bees at random positions
      seed
      
      @iterations.times do |i|
        step(i)
      end
      
      @bees.sort! { |a,b| b.fitness <=> a.fitness }
      @bees[0]
    end
    
    def step(i)
      @bees.sort! { |a, b| b.fitness <=> a.fitness }
      @monitor.starting_step(i, @bees[0], @answer)
      @answer = @bees[0].fitness if @answer.nil? || @answer < @bees[0].fitness
      
      new_bees = []
      
      0.upto(@focused_sites) do |j|
        new_bees << @bees[j].dup
        @elite_bees.times do
          new_bees << @bees[j].dup
          new_bees.last.explore(@explore_distance, i)
        end
      end

      # @focused_sites.upto(@sites) do |j|
      #   @pleb_bees.times do
      #     @bees[j].explore(@explore_distance, i)
      #   end
      # end
      
      (@focused_sites).upto(@number_of_bees-1) do |j|
        new_bees << @bees[j]
        new_bees.last.random_position
      end
      
      # Site exhausted
      new_bees.each do |bee|
        if bee.age > 25
          bee.die
        end
      end
      
      @bees = new_bees
    end

    # def step(i)
    #   @bees.sort! { |a, b| b.fitness <=> a.fitness }
    #   @monitor.starting_step(i, @bees[0], @answer)
    #   @ansewr = @bees[0].fitness if @ansewr = @bees[0]
    #   
    #   0.upto(@focused_sites) do |j|
    #     @elite_bees.times do
    #       @bees[j].explore(@explore_distance, i)
    #     end
    #   end
    # 
    #   @focused_sites.upto(@sites) do |j|
    #     @pleb_bees.times do
    #       @bees[j].explore(@explore_distance, i)
    #     end
    #   end
    #   
    #   (@sites).upto(@number_of_bees-1) do |j|
    #     @bees[j].random_position
    #   end
    #   
    #   # Site exhausted
    #   @bees.each do |bee|
    #     if bee.age > 10_000
    #       bee.random_position
    #     end
    #   end
    #   
    # end
    
    def seed
      @number_of_bees.times do
        @bees << Bee.new(@domain_proxy.new(*@domain_proxy_args))
        @bees.last.random_position
      end
    end

  end
  
end