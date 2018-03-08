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

    ExamScheduler(){
        studentSchedules = new ArrayList<>();
    }

    // This method will be used to control the rest of the program
    public void run(){

        initializeParameters();

        generateStudentSchedules();

        for(int i = 0; i < studentSchedules.size(); i++) {
            System.out.println(studentSchedules.get(i));
        }
    }

    private void initializeParameters(){

        ParameterReader reader = new ParameterReader();

        numGenerations = reader.getNumGenerations();
        populationSize = reader.getPopulationSize();
        numStudents = reader.getNumStudents();
        numModulesTotal = reader.getNumModulesTotal();
        numModulesPerCourse = reader.getNumModulesPerCourse();
        numDays = reader.getNumDays();
        crossoverProbability = reader.getCrossoverProbability();
        mutationProbability = reader.getMutationProbability();

        //Vaidate parameters.
        while(!(numModulesTotal > numModulesPerCourse)){
            System.out.println("Error! There are less total modules " +
                               "than modules per course");

            numModulesTotal = reader.getNumModulesTotal();
            numModulesPerCourse = reader.getNumModulesPerCourse();
        }

        while(crossoverProbability + mutationProbability > 100){
            System.out.println("Crossover and mutation probability sum " +
                               "to be grater than 100");
            crossoverProbability = reader.getCrossoverProbability();
            mutationProbability = reader.getMutationProbability();
        }
    }

    private void generateStudentSchedules(){
        Schedule studentSchedule;

        for(int i = 0; i < numStudents; i++){
            studentSchedule = new Schedule(i);
            while(studentSchedule.getModuleNumbers().size() < numModulesPerCourse){
                int module = (int)(Math.random() * numModulesTotal);
                System.out.println(module);
                studentSchedule.addModule(module);
            }
            studentSchedules.add(studentSchedule);
        }
    }
}

class ParameterReader{
    private Scanner scanner;

    ParameterReader(){
        scanner = new Scanner(System.in);
    }

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
        System.out.print(message);
        return scanner.nextInt();
    }
}

class Schedule{
    private final int studentNum;
    private Set<Integer> moduleNumbers;

    Schedule(int studentNum){
        this.studentNum = studentNum;
        moduleNumbers = new HashSet<>();
    }

    public int getStudentNum() {
        return studentNum;
    }

    public Set<Integer> getModuleNumbers() {
        return moduleNumbers;
    }

    public void addModule(int module){
        moduleNumbers.add(module);
    }

    @Override
    public String toString(){
        return  studentNum + ": " + moduleNumbers.toString();
    }
}


