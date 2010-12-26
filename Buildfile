require 'buildr/scala'

repositories.remote << 'https://repository.apache.org/content/groups/public/'

define 'BeesSolver' do
  run.using :main => "com.visfleet.beessolver.Solver"
end


# java -server -Xbootclasspath/a:/opt/local/share/scala-2.8/lib/scala-library.jar -cp ./target/classes com.visfleet.beesolver.Solver