
# require "../route"
# require "../city"
# require "../location"

module TSP

  module Simple
    DATA = [
      [1, 0, 0],
      [2, 1, 0],
      [3, 1, 1],
      [4, 0, 1],
    ]

    def self.cities
      cities = []
      DATA.each do |d|
        cities << City.new(d[0], Location.new(d[1], d[2]))
      end
      cities
    end
  
  end
  
end
