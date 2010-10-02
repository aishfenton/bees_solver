
module TSP

  class Location  
    attr_accessor :x, :y
  
    def initialize(x, y)
      @x = x.to_f
      @y = y.to_f
    end

    # Assuming symetric distance here (i.e from distance and to distance are the same)
    def distance_to(to_loc)
      dx = @x - to_loc.x
      dy = @y - to_loc.y
      Math.sqrt((dx ** 2) + (dy ** 2))
    end

    def manhattan_distance_to(to_loc)
      dx = @x - to_loc.x
      dy = @y - to_loc.y      
      dx.abs + dy.abs
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
  
    def inspect
      return '[' << @x.to_s << ', ' << @y.to_s << ']'
    end

    def to_s
      inspect
    end
  
  end

end
