package iterim;


import javax.swing.*;
import java.util.*;

public class is15147312 {

    public static void main(String [] args){
        ExamScheduler scheduler = new ExamScheduler();
        scheduler.run();
    }
}


class ExamScheduler{
    private int numGenerations;
    private int populationSize;
    private int numStudents;
    private int numModulesTotal;
    private int numModulesPerCourse;
    private int numDays;
    private int crossoverProbability;
    private int mutationProbability;
    private List<Schedule> studentSchedules;

    public ExamScheduler(){
        studentSchedules = new ArrayList<>();
    }

    // This method will be used to control the rest of the program
    public void run(){
        Population population = new Population();
        Scanner scanner       = new Scanner(System.in);

        initializeParameters();
        generateStudentSchedules();
        printStudentSchedules();
        scanner.nextLine();

        population.randomlyGenerate(numDays, populationSize, numModulesTotal);
        population.calculateCosts(studentSchedules);
        population.sort();
        System.out.println("\nPopulation\n\n" + population);
    }

    private void initializeParameters(){
        ParameterReader reader = new ParameterReader();

        numGenerations       = reader.getNumGenerations();
        populationSize       = reader.getPopulationSize();
        numStudents          = reader.getNumStudents();
        numModulesTotal      = reader.getNumModulesTotal();
        numModulesPerCourse  = reader.getNumModulesPerCourse();
        numDays              = reader.getNumDays();
        crossoverProbability = reader.getCrossoverProbability();
        mutationProbability  = reader.getMutationProbability();

        validateParameters(reader);
    }

    private void generateStudentSchedules(){
        Schedule studentSchedule;
        Random myRand = new Random();

        for(int i = 1; i <= numStudents; i++){
            studentSchedule = new Schedule(i);
            while(studentSchedule.getModuleNumbers().size() < numModulesPerCourse){
                int module = myRand.nextInt(numModulesTotal) + 1;
                studentSchedule.addModule(module);
            }
            studentSchedules.add(studentSchedule);
        }
    }

    private void printStudentSchedules(){
        for (Schedule studentSchedule : studentSchedules) {
            System.out.println(studentSchedule);
        }
    }

    private void validateParameters(ParameterReader reader){
        String modulesError   = "Error! There must be more total modules than"
                              + " modules per course";
        String sumError       = "Sum of crossover and mutation probability"
                              + " is grater than 100";
        String crossoverError = "Crossover probability cannot be 100 if"
                              + " there are an even number of modules";


        while(!(numModulesTotal > numModulesPerCourse)){
            JOptionPane.showMessageDialog(null, modulesError);
            numModulesTotal     = reader.getNumModulesTotal();
            numModulesPerCourse = reader.getNumModulesPerCourse();
        }

        while(crossoverProbability + mutationProbability > 100){
            JOptionPane.showMessageDialog(null, sumError);
            crossoverProbability = reader.getCrossoverProbability();
            mutationProbability  = reader.getMutationProbability();
        }

        if((numModulesTotal % 2) != 0){
            while(crossoverProbability == 100){
                JOptionPane.showMessageDialog(null, crossoverError);
                crossoverProbability = reader.getCrossoverProbability();
            }
        }
    }
}

class ParameterReader{

    int getNumGenerations(){
        return getIntInput("Enter the number of generations: ");
    }

    int getPopulationSize(){
        return getIntInput("Enter the population size: ");
    }

    int getNumStudents(){
        return getIntInput("Enter the number of students: ");
    }

    int getNumModulesTotal(){
        return getIntInput("Enter the total number of modules: ");
    }

    int getNumModulesPerCourse(){
        return getIntInput("Enter the number of modules for the course: ");
    }

    int getNumDays(){
        return getIntInput("Enter the number of exam days: ");
    }

    int getCrossoverProbability(){
        int prob = getIntInput("Enter the crossover probability (0-100): ");

        //Ensure valid probability is entered.
        while(prob < 0 || prob > 100){
            prob = getIntInput("Crossover probability must be in the range" +
                    " (0-100). Try again: ");
        }

        return prob;
    }

    int getMutationProbability(){
        int prob =  getIntInput("Enter the mutation probability (1-100): ");

        //Ensure valid probability is entered.
        while(prob < 0 || prob > 100){
            prob = getIntInput("Mutation probability must be in the range" +
                    " (0-100). Try again: ");
        }

        return prob;
    }

    private int getIntInput(String message){
        String input = JOptionPane.showInputDialog(null, message);
        String errorMessage = "Error! Could not parse input to integer";

        while(!tryParseInt(input)){
            JOptionPane.showMessageDialog(null, errorMessage);
            input = JOptionPane.showInputDialog(null, message);
        }

        return Integer.parseInt(input);
    }

    private boolean tryParseInt(String input){
        // Exit the program if cancel is chosen
        if(input == null) System.exit(0);

        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

class Schedule{
    private final int studentNum;
    private Set<Integer> moduleNumbers;

    public Schedule(int studentNum){
        this.studentNum = studentNum;
        moduleNumbers = new HashSet<>();
    }

    public Set<Integer> getModuleNumbers() {
        return moduleNumbers;
    }

    public void addModule(int module){
        moduleNumbers.add(module);
    }

    @Override
    public String toString(){
        String initial = "Student: " + studentNum + ": ";
        StringBuilder output = new StringBuilder();

        output.append(initial);

        for(Integer moduleNumber: moduleNumbers){
            output.append(moduleNumber);
            output.append(" ");
        }

        return output.toString();
    }
}

class Ordering{
    private int numDays;
    private int fitnessCost;
    private List<Integer> ordering;

    public Ordering(int days){
        numDays = days;
        ordering = new ArrayList<>();
    }

    public int getFitnessCost() {
        return fitnessCost;
    }

    public void add(int item){
        ordering.add(item);
    }

    public void set(int index, int value){
        ordering.set(index, value);
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

        for(Set<Integer> currentExams : examSessions){
            for(Schedule studentSchedule : studentSchedules){
                Set<Integer> schedule = studentSchedule.getModuleNumbers();
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
        List<Set<Integer>> examDays = new ArrayList<>();
        Set<Integer> buffer         = new HashSet<>();

        for(int i = 0; i < ordering.size(); i++){
            buffer.add(ordering.get(i));
            if((i+1) % examsPerSession == 0){
                examDays.add(new HashSet<>(buffer));
                buffer.clear();
            }
        }

        //Add remaining days to the List
        if(buffer.size() > 0) {
            examDays.add(new HashSet<>(buffer));
        }

        return examDays;
    }
}

class Population{
    private List<Ordering> orderings;

    public Population(){
        orderings = new ArrayList<>();
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
        for(int i = 0; i < populationSize; i++){
            Ordering ordering = new Ordering(numDays);
            for(int j = 1; j <= numModulesTotal; j++){
                ordering.add(j);
            }

            Collections.shuffle(ordering.getOrdering());
            orderings.add(ordering);
        }
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
