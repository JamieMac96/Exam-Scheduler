package com.macmanus.scheduler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Ordering{
    private int numDays;
    private int fitnessCost;
    private List<Integer> ordering;

    public Ordering(int days){
        numDays = days;
        ordering = new ArrayList<>();
    }

    public Ordering(){
        ordering = new ArrayList<>();
        numDays = 1;
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

    List<Integer> getOrdering(){
        return ordering;
    }

    public Ordering calculateFitnessCost(List<Schedule> studentSchedules){
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
                // how to handle these situations
                // we will consider any overlap to be weighted as one
                // additional fitness cost
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
        StringBuilder builder = new StringBuilder("");

        for (Integer anOrdering : ordering) {
            builder.append(anOrdering).append(" ");
        }

        builder.append("\n");

        List<List<Integer>> examSessions = getExamSessionsList();
        List<String> rows = getRows(examSessions);

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

    private List<String> getRows(List<List<Integer>> examSessions) {
        List<String> rows = new ArrayList<>();

        for(int j = 0; j < examSessions.get(0).size(); j++){
            StringBuilder row = new StringBuilder("");
            for(int i = 0; i < examSessions.size(); i++){
                if(examSessions.get(i).size() > j) {
                    row.append(examSessions.get(i).get(j));
                    row.append("\t\t\t");
                }
            }
            rows.add(row.toString());
        }

        return rows;
    }

    @Override
    public int hashCode(){
        return ordering.hashCode();
    }
}
