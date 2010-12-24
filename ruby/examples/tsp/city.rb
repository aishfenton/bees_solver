
module TSP

  class City
      attr_reader :id, :location

      def initialize(id, location)
        @id = id
        @location = location
      end
    
      def inspect
        id.to_s
      end
    
      def to_s
        id.to_s
      end

      def ==(other)
        if !other.instance_of?(City)
          return false
        end
        return self.id == other.id
      end
  end
  
  
end
