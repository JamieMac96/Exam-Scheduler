package com.macmanus.scheduler;

import javax.swing.*;

public class ParameterReader{

    int getNumGenerations(){
        return getPositiveIntInput("Enter the number of generations: ");
    }

    int getPopulationSize(){
        return getPositiveIntInput("Enter the population size: ");
    }

    int getNumStudents(){
        return getPositiveIntInput("Enter the number of students: ");
    }

    int getNumModulesTotal(){
        String message = "Enter the total number of modules: ";
        String errorMessage = "Error! there must be atleast 2 modules";
        int numModules = getPositiveIntInput(message);

        while(numModules < 2){
            JOptionPane.showMessageDialog(null, errorMessage);
            numModules = getPositiveIntInput(message);
        }

        return numModules;
    }

    int getNumModulesPerCourse(){
        return getPositiveIntInput("Enter the number of modules for the course: ");
    }

    int getNumDays(){
        return getPositiveIntInput("Enter the number of exam days: ");
    }

    int getCrossoverProbability(){
        String message      = "Enter the crossover probability (0-100): ";
        String errorMessage = "Crossover probability must be in the range" +
                              " (0-100). Try again: ";
        int prob = getPositiveIntInput(message);

        //Ensure valid probability is entered.
        while(prob < 0 || prob > 100){
            prob = getPositiveIntInput(errorMessage);
        }

        return prob;
    }

    int getMutationProbability(){
        String message      = "Enter the mutation probability (0-100): ";
        String errorMessage = "Mutation probability must be in the range" +
                              " (0-100). Try again: ";

        int prob =  getPositiveIntInput(message);

        //Ensure valid probability is entered.
        while(prob < 0 || prob > 100){
            prob = getPositiveIntInput(errorMessage);
        }

        return prob;
    }

    private int getPositiveIntInput(String message){
        String errorMessage = "Error! Could not parse input to positive integer";
        String input = JOptionPane.showInputDialog(null, message);

        while(!tryParseInt(input)){
            JOptionPane.showMessageDialog(null, errorMessage);
            input = JOptionPane.showInputDialog(null, message);
        }

        return Integer.parseInt(input);
    }

    private boolean tryParseInt(String input){
        // Exit the program if cancel is chosen
        if(input == null){
            System.exit(0);
        }

        try {
            int val = Integer.parseInt(input);
            return val >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
