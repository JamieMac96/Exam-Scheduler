package com.macmanus.scheduler;

import java.util.*;

public class Population{
    private List<Ordering> orderings;

    public Population(){
        orderings = new ArrayList<>();
    }

    public List<Ordering> getOrderings(){
        return orderings;
    }

    public void setOrderings(List<Ordering> orderings){
        this.orderings = orderings;
    }

    public void sort(){
        orderings.sort(Comparator.comparing(Ordering::getFitnessCost));
    }

    public void calculateCosts(List<Schedule> studentSchedules){
        for(Ordering ordering: orderings){
            ordering.calculateFitnessCost(studentSchedules);
        }
    }

    public void randomlyGenerate(int numDays,
                                 int populationSize,
                                 int numModulesTotal){

        // Populate a set to ensure that the initial population has
        // unique orderings
        Set<Ordering> initialPop = new HashSet<>();

        while(initialPop.size() < populationSize){
            Ordering ordering = new Ordering(numDays);
            for(int j = 1; j <= numModulesTotal; j++){
                ordering.add(j);
            }

            Collections.shuffle(ordering.getOrdering());
            initialPop.add(ordering);
        }

        orderings = new ArrayList<>(initialPop);
    }

    @Override
    public String toString(){
        StringBuilder output = new StringBuilder("");

        for(int i = 0; i < orderings.size(); i++){
            output.append("Ordering ")
                  .append(Integer.toString(i + 1))
                  .append(": ");

            output.append(orderings.get(i).toString());
            output.append("Fitness Cost: ")
                  .append(orderings.get(i).getFitnessCost())
                  .append("\n");
        }

        return output.toString();
    }
}
