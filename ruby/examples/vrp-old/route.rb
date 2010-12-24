require "pp"
require "util/kernel"
require "util/array"

class Route
  include Enumerable
  
  attr_reader :depot
  
  def initialize(depot, jobs = Array.new)
    @dirty = true
    @depot = depot
    @jobs = jobs
  end

  def initialize_copy(orig)
    @jobs = @jobs.clone
  end

  def +(other_route)
    Route.new(self.depot, @jobs + other_route.to_a)
  end

  def -(other_route)
    Route.new(self.depot, @jobs - other_route.to_a)
  end
  
  def <<(job)
    @dirty = true
    raise Exception.new('Nil job passed in') if job == nil

    @jobs << job
    self
  end

  def random!
    @jobs.random!
  end

  def first
    @jobs.first
  end

  def last
    @jobs.last
  end

  def clear
    @dirty = true
    @jobs.clear
  end
    
  def [](index)
    if index == -1 || index == size
      return DepotJobStub.new(@depot)
    end
    
    @jobs[index]
  end

  def []=(index, job)
    @dirty = true
    
    raise Exception.new('Index outside of bounds') if index < 0 || index >= size
    raise Exception.new('Nil job passed in') if job == nil
    
    @jobs[index] = job
  end
  
  def index(job)
    @jobs.index(job)
  end
  
  def empty?
    @jobs.empty?
  end
  
  def size
    @jobs.size
  end

  def delete_at(idx)
    @dirty = true
    @jobs.delete_at(idx)
  end

  def last
    @jobs.last
  end

  def pop
    @dirty = true
    @jobs.pop
  end

  def empty?
    @jobs.empty?
  end

  def each
    @jobs.each { |job| yield job }
  end

  def each_index
    @jobs.each_index { |r_idx| yield r_idx }
  end

  def to_a
    return @jobs
  end
  
  def include?(job)
    return @jobs.include?(job)
  end

  def insert(index, *values)
    @dirty = true    
    raise Exception.new('Inserted past end job array:' + index.to_s) if index > @jobs.size
    
    @jobs.insert(index, *values)
  end

  # Returns the distance taking into consideration the driving route
  def route_distance
    sum = 0.0
    prev_location = self.depot
    @jobs.each do |job|
      sum += prev_location.route_distance_to job.location
      prev_location = job.location
    end
    # back to base
    sum += prev_location.route_distance_to depot
    sum
  end

  # Returns the travel duration taking into consideration the driving route
  def route_duration
    sum = 0.0
    prev_location = self.depot
    @jobs.each do |job|
      sum += prev_location.route_duration_to job.location
      prev_location = job.location
    end
    # back to base
    sum += prev_location.route_duration_to depot
    sum
  end
  
  def travel_duration
    sum = 0.0
    prev_location = self.depot
    @jobs.each do |job|
      sum += prev_location.duration_to job.location
      prev_location = job.location
    end
    # back to base
    sum += prev_location.duration_to depot
    sum
  end

  def job_duration
    @jobs.inject(0) { |sum, job| sum + job.duration }
  end
  
  def total_duration
    self.job_duration + self.travel_duration
  end

  def total_route_duration
    self.job_duration + self.route_duration
  end

  # Returns the total distance travelled on route
  def distance
    sum = 0.0
    prev_location = self.depot
    @jobs.each do |job|
      sum += prev_location.distance_to job.location
      prev_location = job.location
    end
    # back to base
    sum += prev_location.distance_to depot
    sum
  end
    
  def ==(other)
    return false if !other.instance_of?(Route)
    
    return false if other.size != self.size
    
    0.upto(size() - 1) do |i|
      if self[i] != other[i]
        return false
      end
    end
    
    return true 
  end
  
  # def clone
  #   Route.new(self.depot, @jobs.clone)
  # end
  
  # Reverses job order between two given indexs (inclusive)
  def reverse!(j_idx1, j_idx2)
    @dirty = true
    
    l_idx = Kernel::min(j_idx1, j_idx2)
    h_idx = Kernel::max(j_idx1, j_idx2)
    
    while (l_idx < h_idx)
      self.swap!(l_idx, h_idx)
      l_idx += 1
      h_idx -= 1
    end
  end
  
  # Swaps two jobs
  def swap!(j_idx1, j_idx2)
    @dirty = true
    tmp = @jobs[j_idx1]
    @jobs[j_idx1] = @jobs[j_idx2]
    @jobs[j_idx2] = tmp
  end
  
  def inspect
    str = '['
    @jobs.each_with_index do |job, j_idx|
      str += job.inspect
      str += "," if j_idx < (@jobs.size - 1)
    end
    str += ']'
  end
  
  def hash
    str = @jobs.inject('') { |str, job| str + job.id.to_s + ',' }
    str.hash
  end
    
  # XXX What is the fitness function here? 
  # XXX Can we make this more configurable? Perhaps pass it in as a block?
  def fitness
    if @dirty
      @fitness = self.distance + 
        # self.size ** 1.1
        # Kernel::max(self.size - 28, 0)
        # Kernel::max(total_duration - 36_000, 0)
        Kernel::max(total_duration - 27_000, 0) * 1
        
      @dirty = false
    end
    @fitness
  end

protected

  def fitness=(fitness)
    @fitness = fitness
  end
  def dirty=(dirty)
    @dirty = dirty
  end
  
end

# Stub class that returns a fake job that has it's delivery_location as the depot. Used for 
# route[-1] and route[max_index + 1]  
class DepotJobStub
  def initialize(depot)
    @depot = depot
  end
  
  def location
    return @depot
  end
end
