import java.util.ArrayList;
import java.util.List;

public class Ordering{
    private List<Integer> ordering;

    Ordering(){
        ordering = new ArrayList<>();
    }

    void add(int item){
        ordering.add(item);
    }

    int get(int index){
        return ordering.get(index);
    }

    int size(){
        return ordering.size();
    }

    List<Integer> getOrdering(){
        return ordering;
    }

    @Override
    public String toString(){
        return ordering.toString();
    }
}
