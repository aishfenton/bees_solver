require 'buildr/scala'

repositories.remote << 'https://repository.apache.org/content/groups/public/'

define 'BeesSolver' do
  run.using :main => "com.visfleet.beessolver.Solver"
end
