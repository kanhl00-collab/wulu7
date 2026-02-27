# single element without loop
def change(x)
    if x%3 == 0
        if x%5 == 0
            return "fizzbuzz"
        else
            return "fizz"
        end
    elsif x%5 == 0
        return "buzz"
    else
        return x.to_s
    end
end

# with loop
def fizzbuzzLooper(list)
    result = []
    for i in list
        result.push(change(i))
    end
    return result
end

# with iterator
def fizzbuzzIterator(list)
    result = []
    list.each { |x| result.push(change(x)) }
    return result
end


def zuzzer(list, rules)
    result = Array.new(list.size,"")
    for i in 0..list.size-1
        orO = false
        for rule in rules
            if rule[0].(list[i])
                orO = true
                result[i] += rule[1].("")
            end
        end
        if !orO
            result[i] = list[i].to_s
        end
    end
    return result
end

