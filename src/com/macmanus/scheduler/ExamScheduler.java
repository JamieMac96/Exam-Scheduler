package com.macmanus.scheduler;

import javax.swing.*;
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
        EvolutionHandler eHandler = new EvolutionHandler(mutationProbability,
                                                         crossoverProbability);
        Population population = new Population();
        initializeParameters();
        generateStudentSchedules();
        printStudentSchedules();

        population.randomlyGenerate(numDays, populationSize, numModulesTotal);
        population.calculateCosts(studentSchedules);
        population.sort();
        System.out.println("\nPopulation\n" + population);


        for(int i = 0; i < numGenerations; i++){
            population = eHandler.getNextGeneration(population);
            population.calculateCosts(studentSchedules);
            population.sort();
            System.out.println(population);
        }
    }

    private void initializeParameters(){
        ParameterReader reader = new ParameterReader();

        numGenerations = reader.getNumGenerations();
        populationSize = reader.getPopulationSize();
        numStudents = reader.getNumStudents();
        numModulesTotal = reader.getNumModulesTotal();
        numModulesPerCourse = reader.getNumModulesPerCourse();
        numDays = reader.getNumDays();
        crossoverProbability = reader.getCrossoverProbability();
        mutationProbability = reader.getMutationProbability();

        validateParameters(reader);
    }

    private void generateStudentSchedules(){
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

    private void printStudentSchedules(){
        for (Schedule studentSchedule : studentSchedules) {
            System.out.println(studentSchedule);
        }
    }

    private void validateParameters(ParameterReader reader){
        String modulesError   = "Error! There are less total modules than "
                              + "modules per course";
        String sumError       = "Sum of crossover and mutation probability"
                              + " is grater than 100";
        String crossoverError = "Crossover probability cannot be 100 if "
                              + "there are an even number of modules";


        while(!(numModulesTotal > numModulesPerCourse)){
            JOptionPane.showMessageDialog(null, modulesError);
            numModulesTotal = reader.getNumModulesTotal();
            numModulesPerCourse = reader.getNumModulesPerCourse();
        }

        while(crossoverProbability + mutationProbability > 100){
            JOptionPane.showMessageDialog(null, sumError);
            crossoverProbability = reader.getCrossoverProbability();
            mutationProbability = reader.getMutationProbability();
        }

        if((numModulesTotal % 2) != 0){
            while(crossoverProbability == 100){
                JOptionPane.showMessageDialog(null, crossoverError);
                crossoverProbability = reader.getCrossoverProbability();
            }
        }
    }
}
