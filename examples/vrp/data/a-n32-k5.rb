
module VRP

  # NAME : A-n32-k5
  # COMMENT : (Augerat et al, No of trucks: 5, Optimal value: 784)
  # TYPE : CVRP
  # DIMENSION : 32
  # EDGE_WEIGHT_TYPE : EUC_2D 
  # CAPACITY : 100
  module A_N32_K5
    VEHICLE_CAPACITY = 100
    MAX_ROUTE_TIME = 999999
    SERVICE_TIME = 0
    DEPOT = Location.new(82, 76)

    CUSTOMERS = [
      # [1, 82, 76, 0], 
      [2, 96, 44, 19], 
      [3, 50, 5, 21], 
      [4, 49, 8, 6], 
      [5, 13, 7, 19], 
      [6, 29, 89, 7], 
      [7, 58, 30, 12], 
      [8, 84, 39, 16], 
      [9, 14, 24, 6], 
      [10, 2, 39, 16], 
      [11, 3, 82, 8], 
      [12, 5, 10, 14], 
      [13, 98, 52, 21], 
      [14, 84, 25, 16], 
      [15, 61, 59, 3], 
      [16, 1, 65, 22], 
      [17, 88, 51, 18], 
      [18, 91, 2, 19], 
      [19, 19, 32, 1], 
      [20, 93, 3, 24], 
      [21, 50, 93, 8], 
      [22, 98, 14, 12], 
      [23, 5, 42, 4], 
      [24, 42, 9, 8], 
      [25, 61, 62, 24], 
      [26, 9, 97, 24], 
      [27, 80, 55, 2], 
      [28, 57, 69, 20], 
      [29, 23, 15, 15], 
      [30, 20, 70, 2], 
      [31, 85, 60, 14], 
      [32, 98, 5, 9], 
    ]

    def self.customers
      customers = []
      CUSTOMERS.each do |d|
        customers << Customer.new(d[0], Location.new(d[1], d[2]), d[3], SERVICE_TIME)
      end
      customers
    end
  
  end
  
end

