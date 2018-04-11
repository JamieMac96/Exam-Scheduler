package finalsubmission;

import javax.swing.*;
import java.util.*;

public class is15147312 {

    public static void main(String [] args){
        // Read in program parameters
        ParameterReader reader = new ParameterReader();
        int numGenerations = reader.getNumGenerations();
        int populationSize = reader.getPopulationSize();
        int numStudents = reader.getNumStudents();
        int numModulesTotal = reader.getNumModulesTotal();
        int numModulesPerCourse = reader.getNumModulesPerCourse();
        int numDays = reader.getNumDays();
        int crossoverProbability = reader.getCrossoverProbability();
        int mutationProbability = reader.getMutationProbability();

        // Validate program parameters
        String modulesError   = "Error! There are less total modules than "
                              + "modules per course";
        String sumError       = "Error! Sum of crossover and mutation probability"
                              + " is grater than 100";
        String crossoverError = "Error! Crossover probability cannot be 100 if "
                              + "there are an even number of modules";
        String popSizeError   = "Error! Population size is too large";

        while(!(numModulesTotal > numModulesPerCourse)){
            JOptionPane.showMessageDialog(null, modulesError);
            numModulesTotal = reader.getNumModulesTotal();
            numModulesPerCourse = reader.getNumModulesPerCourse();
        }

        while(crossoverProbability + mutationProbability > 100){
            JOptionPane.showMessageDialog(null, sumError);
            crossoverProbability = reader.getCrossoverProbability();
            mutationProbability = reader.getMutationProbability();
        }

        if((numModulesTotal % 2) != 0){
            while(crossoverProbability == 100){
                JOptionPane.showMessageDialog(null, crossoverError);
                crossoverProbability = reader.getCrossoverProbability();
            }
        }

        // To ensure that we can generate enough unique orderings for the
        // population we must check that factorial(numModulesTotal) >= populationSize
        while(!(factorialApproximation(numModulesTotal) >= populationSize)){
            JOptionPane.showMessageDialog(null, popSizeError);
            populationSize = reader.getPopulationSize();
        }


        // Create and run the scheduler
        ExamScheduler scheduler = new ExamScheduler
                .Builder()
                .numGenerations(numGenerations)
                .populationSize(populationSize)
                .numStudents(numStudents)
                .numModulesTotal(numModulesTotal)
                .numModulesPerCourse(numModulesPerCourse)
                .numDays(numDays)
                .crossoverProbability(crossoverProbability)
                .mutationProbability(mutationProbability)
                .createScheduler();

        scheduler.run();
    }

    private static double factorialApproximation(int n){
        return Math.sqrt(2.0*Math.PI*n)*Math.pow(n,n)*Math.pow(Math.E, -n);
    }
}

class ParameterReader{

    public int getNumGenerations(){
        return getPositiveIntInput("Enter the number of generations: ");
    }

    public int getPopulationSize(){
        return getPositiveIntInput("Enter the population size: ");
    }

    public int getNumStudents(){
        return getPositiveIntInput("Enter the number of students: ");
    }

    public int getNumModulesTotal(){
        String message      = "Enter the total number of modules: ";
        String errorMessage = "Error! there must be atleast 2 modules";

        int numModules = getPositiveIntInput(message);

        while(numModules < 2){
            JOptionPane.showMessageDialog(null, errorMessage);
            numModules = getPositiveIntInput(message);
        }

        return numModules;
    }

    public int getNumModulesPerCourse(){
        return getPositiveIntInput("Enter the number of modules for the course: ");
    }

    public int getNumDays(){
        return getPositiveIntInput("Enter the number of exam days: ");
    }

    public int getCrossoverProbability(){
        String message      = "Enter the crossover probability (0-100): ";
        String errorMessage = "Crossover probability must be in the range" +
                              " (0-100). Try again: ";

        int prob = getPositiveIntInput(message);

        //Ensure valid probability is entered.
        while(prob < 0 || prob > 100){
            prob = getPositiveIntInput(errorMessage);
        }

        return prob;
    }

    public int getMutationProbability(){
        String message      = "Enter the mutation probability (0-100): ";
        String errorMessage = "Mutation probability must be in the range" +
                " (0-100). Try again: ";

        int prob =  getPositiveIntInput(message);

        //Ensure valid probability is entered.
        while(prob < 0 || prob > 100){
            prob = getPositiveIntInput(errorMessage);
        }

        return prob;
    }

