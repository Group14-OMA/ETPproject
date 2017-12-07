package com.company;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Tier_1 {

    private Population population;
    private ArrayList<Chromosome> reproductionPop;          //10% of starting populations used to generate new chromosome
    private Executors executor;
    private ExecutorService threadManager;

    //CONSTRUCTOR
    public Tier_1(Population population){

        this.population = population;
        threadManager = executor.newCachedThreadPool();     //thread manager initialization

    }

    //Called by main to start generating new generations
    public void first_tier(){

        //while(true) UNTIL TIME ENDS

        //CALCULATING FITNESS AND ORDERING

        //SELECT 10% OF BEST FITNESS, ALWAYS AN EVEN NUMBER

        //START N THREADS TO CREATE NEW GENERATION, HALF MUTATION, HALF CROSSOVER
        //threadManager.execute(runnable);
        //threadManager.shutdown(); IN ORDER TO HAVE A SINGLE EXECUTION

        //DELETE 10% OF ORIGINAL POPULATION

        //AS SOON AS ALL THREADS HAVE TERMINATED, WRITE TO FILE.

    }// end first_tier



}
