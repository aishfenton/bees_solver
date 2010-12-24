require 'RMagick'

module Giochonda

  MAX_PIXELS_MOVED = 100
  
  class Canvas
    include GL, GLU
  
    def initialize(width, height, no_of_polygons, no_of_points)
      @no_of_polygons = no_of_polygons
      @no_of_points = no_of_points
      @width = width
      @height = height

      @polygons = []
    end
    
    def initialize_copy(obj)
      super
      @polygons = obj.polygons.map { |p| p.dup }
    end

    def difference(original)
      b_size = @width * @height * 3
      ptr = FFI::MemoryPointer.new(:ushort, b_size)
      glReadPixels(0, 0, @width, @height, GL_RGB, GL_UNSIGNED_SHORT, ptr)
      pixels = ptr.get_bytes(0, ptr.size)

      @image ||= Magick::Image.new(@width, @height)
      @image.import_pixels(0, 0, @width, @height, "RGB", pixels, Magick::ShortPixel)
      @image.flip!
      @image.difference(original)[1]
    end

    def explore(distance, i)
      
      @@no_polys ||= 3
      if (i % 50 == 0 && @@current_iteration != i)
        @@no_polys = within(@@no_polys + 1, 1, @no_of_polygons)
      end
      @@current_iteration = i

      if (@polygons.size != @@no_polys)
        @polygons << poly = Polygon.new(@no_of_points, @width, @height)
        poly.randomize
      end

      # add / remove polygon
      # case (-1..1).to_a.rand
      # when -1
      #   @polygons.delete_at(rand(@polygons.size))
      #   return
      # when 1
      #   if @polygons.size < @no_of_polygons
      #     @polygons << poly = Polygon.new(@no_of_points, @width, @height)
      #     poly.randomize
      #   end
      #   return
      # when 0
      #   # noop
      # end

      rand(@polygons.size + 1).times do
        @polygons[rand(@polygons.size)].explore(distance, i)
      end
    end

    def randomize
      @polygons.clear
      3.times do
        @polygons << poly = Polygon.new(@no_of_points, @width, @height)
        poly.randomize
      end
    end
  
    def draw
      @polygons.sort { |a,b| a.weight <=> b.weight }
      
      glClear(GL_COLOR_BUFFER_BIT)
      glMatrixMode (GL_PROJECTION);
      glLoadIdentity
      glScalef(2, 2, 0)
      glTranslatef(-0.5, -0.5, 0)
      @polygons.each do |poly|
        glColor4f(poly.color[0], poly.color[1], poly.color[2], 0.5)
        glBegin(GL_POLYGON)
        poly.points.each do |p|
          glVertex2f(p.x / @width.to_f, p.y / @height.to_f)
        end
        glVertex2f(poly.points.last.x / @width.to_f, poly.points.last.y / @height.to_f)
        glEnd
      end
    end

    def write(file)
      @canvas.write(file)
    end
    
  protected
  
    def polygons
      @polygons
    end

    def canvas
      @canvas
    end
  
  end

  class Polygon
    attr_accessor :color, :points, :weight

    def initialize(no_of_points, max_width, max_height)
      @no_of_points = no_of_points
      @max_width = max_width
      @max_height = max_height
      @points = []
      @color = []
      @weight = 0.0
    end
    
    def initialize_copy(obj)
      super
      @points = obj.points.map { |p| p.dup }
      @color = obj.color.dup
    end
        
    def randomize
      @points = []
      @weight = rand
      @color = [rand, rand, rand]
      @no_of_points.times do
        @points << Point.new(rand(@max_width), rand(@max_height))
      end
    end
    
    def explore(distance, i)
      
      case rand(2)
      when 0
        # move points
        rand(@points.size).times do
          @points[rand(@points.size)].explore(distance, @max_width, @max_height)
        end
      when 1
        # change color
        dw = rand * distance * (-1..1).to_a.rand
        @weight = within(weight + dw, 0.0, 1.0)

        dr = rand * distance * (-1..1).to_a.rand
        dg = rand * distance * (-1..1).to_a.rand
        db = rand * distance *(-1..1).to_a.rand
        @color[0] = within(@color[0] + dr, 0.0, 1.0)
        @color[1] = within(@color[1] + dg, 0.0, 1.0)
        @color[2] = within(@color[2] + db, 0.0, 1.0)
      end

    end

    def [](index)
      @points[index]
    end
      
  end

  class Point
    attr_accessor :x, :y

    def initialize(x, y)
      @x, @y = x, y
    end

    def explore(distance, max_width, max_height)
      dx = rand(max_width / 2 * distance).to_i * (-1..1).to_a.rand
      dy = rand(max_height / 2 * distance).to_i * (-1..1).to_a.rand
      
      @x = within(@x + dx, 0, max_width)
      @y = within(@y + dy, 0, max_height)
    end
  end

end

class Array
  def rand
    self[Kernel.rand(self.size)]
  end
end

module Kernel
  
  def within(value, min, max)
    return min if value < min
    return max if value > max
    value
  end

end