    private int getPositiveIntInput(String message){
        String errorMessage = "Error! Could not parse input to positive integer";
        String input = JOptionPane.showInputDialog(null, message);

        while(!tryParseInt(input)){
            JOptionPane.showMessageDialog(null, errorMessage);
            input = JOptionPane.showInputDialog(null, message);
        }

        return Integer.parseInt(input);
    }

    private boolean tryParseInt(String input){
        // Exit the program if cancel is chosen
        if(input == null){
            System.exit(0);
        }

        try {
            int val = Integer.parseInt(input);
            return val >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
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

    private ExamScheduler(int numGenerations,
                          int populationSize,
                          int numStudents,
                          int numModulesTotal,
                          int numModulesPerCourse,
                          int numDays,
                          int crossoverProbability,
                          int mutationProbability,
                          List<Schedule> studentSchedules){
        this.numGenerations = numGenerations;
        this.populationSize = populationSize;
        this.numStudents = numStudents;
        this.numModulesTotal = numModulesTotal;
        this.numModulesPerCourse = numModulesPerCourse;
        this.numDays = numDays;
        this.crossoverProbability = crossoverProbability;
        this.mutationProbability  = mutationProbability;
        this.studentSchedules = studentSchedules;
    }

    // This method will be used to control the rest of the program
    public void run(){
        Scanner scanner           = new Scanner(System.in);
        Population population     = new Population();
        EvolutionHandler eHandler = new EvolutionHandler(mutationProbability,
                                                         crossoverProbability);

        generateStudentSchedules();
        printStudentSchedules();

        population.randomlyGenerate(numDays, populationSize, numModulesTotal);
        population.calculateCosts(studentSchedules);
        population.sort();

        scanner.nextLine();

        for(int i = 0; i <= numGenerations; i++){
            System.out.println("Generation " + (i));
            System.out.println(population);
            System.out.println("-------------------------------------------");
            population = eHandler.getNextGeneration(population);
            population.calculateCosts(studentSchedules);
            population.sort();
        }
    }

    private void generateStudentSchedules(){
        studentSchedules = new ArrayList<>();

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

    public static class Builder{
        private int numGenerations;
        private int populationSize;
        private int numStudents;
        private int numModulesTotal;
        private int numModulesPerCourse;
        private int numDays;
        private int crossoverProbability;
        private int mutationProbability;
        private List<Schedule> studentSchedules;

        public Builder populationSize(int populationSize) {
            this.populationSize = populationSize;
            return this;
        }

        public Builder numStudents(int numStudents) {
            this.numStudents = numStudents;
            return this;
        }

        public Builder numModulesTotal(int numModulesTotal) {
            this.numModulesTotal = numModulesTotal;
            return this;
        }

        public Builder numModulesPerCourse(int numModulesPerCourse) {
            this.numModulesPerCourse = numModulesPerCourse;
            return this;
        }

        public Builder numDays(int numDays) {
            this.numDays = numDays;
            return this;
        }

        public Builder crossoverProbability(int crossoverProbability) {
            this.crossoverProbability = crossoverProbability;
            return this;
        }

        public Builder mutationProbability(int mutationProbability) {
            this.mutationProbability = mutationProbability;
            return this;
        }

        public Builder studentSchedules(List<Schedule> studentSchedules) {
            this.studentSchedules = studentSchedules;
            return this;
        }

        public Builder numGenerations(int numGenerations){
            this.numGenerations = numGenerations;
            return this;
        }

        public ExamScheduler createScheduler(){
            return new ExamScheduler(numGenerations,
                    populationSize,
                    numStudents,
                    numModulesTotal,
                    numModulesPerCourse,
                    numDays,
                    crossoverProbability,
                    mutationProbability,
                    studentSchedules);
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

class EvolutionHandler {
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

        // If the number of modules is less than 4 then we have no range
        // to work with and just choose the middle as the cutpoint.
        if(numModules < 4){
            return numModules / 2;
        }

        return rand.nextInt(numModules - 2) + 1;
    }
}

class Population{
    private List<Ordering> orderings;

    public Population(){
        orderings = new ArrayList<>();
    }

    public List<Ordering> getOrderings(){
        return orderings;
    }

    public void setOrderings(List<Ordering> orderings){
        this.orderings = orderings;
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
        // Use set to ensure that the initial population has unique orderings
        Set<Ordering> initialPop = new HashSet<>();

        while(initialPop.size() < populationSize){
            Ordering ordering = new Ordering(numDays);
            for(int j = 1; j <= numModulesTotal; j++){
                ordering.add(j);
            }

            Collections.shuffle(ordering.getOrdering());
            initialPop.add(ordering);
        }

        orderings = new ArrayList<>(initialPop);
    }

    @Override
    public String toString(){
        // Print best result in ordering
        return  "Ordering " +
                Integer.toString(1) +
                ": " +
                orderings.get(0).toString() +
                "Fitness Cost: " +
                orderings.get(0).getFitnessCost() +
                "\n";
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

    public int get(int index){
        return ordering.get(index);
    }

    public int getNumDays(){
        return numDays;
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

    public boolean contains(int item){
        return ordering.contains(item);
    }

    public List<Integer> getOrdering(){
        return ordering;
    }

    public void calculateFitnessCost(List<Schedule> studentSchedules){
        int cost = 0;
        List<Set<Integer>> examSessions = getExamSessions();

        for(Set<Integer> currentExams : examSessions){
            for(Schedule studentSchedule : studentSchedules){
                Set<Integer> schedule = studentSchedule.getModuleNumbers();
                Set<Integer> intersection = intersect(schedule, currentExams);

                // Some intersections may be larger than others
                // ie there may be a greater or lesser overlap between a single
                // session and a single student schedule.
                // However, since it was not specified in the project spec
                // how we should handle these situations
                // we will consider any overlap to be weighted as one
                // additional fitness cost
                if(intersection.size() > 1){
                    cost++;
                }
            }
        }

        this.fitnessCost = cost;
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder("");

        for (Integer anOrdering : ordering) {
            builder.append(anOrdering).append(" ");
        }

        builder.append("\n");

        List<List<Integer>> examSessions = getExamSessionsList();
        List<String> rows = getSessionRows(examSessions);

        for (int i = 0; i < examSessions.size(); i++) {
            builder.append("Session ").append(i + 1).append("\t");
        }
        builder.append("\n");
        for(String row: rows){
            builder.append(row).append("\n");
        }

        return builder.toString();
    }

    private Set<Integer> intersect(Set<Integer> a, Set<Integer> b){
        Set<Integer> intersect = new HashSet<>(a);
        intersect.retainAll(b);

        return intersect;
    }

    @Override
    public boolean equals(Object obj){
        if(! (obj instanceof Ordering)) return false;

        Ordering objOrdering = (Ordering) obj;

        return objOrdering.getOrdering().equals(ordering);
    }

    @Override
    public int hashCode(){
        return ordering.hashCode();
    }

    private List<Set<Integer>> getExamSessions(){
        int examsPerSession = (int) Math.ceil((double) this.size() / numDays);
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

    private List<List<Integer>> getExamSessionsList(){
        int examsPerSession = (int) Math.ceil((double) this.size() / numDays);
        List<List<Integer>> examDays = new ArrayList<>();
        List<Integer> buffer         = new ArrayList<>();

        for(int i = 0; i < ordering.size(); i++){
            buffer.add(ordering.get(i));
            if((i+1) % examsPerSession == 0){
                examDays.add(new ArrayList<>(buffer));
                buffer.clear();
            }
        }

        //Add remaining days to the List
        if(buffer.size() > 0) {
            examDays.add(new ArrayList<>(buffer));
        }

        return examDays;
    }

    private List<String> getSessionRows(List<List<Integer>> examSessions) {
        List<String> rows = new ArrayList<>();

        for(int j = 0; j < examSessions.get(0).size(); j++){
            StringBuilder row = new StringBuilder("");
            for (List<Integer> examSession : examSessions) {
                if (examSession.size() > j) {
                    row.append(examSession.get(j));
                    row.append("\t\t\t");
                }
            }
            rows.add(row.toString());
        }

        return rows;
    }
}


enum GaTechnique{
    MUTATION,
    CROSSOVER,
    REPRODUCTION
}