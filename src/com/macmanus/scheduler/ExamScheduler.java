package com.macmanus.scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class ExamScheduler{
    private int numGenerations;
    private int populationSize;
    private int numStudents;
    private int numModulesTotal;
    private int numModulesPerCourse;
    private int numDays;
    private int crossoverProbability;
    private int mutationProbability;
    private List<Schedule> studentSchedules;

    private ExamScheduler(int numGenerations,
                          int populationSize,
                          int numStudents,
                          int numModulesTotal,
                          int numModulesPerCourse,
                          int numDays,
                          int crossoverProbability,
                          int mutationProbability,
                          List<Schedule> studentSchedules){
        this.numGenerations = numGenerations;
        this.populationSize = populationSize;
        this.numStudents = numStudents;
        this.numModulesTotal = numModulesTotal;
        this.numModulesPerCourse = numModulesPerCourse;
        this.numDays = numDays;
        this.crossoverProbability = crossoverProbability;
        this.mutationProbability  = mutationProbability;
        this.studentSchedules = studentSchedules;
    }

    // This method will be used to control the rest of the program
    public void run(){
        EvolutionHandler eHandler = new EvolutionHandler(mutationProbability,
                                                         crossoverProbability);
        Population population     = new Population();
        Scanner scanner           = new Scanner(System.in);
        generateStudentSchedules();
        printStudentSchedules();

        population.randomlyGenerate(numDays, populationSize, numModulesTotal);
        population.calculateCosts(studentSchedules);
        population.sort();

        scanner.nextLine();
        System.out.println("\nGeneration 0\n" + population);
        System.out.println("-------------------------------------------");


        for(int i = 0; i < numGenerations; i++){
            population = eHandler.getNextGeneration(population);
            population.calculateCosts(studentSchedules);
            population.sort();
            System.out.println("Generation " + (i+1));
            System.out.println(population);
            System.out.println("-------------------------------------------");
        }
    }

    private void generateStudentSchedules(){
        studentSchedules = new ArrayList<>();

        Schedule studentSchedule;
        Random myRand = new Random();

        for(int i = 1; i <= numStudents; i++){
            studentSchedule = new Schedule(i);
            while(studentSchedule.getModuleNumbers().size() < numModulesPerCourse){
                int module = myRand.nextInt(numModulesTotal) + 1;
                studentSchedule.addModule(module);
            }
            studentSchedules.add(studentSchedule);
        }
    }

    private void printStudentSchedules(){
        for (Schedule studentSchedule : studentSchedules) {
            System.out.println(studentSchedule);
        }
    }

    public static class Builder{
        private int numGenerations;
        private int populationSize;
        private int numStudents;
        private int numModulesTotal;
        private int numModulesPerCourse;
        private int numDays;
        private int crossoverProbability;
        private int mutationProbability;
        private List<Schedule> studentSchedules;


        public Builder populationSize(int populationSize) {
            this.populationSize = populationSize;
            return this;
        }

        public Builder numStudents(int numStudents) {
            this.numStudents = numStudents;
            return this;
        }

        public Builder numModulesTotal(int numModulesTotal) {
            this.numModulesTotal = numModulesTotal;
            return this;
        }

        public Builder numModulesPerCourse(int numModulesPerCourse) {
            this.numModulesPerCourse = numModulesPerCourse;
            return this;
        }

        public Builder numDays(int numDays) {
            this.numDays = numDays;
            return this;
        }

        public Builder crossoverProbability(int crossoverProbability) {
            this.crossoverProbability = crossoverProbability;
            return this;
        }

        public Builder mutationProbability(int mutationProbability) {
            this.mutationProbability = mutationProbability;
            return this;
        }

        public Builder studentSchedules(List<Schedule> studentSchedules) {
            this.studentSchedules = studentSchedules;
            return this;
        }

        public Builder numGenerations(int numGenerations){
            this.numGenerations = numGenerations;
            return this;
        }

        public ExamScheduler createScheduler(){
            return new ExamScheduler(numGenerations,
                                     populationSize,
                                     numStudents,
                                     numModulesTotal,
                                     numModulesPerCourse,
                                     numDays,
                                     crossoverProbability,
                                     mutationProbability,
                                     studentSchedules);
        }
    }
}
