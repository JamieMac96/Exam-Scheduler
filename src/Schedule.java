import java.util.HashSet;
import java.util.Set;

public class Schedule{
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
        return  "Student " + studentNum + ": " + moduleNumbers.toString();
    }
}
