package com.macmanus.test;

import com.macmanus.scheduler.Ordering;
import com.macmanus.scheduler.Schedule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class testOrdering {

    private Ordering ordering;
    private List<Schedule> studentSchdules;

    @BeforeEach
    void setUp(){
        studentSchdules = new ArrayList<>();
        ordering = new Ordering();
    }

    @Test
    void testFitnessCost(){
        Schedule s1 = new Schedule(5);
        s1.addModule(3);
        s1.addModule(0);
        s1.addModule(4);
        s1.addModule(7);
        s1.addModule(3);
        s1.addModule(3);
        s1.addModule(3);
        s1.addModule(3);
        s1.addModule(3);
        s1.addModule(3);

        Ordering []orderings = new Ordering[5];


    }
}
