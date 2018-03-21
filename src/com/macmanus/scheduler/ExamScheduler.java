package com.macmanus.scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    public ExamScheduler(){
        studentSchedules = new ArrayList<>();
    }

    // This method will be used to control the rest of the program
    public void run(){

        initializeParameters();
        generateStudentSchedules();

        Population population = new Population();
        population.randomlyGenerate(numDays, populationSize, numModulesTotal);


        for(int i = 0; i < studentSchedules.size(); i++) {
            System.out.println(studentSchedules.get(i));
        }

        population.calculateCosts(studentSchedules);
        population.sort();
        System.out.println("\n" + population);

        Evolver evolver = new Evolver(mutationProbability, crossoverProbability);

        for(int i = 0; i < numGenerations; i++){
            population = evolver.getNextGeneration(population);
            population.calculateCosts(studentSchedules);
            population.sort();
            System.out.println(population);
        }
    }

    public void initializeParameters(){

        ParameterReader reader = new ParameterReader();

        numGenerations = reader.getNumGenerations();
        populationSize = reader.getPopulationSize();
        numStudents = reader.getNumStudents();
        numModulesTotal = reader.getNumModulesTotal();
        numModulesPerCourse = reader.getNumModulesPerCourse();
        numDays = reader.getNumDays();
        crossoverProbability = reader.getCrossoverProbability();
        mutationProbability = reader.getMutationProbability();

        //Vaidate parameters.
        while(!(numModulesTotal > numModulesPerCourse)){
            System.out.println("Error! There are less total modules " +
                               "than modules per course");

            numModulesTotal = reader.getNumModulesTotal();
            numModulesPerCourse = reader.getNumModulesPerCourse();
        }

        while(crossoverProbability + mutationProbability > 100){
            System.out.println("Crossover and mutation probability sum " +
                               "to be grater than 100");
            crossoverProbability = reader.getCrossoverProbability();
            mutationProbability = reader.getMutationProbability();
        }
    }

    public void generateStudentSchedules(){
        Schedule studentSchedule;
        Random myRand = new Random();

        for(int i = 1; i <= numStudents; i++){
            studentSchedule = new Schedule(i);
            while(studentSchedule.getModuleNumbers().size() < numModulesPerCourse){
                int module = myRand.nextInt(numModulesTotal);
                studentSchedule.addModule(module);
            }
            studentSchedules.add(studentSchedule);
        }
    }
}
