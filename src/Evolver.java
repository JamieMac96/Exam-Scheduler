import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Evolver {
    private int mutationProbability;
    private int crossoverPrbability;


    public Evolver(int mutationProbability, int crossoverPrbability){
        this.mutationProbability = mutationProbability;
        this.crossoverPrbability = crossoverPrbability;
    }

    public enum GaTechnique{
        MUTATION,
        CROSSOVER,
        REPRODUCTION
    }

    // The new generation is created by applying the GA techniques to
    // the orderings of the current generation and adding the result to
    // the "newOrderings" arraylist
    public Population getNextGeneration(Population currentGeneration){
        List<Ordering> newOrderings = new ArrayList<>();
        Population nextGeneration = new Population();

        currentGeneration.sort();
        List<Ordering> filteredOrderings = filterOrderings(currentGeneration);

        for(int i = 0; i < currentGeneration.size(); i++){
            GaTechnique technique = chooseTechnique();
            Ordering currentOrdering = currentGeneration.get(i);

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

        nextGeneration.setOrderings(newOrderings);

        return nextGeneration;
    }

    // We first divide the current orderings into three sets/categories:
    // Best, average and worst.
    // We filter the orderings by adding the best set of
    // orderings twice and then adding the second best set of orderings once
    private List<Ordering> filterOrderings(Population currentPopulation){
        List<Ordering> currentOrderings = currentPopulation.getOrderings();
        List<Ordering> filteredOrderings = new ArrayList<>();
        int size = currentOrderings.size();
        int setSize = (int)Math.round(((double)size / 3));
        // The average set may
        int altSetSize = size - (2 * setSize);

        for(int i = 0; i < setSize; i++){
            filteredOrderings.add(currentOrderings.get(i));
        }
        for(int i = 0; i < setSize; i++){
            filteredOrderings.add(currentOrderings.get(i));
        }
        for(int i = setSize; i < setSize + altSetSize; i++){
            filteredOrderings.add(currentOrderings.get(i));
        }

        return filteredOrderings;
    }

    private void applyCrossover(List<Ordering> filteredOrderings,
                                List<Ordering> newOrderings){

    }

    private void applyMutation(List<Ordering> filteredOrderings,
                               List<Ordering> newOrderings){

    }

    private void applyReproduction(List<Ordering> filteredOrderings,
                                   List<Ordering> newOrderings){

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
}
