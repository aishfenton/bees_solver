require 'model/location'

class Job

    attr_reader :id, :location, :quantity, :meta_data
    attr_accessor :nearest_locations, :duration
        
    def initialize(id, duration, location, quantity = nil, meta_data = {})
      @id = id;
      @location = location
      @quantity = quantity
      @duration = duration
      @meta_data = meta_data
    end
    
    def inspect
      self.to_s
    end
    
    # def location_centroid
    #   x = @pickup_location.x + @delivery_location.x
    #   y = @pickup_location.y + @delivery_location.y
    #         
    #   return Location.new(x / 2, y / 2)
    # end

    def end_date
      return release_date + duration
    end

    def ==(other)
      if !other.instance_of?(Job)
        return false
      end
      return self.id == other.id
    end

    def to_s
       id.to_s
    end

end
