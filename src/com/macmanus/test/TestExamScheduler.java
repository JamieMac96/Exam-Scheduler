package com.macmanus.test;

import com.macmanus.scheduler.ExamScheduler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TestExamScheduler {
    private ExamScheduler scheduler;

    @BeforeEach
    void setUp(){
        scheduler = new ExamScheduler();
    }

    @Test
    void testGenerateSchedules(){
        scheduler.generateStudentSchedules();


    }


}
