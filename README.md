# Exam-Scheduler

This repository contains a genetic algorithm that uses genetic techniques such as crossover, mutation and selection to find the best exam schedule based on a set of students and their set of modules. 
### Algorithm
1. Assign each student the modules that they will be examined on.
2. Generate a number random exam orderings. This is the first generation.
3. Evaluate the fitness cost of each ordering. The fitness cost is incremented when, for a particular exam day, there is a student who has 2 or more exams on that day.
4. Apply [selection](https://en.wikipedia.org/wiki/Selection_(genetic_algorithm)) on the orderings
5. Iterate over the orderings, probabilistically applying either [crossover](https://en.wikipedia.org/wiki/Crossover_(genetic_algorithm)), [mutation](https://en.wikipedia.org/wiki/Mutation_(genetic_algorithm)) or reproduction (copy ordering directly into next generation with no change).
6. If we have completed the desired number of generations the program ends, otherwise go to 3.

