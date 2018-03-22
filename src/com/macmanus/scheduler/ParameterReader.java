package com.macmanus.scheduler;

import javax.swing.*;
import java.util.Scanner;

public class ParameterReader{

    int getNumGenerations(){
        return getIntInput("Enter the number of generations: ");
    }

    int getPopulationSize(){
        return getIntInput("Enter the population size: ");
    }

    int getNumStudents(){
        return getIntInput("Enter the number of students: ");
    }

    int getNumModulesTotal(){
        return getIntInput("Enter the total number of modules: ");
    }

    int getNumModulesPerCourse(){
        return getIntInput("Enter the number of modules for the course: ");
    }

    int getNumDays(){
        return getIntInput("Enter the number of exam days: ");
    }

    int getCrossoverProbability(){
        int prob = getIntInput("Enter the crossover probability (0-100): ");

        //Ensure valid probability is entered.
        while(prob < 0 || prob > 100){
            prob = getIntInput("Crossover probability must be in the range" +
                    " (0-100). Try again: ");
        }

        return prob;
    }

    int getMutationProbability(){
        int prob =  getIntInput("Enter the mutation probability (1-100): ");

        //Ensure valid probability is entered.
        while(prob < 0 || prob > 100){
            prob = getIntInput("Mutation probability must be in the range" +
                    " (0-100). Try again: ");
        }

        return prob;
    }

    private int getIntInput(String message){
        String errorMessage = "Error! Could not parse input to integer";
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
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
