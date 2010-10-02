$LOAD_PATH.unshift(File.expand_path(File.dirname(__FILE__) + '/../lib')) unless $LOAD_PATH.include?(File.expand_path(File.dirname(__FILE__) + '/../lib'))
require "bees_solver"

ITEMS = [2,3,6,8,10,12,14,16,18,20,22,24,26,28,30,32,34,36,38,40,42,44,46,48,50,52,150]
MAX_WEIGHT = 151

class Knapsack < BeesSolver::Bee

  def initialize
    @items = []
  end
  
  def random_position
    @items = ITEMS.dup
    while weight > MAX_WEIGHT && !@items.empty? do
      @items.delete_at(rand(@items.size))
    end
  end

  def weight
    @items.inject(0) { |sum, i| sum + i }
  end

  def fitness
    @items.inject(0) {|sum, n| sum+n }
  end
  
  def explore(distance, only_if_better = true)
    old_fitness = fitness
    old_items = @items.dup
    
    2.times do
      @items.delete_at(rand(@items.size))
    end
      
    complement = ITEMS - @items
    count = 0
    while weight < MAX_WEIGHT && count < 10 do
      count += 1
      pos = rand(complement.size)
      if weight + complement[pos] <= MAX_WEIGHT
        @items << complement[pos]
        complement.delete_at(pos)
      end
    end
    
    if old_fitness > fitness
      @items = old_items
    end
  end
  
  def inspect
    @items.inspect
  end
      
end

solver = BeesSolver::Solver.new(10, 3, 1, 1, 1, 1, 0.3, Knapsack)
solution = solver.solve 

p solution.fitness, solution
