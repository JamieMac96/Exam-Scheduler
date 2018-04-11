package com.macmanus.scheduler;

import javax.swing.*;

public class is15147312 {

    public static void main(String [] args){
        // Read in program parameters
        ParameterReader reader = new ParameterReader();
        int numGenerations = reader.getNumGenerations();
        int populationSize = reader.getPopulationSize();
        int numStudents = reader.getNumStudents();
        int numModulesTotal = reader.getNumModulesTotal();
        int numModulesPerCourse = reader.getNumModulesPerCourse();
        int numDays = reader.getNumDays();
        int crossoverProbability = reader.getCrossoverProbability();
        int mutationProbability = reader.getMutationProbability();

        // Validate program parameters
        String modulesError   = "Error! There are less total modules than "
                              + "modules per course";
        String sumError       = "Error! Sum of crossover and mutation probability"
                              + " is grater than 100";
        String crossoverError = "Error! Crossover probability cannot be 100 if "
                              + "there are an even number of modules";
        String popSizeError   = "Error! Population size is too large";

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

        // To ensure that we can generate enough unique orderings for the
        // population we must check that factorial(numModulesTotal) >= populationSize
        while(!(factorialApproximation(numModulesTotal) >= populationSize)){
            JOptionPane.showMessageDialog(null, popSizeError);
            populationSize = reader.getPopulationSize();
        }

        ExamScheduler scheduler = new ExamScheduler
                .Builder()
                .numGenerations(numGenerations)
                .populationSize(populationSize)
                .numStudents(numStudents)
                .numModulesTotal(numModulesTotal)
                .numModulesPerCourse(numModulesPerCourse)
                .numDays(numDays)
                .crossoverProbability(crossoverProbability)
                .mutationProbability(mutationProbability)
                .createScheduler();

        scheduler.run();
    }

    public static double factorialApproximation(int n){
        return Math.sqrt(2.0*Math.PI*n)*Math.pow(n,n)*Math.pow(Math.E, -n);
    }
}