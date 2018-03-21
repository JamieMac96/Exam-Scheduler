package com.macmanus.scheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Population{
    private List<Ordering> orderings;

    public Population(){
        orderings = new ArrayList<>();
    }

    public void add(Ordering ordering){
        orderings.add(ordering);
    }

    public int size(){
        return orderings.size();
    }

    public Ordering get(int index){
        return orderings.get(index);
    }

    public void randomlyGenerate(int numDays,
                                 int populationSize,
                                 int numModulesTotal){
        for(int i = 0; i < populationSize; i++){
            Ordering ordering = new Ordering(numDays);
            for(int j = 1; j <= numModulesTotal; j++){
                ordering.add(j);
            }

            Collections.shuffle(ordering.getOrdering());
            orderings.add(ordering);
        }
    }

    public void calculateCosts(List<Schedule> studentSchedules){
        for(Ordering ordering: orderings){
            ordering.calculateFitnessCost(studentSchedules);
        }
    }

    public void setOrderings(List<Ordering> orderings) {
        this.orderings = orderings;
    }

    public List<Ordering> getOrderings() {
        return orderings;
    }

    public void sort(){
        orderings.sort(Comparator.comparing(Ordering::getFitnessCost));
    }

    @Override
    public String toString(){
        StringBuilder output = new StringBuilder("");

        for(int i = 0; i < orderings.size(); i++){
            output.append("Ordering ")
                  .append(Integer.toString(i + 1))
                  .append(":");

            for(Integer item: orderings.get(i).getOrdering()){
                output.append(" ")
                      .append(item);
            }
            output.append(" : Fitness Cost: ")
                  .append(orderings.get(i).getFitnessCost())
                  .append("\n");
        }

        return output.toString();
    }
}
