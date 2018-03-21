package com.macmanus.scheduler;

import java.util.*;

public class Evolver {
    private int mutationProbability;
    private int crossoverPrbability;
    private Random rand;


    public Evolver(int mutationProbability, int crossoverPrbability){
        this.mutationProbability = mutationProbability;
        this.crossoverPrbability = crossoverPrbability;
        this.rand = new Random();
    }

    // The new generation is created by applying the GA techniques to
    // the orderings of the current generation and adding the result to
    // the "newOrderings" arraylist
    public Population getNextGeneration(Population currentGeneration){
        List<Ordering> filteredOrderings = filterOrderings(currentGeneration);
        List<Ordering> newOrderings = new ArrayList<>();
        Population nextGeneration = new Population();

        // When we apply a technique to the filtered orderings we
        while(filteredOrderings.size() > 0){
            GaTechnique technique = chooseTechnique();
            applyTechnique(technique, filteredOrderings, newOrderings);
        }

        nextGeneration.setOrderings(newOrderings);

        return nextGeneration;
    }

    // We first divide the current orderings into three sets/categories:
    // Best, average and worst.
    // We filter the orderings by adding the best set of
    // orderings twice and then adding the second best set of orderings once
    public List<Ordering> filterOrderings(Population currentPopulation){
        List<Ordering> currentOrderings = currentPopulation.getOrderings();
        List<Ordering> filteredOrderings = new ArrayList<>();
        int size = currentOrderings.size();
        int setSize = (int)Math.round(((double)size / 3));
        int altSetSize = size - (2 * setSize);

        for(int i = 0; i < setSize; i++){
            filteredOrderings.add(currentOrderings.get(i));
        }
        for(int i = setSize; i < setSize + altSetSize; i++){
            filteredOrderings.add(currentOrderings.get(i));
        }
        for(int i = 0; i < setSize; i++){
            filteredOrderings.add(currentOrderings.get(i));
        }

        return filteredOrderings;
    }

    public void applyCrossover(List<Ordering> filteredOrderings,
                               List<Ordering> newOrderings){
        if(filteredOrderings.size() < 2) return;

        int indexOne = rand.nextInt(filteredOrderings.size());
        int indexTwo = indexOne;
        while(indexTwo == indexOne){
            indexTwo = rand.nextInt(filteredOrderings.size());
        }

        Ordering parentOne = filteredOrderings.get(indexOne);
        Ordering parentTwo = filteredOrderings.get(indexTwo);

        Ordering childOne = getChildOrdering(parentOne, parentTwo);
        Ordering childTwo = getChildOrdering(parentTwo, parentOne);

        newOrderings.add(childOne);
        newOrderings.add(childTwo);

        filteredOrderings.remove(Math.max(indexOne, indexTwo));
        filteredOrderings.remove(Math.min(indexOne, indexTwo));
    }

    public void applyMutation(List<Ordering> filteredOrderings,
                              List<Ordering> newOrderings){
        int randomIndex = rand.nextInt(filteredOrderings.size());
        Ordering orderingToMutate = filteredOrderings.get(randomIndex);

        // Select two random items from the ordering and swap their positions
        int indexOne = rand.nextInt(orderingToMutate.size());
        int indexTwo = rand.nextInt(orderingToMutate.size());
        int valueOne = orderingToMutate.get(indexOne);
        int valueTwo = orderingToMutate.get(indexTwo);
        orderingToMutate.set(indexOne, valueTwo);
        orderingToMutate.set(indexTwo, valueOne);

        newOrderings.add(orderingToMutate);
        filteredOrderings.remove(randomIndex);

    }

    public void applyReproduction(List<Ordering> filteredOrderings,
                                   List<Ordering> newOrderings){
        int randomIndex = rand.nextInt(filteredOrderings.size());

        newOrderings.add(filteredOrderings.get(randomIndex));
        filteredOrderings.remove(randomIndex);
    }

    private GaTechnique chooseTechnique(){
        GaTechnique selectedTechnique;
        Random random = new Random();
        int randomPercentage = random.nextInt(100);

        if(randomPercentage <= mutationProbability){
            selectedTechnique = GaTechnique.MUTATION;
        }
        else if(randomPercentage <=
                (mutationProbability + crossoverPrbability)){
            selectedTechnique = GaTechnique.CROSSOVER;
        }
        else{
            selectedTechnique = GaTechnique.REPRODUCTION;
        }

        return selectedTechnique;
    }

    private void applyTechnique(GaTechnique technique,
                                List<Ordering> filteredOrderings,
                                List<Ordering> newOrderings){
        if(technique == GaTechnique.MUTATION){
            applyMutation(filteredOrderings, newOrderings);
        }
        else if(technique == GaTechnique.CROSSOVER){
            applyCrossover(filteredOrderings, newOrderings);
        }
        else{
            applyReproduction(filteredOrderings, newOrderings);
        }
    }

    private Ordering getChildOrdering(Ordering parentOne,
                                      Ordering parentTwo){
        Ordering child = new Ordering(parentOne.getNumDays());
        int orderingSize = parentOne.size();
        int cutPoint = rand.nextInt(orderingSize - 4) + 2;

        List<Integer> requiredItems = new ArrayList<>(parentOne.getOrdering());
        List<Integer> preCutoff = getSubOrdering(parentOne,0, cutPoint);
        List<Integer> postCutoff = getSubOrdering(parentTwo,
                                                  cutPoint,
                                                  orderingSize);

        requiredItems.removeAll(preCutoff);
        requiredItems.removeAll(postCutoff);

        for(int i = 0; i < preCutoff.size(); i++){
            if(child.contains(preCutoff.get(i))){
                child.add(requiredItems.remove(0));
            }
            else{
                child.add(preCutoff.get(i));
            }
        }

        for(int i = 0; i < postCutoff.size(); i++){
            if(child.contains(postCutoff.get(i))){
                child.add(requiredItems.remove(0));
            }
            else{
                child.add(postCutoff.get(i));
            }
        }

        return child;
    }

    private List<Integer> getSubOrdering(Ordering list,
                                         int start,
                                         int end){
        List<Integer> subList = new ArrayList<>();

        for(int i = start; i < end; i++){
            subList.add(list.get(i));
        }

        return subList;
    }
}
