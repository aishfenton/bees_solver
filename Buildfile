require 'buildr/scala'

repositories.remote << 'https://repository.apache.org/content/groups/public/'

define 'BeesSolver' do
  compile.options.optimise = true

  task :debug, :problem, :needs => :compile do |t, args|
    run.using :main => ["com.routably.beessolver.vrp.Runner", args.problem],
              :java_args => ["-server"]
  
    Rake::Task["run"].invoke
  end

  # buildr BeesSolver:benchmark[100]
  task :benchmark, :duration, :set_name, :needs => :compile do |t, args|
    
    raise Exception.new("Duration parameter must be specified!") unless args.duration
        
    problems = ["P01E51K05", "P02E76K10", "P03E101K08", "P04E151K12", "P05E200K17",
                "P06D51K06", "P07D76K11", "P08D101K09", "P09D151K14", "P10D200K18",
                "P11E121K07", "P12E101K10", "P13D121K11", "P14D101K11"]
    
    run.using :main => ["com.routably.beessolver.vrp.Runner ", args.duration, args.set_name, problems.join(",")],
              :java_args => ["-server"]
              
    Rake::Task["run"].invoke
    
    # produce_joined_dat(problems, args.set_name)
    # produce_plot(problems, args.set_name)
  end

end

def produce_plot(problems, set_name)
  cmd = <<-EOF
set key right bottom;
set terminal postscript eps font 'CMUBright-Roman, 10';
set output '#{set_name}/#{set_name}.eps';
set xlabel 'Time (secs)';
set ylabel 'Percentage of Best Known';
set style line 1 lt 1 lw 1 pt 10 linecolor rgb 'red';
EOF

  series = []
  problems.each_with_index do |problem, i|
    series << "'#{set_name}/#{set_name}.dat' using 1:#{(3*(i+1))+1} with lines ti '#{problem}'"
  end
  cmd += "plot [] [0.9:1.0] '#{set_name}/#{set_name}.dat' using 1:(\\$4+\\$7+\\$10+\\$13+\\$16+\\$19+\\$22+\\$25+\\$28+\\$31+\\$34+\\$37+\\$40+\\$43)/14 with linespoints ls 1 ti 'Average';"

  `gnuplot -e "#{cmd}"`
end

def produce_joined_dat(problems, set_name)
  cmd = "join #{set_name}/#{problems[0]} #{set_name}/#{problems[1]}"
  
  2.upto(problems.size - 1) do |i|
    cmd += " | join - #{set_name}/#{problems[i]}"
  end
  
  cmd +=  " > #{set_name}/#{set_name}.dat"

  `#{cmd}`
end
