$LOAD_PATH.unshift(File.expand_path(File.dirname(__FILE__) + '/../../lib')) unless $LOAD_PATH.include?(File.expand_path(File.dirname(__FILE__) + '/../../lib'))
require 'RMagick'
require 'ffi-opengl'

require "bees_solver"
require './canvas'

NUM_POLYGONS = 50
NO_OF_POINTS = 3
REAL_IMAGE = Magick::Image.read(ARGV[0]).first

module Giochonda
       
  class ProblemProxy

    def initialize(canvas = Canvas.new(REAL_IMAGE.columns, REAL_IMAGE.rows, NUM_POLYGONS, NO_OF_POINTS))
      @canvas = canvas
    end
  
    def initialize_copy(obj)
      super
      @canvas = obj.canvas.dup
    end

    def random_position
      @canvas.randomize
    end

    def fitness
      @canvas.draw
      1.0 - @canvas.difference(REAL_IMAGE)
    end
  
    def explore(distance, i)
      @canvas.explore(distance, i)
    end
    
    def draw
      @canvas.draw
    end
  
  protected
    
    def canvas
      @canvas
    end
          
  end

  class Monitor
    attr_accessor :fittest
  
    def starting_step(i, best, answer)
      puts "Iteration: #{i} Fitness: #{best.fitness}, Answer: #{answer}"
      best.domain_proxy.draw
      GL::glFlush
    end
  end
  
  class Window
    include GL, GLU, GLUT
    
    def start(solver)
      display = lambda do
        @count ||= 0
        @count+=1
        solver.step(@count)
      end

      glutInit(FFI::MemoryPointer.new(:int, 1).put_int(0, 0), FFI::MemoryPointer.new(:pointer, 1).put_pointer(0, nil))
      glutInitDisplayMode(GLUT_SINGLE | GLUT_RGB | GLUT_ALPHA)
      glutInitWindowSize(REAL_IMAGE.columns, REAL_IMAGE.rows)
      glutInitWindowPosition(100, 100)
      glutCreateWindow($0)
      glEnable(GL_BLEND)
      glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
      glutDisplayFunc(display)
      # glutKeyboardFunc(keyboard)
      glutIdleFunc(display)
      glutMainLoop
    end
  
  end
  
end

monitor = Giochonda::Monitor.new
solver = BeesSolver::Solver.new(0, 0, 1, 1, 1.0, monitor, Giochonda::ProblemProxy)
solver.seed
# solution = solver.solve

Giochonda::Window.new.start(solver)