module BeesSolver
  class Solver
    
    def initialize(iterations, no_sites, no_worker_bees, no_moves, explore_distance, monitor, domain_proxy, *args)
      @iterations = iterations
      @explore_distance = explore_distance
      @monitor = monitor
      @domain_proxy = domain_proxy
      @domain_proxy_args = args
      @answer = nil

      @no_sites = no_sites
      @no_worker_bees = no_worker_bees
      @no_moves = no_moves
      
      # raise "You must more scouts than worker bees!" if @no_scout_bees <= @no_worker_bees

      @sites = []
      # @scout_bees = scout_bees
      # @sites = sites
      # @bees = []
    end
    
    def solve
      # Create bees at random positions
      seed
      
      @iterations.times do |i|
        step(i)
      end
      
      @answer
    end
    
    def step(i)
      # find fittest site
      if i % 10 == 0
        @monitor.starting_step(i, @sites.first.best_bee, @answer)
        if i % 100 == 0
          puts @sites.join("\n")
        end
      end
      
      @sites.each do |site|
        site.explore(@no_moves, @explore_distance, i)
      end

      @sites.sort! { |a, b| b.best_bee.fitness <=> a.best_bee.fitness }
      @answer = @sites.first.best_bee.fitness if @answer.nil? || @answer < @sites.first.best_bee.fitness
    end

    def seed
      @no_sites.times do
        @sites << Site.new(@no_worker_bees, @domain_proxy, @domain_proxy_args)
      end
    end

    # def step(i)
    #   @bees.sort! { |a, b| b.fitness <=> a.fitness }
    #   @answer = @bees[0].fitness if @answer.nil? || @answer < @bees[0].fitness
    #   @monitor.starting_step(i, @bees[0], @answer) if i % 10 == 0
    # 
    #   new_sites = []
    #   
    #   # if i % 200 == 0
    #   #   str = ""
    #   #   @sites.times do |i|
    #   #     str << "#{@bees[i].fitness}, "
    #   #   end
    #   #   puts str
    #   # end
    #   
    #   @sites.times do |j|
    #     new_bees << (head_bee = @bees[j])
    #     head_bee.age
    #     (@worker_bees).times do
    #       new_bees << (worker_bee = head_bee.dup)
    #       worker_bee.explore(@explore_distance, i)
    #     end
    #   end
    #   
    #   @scout_bees.times do |j|
    #     new_bees << (scout_bee = @bees[0].dup)
    #     scout_bee.random_position
    #   end
    #         
    #   @bees = new_bees
    # end

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
    
    # def seed
    #   (@scout_bees + (@sites + @worker_bees)).times do
    #     @bees << Bee.new(@domain_proxy.new(*@domain_proxy_args))
    #     @bees.last.random_position
    #   end
    # end

  end
  
end                              