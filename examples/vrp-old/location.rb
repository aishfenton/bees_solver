require "geo_route"

class Location
  
  attr_accessor :x, :y
  
  @@distance_cache = {}
  METRES_PER_SEC = 16.66

  def initialize(x, y)
    @x = x.to_f
    @y = y.to_f
  end

  def route_distance_to(to_loc)
    # Cache
    result = @@distance_cache[self.to_s + '-' + to_loc.to_s]
    if result.nil?
      result = GeoRoute.distance_between(:from => "#{self.y},#{self.x}", :to => "#{to_loc.y},#{to_loc.x}")
      @@distance_cache[self.to_s + '-' + to_loc.to_s] = result
      @@distance_cache[to_loc.to_s + '-' + self.to_s] = result
    end
    result.distance
  end

  def route_duration_to(to_loc)
    # Cache
    result = @@distance_cache[self.to_s + '-' + to_loc.to_s]
    if result.nil?
      result = GeoRoute.distance_between(:from => "#{self.y},#{self.x}", :to => "#{to_loc.y},#{to_loc.x}")
      @@distance_cache[self.to_s + '-' + to_loc.to_s] = result
      @@distance_cache[to_loc.to_s + '-' + self.to_s] = result
    end
    result.duration
  end

  # Assuming symetric distance here (i.e from distance and to distance are the same)
  def distance_to(to_loc)
    # if (to_loc.y < -37.01) || (@y < -37.01)
    #   return self.route_distance_to(to_loc)
    # end
    manhattan_distance(to_loc)
  end

  def manhattan_distance(to_loc)
    length = to_loc.x - x
    height = to_loc.y - y
    result = length.abs + height.abs
    result * 111_000
  end
  
  def duration_to(to_loc)
    distance = manhattan_distance(to_loc)
    distance / METRES_PER_SEC
  end
  
  def ==(other)
    if !other.instance_of?(Location) 
      return false
    end
    return self.x == other.x && self.y == other.y
  end
  
  def eql?(other)
     return self == other
  end
  
  def to_s
    return '[' + @x.to_s + ', ' + @y.to_s + ']'
  end
  
end
