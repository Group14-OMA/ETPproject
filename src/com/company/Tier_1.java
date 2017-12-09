package com.company;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Tier_1 {

    private Population population;
    private ArrayList<Chromosome> reproductionPop;          //10% of starting populations used to generate new chromosome

    private Executors executor;
    private ExecutorService threadManager;                  //Used to run all tier2 threads




    //CONSTRUCTOR
    public Tier_1(Population population){

        this.population = population;
        threadManager = executor.newCachedThreadPool();     //thread manager initialization

    }


    //Called by main to start generating new generations
    public void first_tier(){

            //while(true)

            //CALCULATING FITNESS AND ORDERING
            fitnessCalc();

            //SELECT 10% OF BEST FITNESS, ALWAYS AN EVEN NUMBER
            //x=10 * size / 100;


            //START N THREADS TO CREATE NEW GENERATION, HALF MUTATION, HALF CROSSOVER
            //threadManager.execute(runnable);
            //threadManager.shutdown(); IN ORDER TO HAVE A SINGLE EXECUTION

            //DELETE 10% OF ORIGINAL POPULATION

            //AS SOON AS ALL THREADS HAVE TERMINATED, WRITE TO FILE.


    }// end first_tier

    //It calculates and sets the fitness for all chromosome in the actual population
    private void fitnessCalc(){

        //Set best objective function value for the actual population
        population.setBestChromosome();

        double actualFitness = 0;
        double random = Math.random();      //number between 0 and 1

        for(Chromosome c : population.getPopulationList()){
            actualFitness = c.getObjFunc() / population.getBestObjectiveFunc();
            c.setFitness(actualFitness);
        }

    }// end fitnessCalc


    //It takes the best 10% chromosomes from the total population, plus the best one
    private void selectReproductionPop(){
        int sizePop = population.getPopulationList().size();
        int sizeReprPop = (10 * sizePop) / 100;

        //I'M CONSIDERING THE LIST ORDERED USING FITNESS
        //SELECTING BEST FITNESS CHROMOSOME
        reproductionPop.add(population.getPopulationList().get(0));

        if((sizeReprPop % 2) == 0){
            //even number of member, i have to add one more to have a total even number of chromosomes
            //starting from 1 to avoid the best one
            for(int i = 1; i <= sizeReprPop; i++){
                reproductionPop.add(population.getPopulationList().get(i));
            }
        }else{
            //odd number of chromosomes, adding the best one, i have an even number
            //starting from 1 to avoid the best one
            for(int i = 1; i <= sizeReprPop + 1; i++){
                reproductionPop.add(population.getPopulationList().get(i));
            }
        }
    }


}
