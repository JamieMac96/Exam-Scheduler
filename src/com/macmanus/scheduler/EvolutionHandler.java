package com.macmanus.scheduler;

import java.util.*;

public class EvolutionHandler {
    private int mutationProbability;
    private int crossoverProbability;
    private Random rand;


    public EvolutionHandler(int mutationProbability, int crossoverProbability){
        this.mutationProbability  = mutationProbability;
        this.crossoverProbability = crossoverProbability;
        this.rand = new Random();
    }

    // The new generation is created by applying the GA techniques to
    // the orderings of the current generation and adding the result to
    // the "newOrderings" ArrayList
    public Population getNextGeneration(Population currentGeneration){
        List<Ordering> newOrderings = new ArrayList<>();
        Population nextGeneration   = new Population();
        List<Ordering> filteredOrderings = filterOrderings(currentGeneration);

        // When we apply a technique to the filtered orderings we remove
        // those orderings from the list.
        while(filteredOrderings.size() > 0){
            GaTechnique technique = chooseTechnique();
            applyTechnique(technique, filteredOrderings, newOrderings);
        }

        nextGeneration.setOrderings(newOrderings);

        return nextGeneration;
    }

    private List<Ordering> filterOrderings(Population currentPopulation){
        List<Ordering> currentOrderings  = currentPopulation.getOrderings();
        int size = currentOrderings.size();
        int setSize = (int)Math.round(((double)size / 3));
        int altSetSize = size - (2 * setSize);

        return getFilteredOrderings(currentOrderings, setSize, altSetSize);
    }

    private GaTechnique chooseTechnique(){
        GaTechnique selectedTechnique;
        Random random = new Random();
        int randomPercentage = random.nextInt(100);

        if(randomPercentage <= mutationProbability){
            selectedTechnique = GaTechnique.MUTATION;
        }
        else if(randomPercentage <=
                (mutationProbability + crossoverProbability)){
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

    private void applyMutation(List<Ordering> filteredOrderings,
                               List<Ordering> newOrderings){
        int chosenIndex = rand.nextInt(filteredOrderings.size());
        Ordering orderingToMutate = filteredOrderings.get(chosenIndex);

        // Select two distinct random items from the ordering and
        // swap their positions
        int indexOne = rand.nextInt(orderingToMutate.size());
        int indexTwo;
        while((indexTwo = rand.nextInt(orderingToMutate.size())) == indexOne);

        int valueOne = orderingToMutate.get(indexOne);
        int valueTwo = orderingToMutate.get(indexTwo);

        orderingToMutate.set(indexOne, valueTwo);
        orderingToMutate.set(indexTwo, valueOne);

        newOrderings.add(orderingToMutate);
        filteredOrderings.remove(chosenIndex);

    }

    private void applyCrossover(List<Ordering> filteredOrderings,
                                List<Ordering> newOrderings){
        if(filteredOrderings.size() < 2) return;

        int indexOne = rand.nextInt(filteredOrderings.size());
        int indexTwo;
        while((indexTwo = rand.nextInt(filteredOrderings.size())) == indexOne);

        Ordering parentOne = filteredOrderings.get(indexOne);
        Ordering parentTwo = filteredOrderings.get(indexTwo);

        int cutPoint = getCutPoint(parentOne);

        Ordering childOne  = getChildOrdering(parentOne, parentTwo, cutPoint);
        Ordering childTwo  = getChildOrdering(parentTwo, parentOne, cutPoint);

        newOrderings.add(childOne);
        newOrderings.add(childTwo);

        filteredOrderings.remove(Math.max(indexOne, indexTwo));
        filteredOrderings.remove(Math.min(indexOne, indexTwo));
    }

    private void applyReproduction(List<Ordering> filteredOrderings,
                                   List<Ordering> newOrderings){
        int randomIndex = rand.nextInt(filteredOrderings.size());

        newOrderings.add(filteredOrderings.get(randomIndex));
        filteredOrderings.remove(randomIndex);
    }

    private Ordering getChildOrdering(Ordering parentOne,
                                      Ordering parentTwo,
                                      int cutPoint){
        Ordering child = new Ordering(parentOne.getNumDays());
        int numModules = parentOne.size();

        List<Integer> requiredItems = new ArrayList<>(parentOne.getOrdering());
        List<Integer> preCutoff = getSubOrdering(parentOne,0, cutPoint);
        List<Integer> postCutoff = getSubOrdering(parentTwo,
                                                  cutPoint,
                                                  numModules);

        requiredItems.removeAll(preCutoff);
        requiredItems.removeAll(postCutoff);

        for(Integer aPreCutoff : preCutoff){
            child.add(aPreCutoff);
        }

        // Remove duplicate modules and replace them with
        // the modules that are not yet a part of the child ordering
        for(Integer aPostCutoff : postCutoff){
            if(child.contains(aPostCutoff)){
                child.add(requiredItems.remove(0));
            }
            else{
                child.add(aPostCutoff);
            }
        }

        return child;
    }

    private List<Ordering> getFilteredOrderings(List<Ordering> currentOrderings,
                                                int setSize,
                                                int altSetSize){
        List<Ordering> filteredOrderings = new ArrayList<>();

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

    private List<Integer> getSubOrdering(Ordering list,
                                         int start,
                                         int end){
        List<Integer> subList = new ArrayList<>();

        for(int i = start; i < end; i++){
            subList.add(list.get(i));
        }

        return subList;
    }

    private int getCutPoint(Ordering ordering){
        int numModules = ordering.size();
        int cutPoint;

        // If the number of modules is less than 4 then we have no range
        // to work with and just choose the middle as the cutpoint.
        if(numModules < 4){
            return numModules / 2;
        }

        return rand.nextInt(numModules - 2) + 1;
    }
}
