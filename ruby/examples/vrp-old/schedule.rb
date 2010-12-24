require 'model/solution'
require 'model/route'

class Schedule < Solution
  attr_reader :depot
  attr_accessor :routes
  
# Bee solver methods
# ------

  def initialize(jobs, size, depot)
    @jobs = jobs
    @depot = depot
    @routes = Array.new(size) { Route.new(depot) }
  end

  def initialize_copy(orig)
    # clone route objects too
    @routes = orig.routes.map { |route| route.clone }
  end

  def random_position
    @schedule.random_position
  end
  
  def explore(distance, i)
    # @schedule.explore(distance, i)
  end

  def fitness
    @routes.inject(0.0) { |sum, route| sum + route.fitness }
  end
  
# Other methods
# ------  

  def add_route
    @routes << Route.new(@depot)
  end

  # Returns the total distance travelled in this solution
  def distance
    @routes.inject(0) { |sum, route| sum + route.distance }
  end
  
  # Returns the total distance travelled on route
  def route_distance
    @routes.inject(0) { |sum, route| sum + route.route_distance }
  end

  # Returns the travel duration taking into consideration the driving route
  def route_duration
    @routes.inject(0) { |sum, route| sum + route.route_duration }
  end
  
  # Returns the total number of jobs scheduled
  def size
    @routes.inject(0) { |sum, route| sum + route.size }
  end

  def max_size
    r = @routes.max { |r1, r2| r1.size <=> r2.size }
    r.size
  end
  
  def job_index(job)
    @routes.each_with_index do |route, r_idx|
      j_idx = route.index(job)
      return r_idx, j_idx unless j_idx.nil?
    end
    nil
  end
  
  # Returns an array of all jobs scheduled in the solution
  def jobs
    jobs = @routes.inject([]) { |arr, route| arr + route.to_a }
    jobs
  end
  
  # Returns the route_index and job_index of the given job within this solution
  def find_job(job)
    @routes.each_with_index do | route, r_idx |
      j_idx = route.index(job)
      return r_idx, j_idx if !j_idx.nil?
    end
    raise Exception.new("Couldn't find job:" + job.to_s)
  end
  
  def ==(other)
    return false if !other.instance_of? Schedule

    # must have same depots
    return false if @depot != other.depot
    
    # must have the same number of routes
    return false if @routes.size() != other.routes.size
    
    # must contain an equal route
    @routes.each do |route|
      return false if !other.routes.include?(route)
    end
      
    return true
  end

  def empty_routes
    @routes.inject(0) do |sum, route|
      if route.empty?
        sum + 1
      else
        sum
      end
    end
  end

  def used_routes
    @routes.size - self.empty_routes 
  end

  # Moves source (r_idx1, j_idx1) to destination (r_idx2, j_idx2). Inserts job before j_idx2   
  def move!(r_idx1, j_idx1, r_idx2, j_idx2)
    job = @routes[r_idx1].delete_at(j_idx1)
    @routes[r_idx2].insert(j_idx2, job)
  end

  # Swaps the specified jobs between the routes 
  def swap!(r_idx1, j_idx1, r_idx2, j_idx2)
    tmp = @routes[r_idx1][j_idx1]
    @routes[r_idx1][j_idx1] = @routes[r_idx2][j_idx2]
    @routes[r_idx2][j_idx2] = tmp
  end

  
  def inspect
    str = "\n"
    @routes.each do |route|
      str += route.inspect + "\n"
    end
    str
  end

  # Hash is based on routes and jobs
  def hash
    hashes = @routes.map { |route| route.hash }
    hashes.sort!
    str = hashes.inject('') { |str, hash| str + hash.to_s + '-' }
    str.hash
  end
  
end
