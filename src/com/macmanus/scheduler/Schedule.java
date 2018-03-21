package com.macmanus.scheduler;

import java.util.HashSet;
import java.util.Set;

public class Schedule{
    private final int studentNum;
    private Set<Integer> moduleNumbers;

    public Schedule(int studentNum){
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
