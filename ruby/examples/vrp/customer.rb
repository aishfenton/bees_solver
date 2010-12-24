
module VRP

  class Customer
      attr_reader :location, :quantity, :service_time

      def initialize(id, location, quantity, service_time)
        @id = id
        @location = location
        @quantity = quantity
        @service_time = service_time
      end
    
      def inspect
        @id.to_s
      end
    
      def to_s
        inspect
      end

      def ==(other)
        if !other.instance_of?(Customer)
          return false
        end
        return self.location == other.location
      end
  end
  
  
end
