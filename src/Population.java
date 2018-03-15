import com.sun.org.apache.xpath.internal.operations.Or;

import java.util.ArrayList;
import java.util.Collections;
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
            for(int j = 0; j < numModulesTotal; j++){
                ordering.add(j);
            }

            Collections.shuffle(ordering.getOrdering());
            orderings.add(ordering);
        }
    }

    public void sort() {
        if (orderings == null || orderings.size()==0){
            return;
        }
        quicksort(0, orderings.size() - 1);
    }

    private void quicksort(int low, int high) {
        int i = low, j = high;
        int pivot = orderings.get(low + (high-low)/2).getFitnessCost();

        while (i <= j) {
            while (orderings.get(i).getFitnessCost() < pivot) {
                i++;
            }
            while (orderings.get(j).getFitnessCost() > pivot) {
                j--;
            }

            if (i <= j) {
                exchange(i, j);
                i++;
                j--;
            }
        }
        // Recursion
        if (low < j)
            quicksort(low, j);
        if (i < high)
            quicksort(i, high);
    }

    private void exchange(int i, int j) {
        Ordering temp = orderings.get(i);
        orderings.set(i, orderings.get(j));
        orderings.set(j, temp);
    }

    public void setOrderings(List<Ordering> orderings) {
        this.orderings = orderings;
    }

    public List<Ordering> getOrderings() {
        return orderings;
    }

    @Override
    public String toString(){
        StringBuilder output = new StringBuilder("");

        for(int i = 0; i < orderings.size(); i++){
            output.append("Ordering ")
                  .append(Integer.toString(i + 1))
                  .append(": ")
                  .append(orderings.get(i).toString())
                  .append(" Fitness Cost: ")
                  .append(orderings.get(i).getFitnessCost())
                  .append("\n");
        }

        return output.toString();
    }
}
