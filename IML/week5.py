import random
target_sentence = "I am the BEST!"
gene_pool = " ,!?abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
population_size = 100

def generate_chromosome(length):
    genes = []
    while len(genes) < length:
        genes.append(gene_pool[random.randrange(0,len(gene_pool))])
    return ''.join(genes)

def calculate_fitness(chromosome):
    fitness = 0
    ctr = 0
    for i in chromosome:
        if i== target_sentence[ctr]:
            fitness +=1
        ctr +=1
    return fitness+1

def mutate(chromosome, rate = 0.5):
    if random.random() < rate:
        index_to_mutate = random.randrange(0, len(chromosome))
        gene = list(chromosome)
        mutated_gene = gene_pool[random.randrange(0,len(gene_pool))]
        gene[index_to_mutate]= mutated_gene
        return ''.join(gene)
    else:
        return chromosome

population = []
for i in range(population_size):
    population.append(generate_chromosome(len(target_sentence)))

population_fitness = []
for chromosome in population:
    population_fitness.append(calculate_fitness(chromosome))

def roulette_wheel_selection():
    total_fitness = sum(population_fitness)
    if total_fitness == 0:
        return random.sample(population,2)
    probabilities = [f / total_fitness for f in population_fitness]
    selected = random.choices(population, weights=probabilities, k=2)
    return selected

def crossover(parent1, parent2):
    crossover_point = random.randrange(1, len(parent1)-1)
    child1 = parent1[:crossover_point] + parent2[crossover_point:]
    child2 = parent2[:crossover_point] + parent1[crossover_point:]
    return child1, child2

print(population)
print(population_fitness)

for generation in range(3000):
    parent1, parent2 = roulette_wheel_selection()
    child1, child2 = crossover(parent1, parent2)
    print("Parent1: ", parent1)
    print("Parent2: ", parent2)
    print("Child1: ", child1)
    print("Child2: ", child2)
    #parent_index = population_fitness.index(max(population_fitness))
    #parent = population[parent_index]
    child1 = mutate(child1)
    child2 = mutate(child2)
    print("MChild1: ", child1)
    print("MChild2: ", child2)

    child1_fitness = calculate_fitness(child1)
    print("Child1 Fitness: ", child1_fitness)
    child2_fitness = calculate_fitness(child2)
    print("Child2 Fitness: ", child2_fitness)

    for i in range(2):
        index_to_delete = population_fitness.index(min(population_fitness))
        del population[index_to_delete]
        del population_fitness[index_to_delete]

    population.append(child1)
    population_fitness.append(child1_fitness)
    population.append(child2)
    population_fitness.append(child2_fitness)

    print("Current Population: ", population)
    print("Current Fitness: ", population_fitness)

    if child1 == target_sentence or child2 == target_sentence:
        print("Solution found at Generation: ", generation)
        break


