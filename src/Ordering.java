import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Ordering{
    private int numDays;
    private int fitnessCost;
    private List<Integer> ordering;
    private boolean processed;

    public Ordering(int days){
        numDays = days;
        ordering = new ArrayList<>();
        processed = false;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public int getFitnessCost() {
        return fitnessCost;
    }

    public void add(int item){
        ordering.add(item);
    }

    public int get(int index){
        return ordering.get(index);
    }

    public int size(){
        return ordering.size();
    }

    List<Integer> getOrdering(){
        return ordering;
    }

    public Ordering calculateFitnessCost(List<Schedule> studentSchedules){
        int cost = 0;
        int examsPerSession = (int) Math.ceil((double) this.size() / numDays);
        List<Set<Integer>> examSessions = getExamSessions(examsPerSession);

        for(int i = 0; i < examSessions.size(); i++){
            Set<Integer> currentExams = examSessions.get(i);
            for(int j = 0; j < studentSchedules.size(); j++){
                Set<Integer> schedule = studentSchedules.get(j).getModuleNumbers();
                Set<Integer> intersection = intersect(schedule, currentExams);
                if(intersection.size() > 1){
                    cost++;
                }
            }
        }

        this.fitnessCost = cost;

        return this;
    }

    @Override
    public String toString(){
        return ordering.toString();
    }

    private Set<Integer> intersect(Set<Integer> a, Set<Integer> b){
        Set<Integer> intersect = new HashSet<>(a);
        intersect.retainAll(b);

        return intersect;
    }

    private List<Set<Integer>> getExamSessions(int examsPerSession){
        // The overall schedule of the exams(not assoc. with students).
        List<Set<Integer>> examDays = new ArrayList<>();
        Set<Integer> buffer = new HashSet<>();


        for(int i = 0; i < ordering.size(); i++){
            buffer.add(ordering.get(i));
            if((i+1) % examsPerSession == 0){
                examDays.add(new HashSet<>(buffer));
                buffer.clear();
            }
        }

        if(buffer.size() > 0) {
            //Add remaining days to the List
            examDays.add(new HashSet<>(buffer));
        }

        return examDays;
    }
}
