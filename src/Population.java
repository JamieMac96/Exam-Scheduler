import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Population{
    private List<Ordering> orderings;
    private int populationSize;
    private int numModulesTotal;

    Population(int populationSize, int numModulesTotal){
        orderings = new ArrayList<>();
        this.populationSize = populationSize;
        this.numModulesTotal = numModulesTotal;
    }

    public void generate(){
        for(int i = 0; i < populationSize; i++){
            Ordering ordering = new Ordering();
            for(int j = 0; j < numModulesTotal; j++){
                ordering.add(j);
            }

            Collections.shuffle(ordering.getOrdering());
            orderings.add(ordering);
        }
    }

    public Ordering getOrdering(int index){
        return orderings.get(index);
    }

    @Override
    public String toString(){
        StringBuilder output = new StringBuilder("");

        for(int i = 0; i < orderings.size(); i++){
            output.append("Ordering ")
                  .append(Integer.toString(i + 1))
                  .append(" ")
                  .append(orderings.get(i).toString())
                  .append("\n");
        }

        return output.toString();
    }
}
