require 'buildr/scala'

repositories.remote << 'https://repository.apache.org/content/groups/public/'

# define 'BeesSolver' do
#   compile.options.optimise = true
#   run.using :main => "com.visfleet.beessolver.Solver", 
#             :java_args => ["-server"]
# end

define 'Test' do
  compile.options.optimise = true
  run.using :main => "com.visfleet.beessolver.Test", 
            :java_args => ["-server"]
end