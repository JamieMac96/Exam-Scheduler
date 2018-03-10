import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Evaluator {

    private List<Schedule> studentSchedules;
    private int numModulesTotal;
    private int numDays;

    public Evaluator(List<Schedule> studentSchedules,
                     int numExams,
                     int numDays){
        this.numModulesTotal = numExams;
        this.numDays = numDays;
        this.studentSchedules = studentSchedules;
    }

    public int getFitnessCost(Ordering ordering){
        int cost = 0;
        int examsPerSession = (int) Math.ceil((double) numModulesTotal / numDays);

        List<Set<Integer>> examDays = getExamDays(ordering, examsPerSession);

        System.out.println(examDays);

        for(int i = 0; i < examDays.size(); i++){
            Set<Integer> currentExams = examDays.get(i);
            for(int j = 0; j < studentSchedules.size(); j++){
                Set<Integer> schedule = studentSchedules.get(j).getModuleNumbers();
                Set<Integer> intersection = intersect(schedule, currentExams);
                if(intersection.size() > 1){
                    cost ++;
                }
            }
        }

        System.out.println(cost);
        return cost;
    }

    private List<Set<Integer>> getExamDays(Ordering ordering,
                                           int examsPerSession){
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

    private Set<Integer> intersect(Set<Integer> a, Set<Integer> b){
        Set<Integer> intersect = new HashSet<>(a);
        intersect.retainAll(b);

        return intersect;
    }
}
