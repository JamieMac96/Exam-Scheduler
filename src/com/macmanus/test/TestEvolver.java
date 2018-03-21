package com.macmanus.test;

import com.macmanus.scheduler.Evolver;
import com.macmanus.scheduler.Ordering;
import com.macmanus.scheduler.Population;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class TestEvolver {
    private Population pop;
    private Evolver evolver;
    private List<Ordering> orderingList;

    @BeforeEach
    void setUp(){
        orderingList = new ArrayList<>();
        pop = new Population();
        evolver = new Evolver(33, 33);

        pop.setOrderings(orderingList);
    }

    @Test
    void testSelection(){

    }

    @Test
    void testCrossover(){
        //evolver.applyCrossover();
    }

    @Test
    void testMutation(){

    }

    @Test
    void testReproduction(){

    }

    @Test
    void testGetNextGeneration(){

    }
}
