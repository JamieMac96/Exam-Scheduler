package com.macmanus.scheduler;

import javax.swing.*;
import java.util.Scanner;

public class ParameterReader{
    private Scanner scanner;
    private JFrame myFrame;

    ParameterReader(){
        myFrame = new JFrame();
        scanner = new Scanner(System.in);
    }

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

        String input = JOptionPane.showInputDialog(myFrame, message);
        int intInput = Integer.parseInt(input);
        return intInput;
    }

    private int getIntInputA(String message){
        System.out.print(message);
        return scanner.nextInt();
    }
}
