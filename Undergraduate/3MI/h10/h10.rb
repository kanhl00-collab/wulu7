Pair = Struct.new(:fst,:snd)

def summingPairs(xs, sum)
  the_pairs = []
  len = xs.length

  for i in 0..(len-1)
    for j in (i+1)..(len-1)
      reader, writer = IO.pipe
      fork do
      #writer.puts(xs[i])
        writer.puts(xs[j])
      end
      Process.waitall
      #int1 = reader.gets.to_i
      int2 = reader.gets.to_i
      #if xs[i] + xs[j] <= sum
      if xs[i] + int2 <= sum
        the_pairs.push(Pair.new(xs[i],int2))
      end
      #Process.waitall
      #end
    end
  end


  return the_pairs
end


require 'date'
require_relative 'collection'

puts "Starting at:   #{DateTime.now.sec} seconds, #{DateTime.now.strftime("%9N")} nanoseconds"
puts summingPairs(INPUT,2020)
puts "Ending at:     #{DateTime.now.sec} seconds, #{DateTime.now.strftime("%9N")} nanoseconds"
