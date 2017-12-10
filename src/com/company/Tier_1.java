package com.company;

import javax.print.attribute.standard.ReferenceUriSchemesSupported;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.Random;


public class Tier_1 {

    private Population population;
    private ArrayList<Chromosome> reproductionPop;          //10% of starting populations used to generate new chromosome
    private Integer[][] C;

    private Random randomGenerator = new Random();


    //CONSTRUCTOR
    public Tier_1(Population population, Integer[][] C){
        this.population = population;
        this.C = C;
    }


    //Called by main to start generating new generations
    public void first_tier(){

            //while(true)

            //CALCULATING FITNESS AND ORDERING
            fitnessCalc();

            //SELECT 10% OF BEST FITNESS, ALWAYS AN EVEN NUMBER
            selectReproductionPop();

            //START N THREADS TO CREATE NEW GENERATION, HALF MUTATION, HALF CROSSOVER
            runningThreads();

            //AS SOON AS ALL THREADS HAVE TERMINATED, WRITE TO FILE.
            //Daghero's Output class
            //CALLING FITNESSCALC GIVES BACK THE BEST CHROMOSOME


    }// end first_tier

    //It calculates and sets the fitness for all chromosome in the actual population
    //IT ALSO GIVES BACK THE BEST CHROMOSOME
    private Chromosome fitnessCalc(){

        Chromosome bestChromosome = null;

        //Set best objective function value for the actual population
        population.setBestChromosome();

        double actualFitness;
        double random = Math.random();      //number between 0 and 1

        for(Chromosome c : population.getPopulationList()){
            actualFitness = c.getObjFunc() / population.getBestObjectiveFunc();

            if(actualFitness == 1){
                //The best chromosome will have fitness set to 1, it will be always first
                c.setFitness(actualFitness);
                bestChromosome = c;
            }else{
                actualFitness *= random;
                c.setFitness(actualFitness);
            }


        }

        return bestChromosome;
    }// end fitnessCalc

    //It takes the best 10% chromosomes from the total population, plus the best one
    private void selectReproductionPop(){
        int sizePop = population.getPopulationList().size();
        int sizeReprPop = (10 * sizePop) / 100;

        //I'M CONSIDERING THE LIST ORDERED BY FITNESS
        //SELECTING BEST FITNESS CHROMOSOME
        this.reproductionPop.add(population.getPopulationList().get(0));

        if((sizeReprPop % 2) == 0){
            //even number of member, i have to add one more to have a total even number of chromosomes
            //starting from 1 to avoid the best one
            for(int i = 1; i <= sizeReprPop; i++){
                this.reproductionPop.add(population.getPopulationList().get(i));
            }
        }else{
            //odd number of chromosomes, adding the best one, i have an even number
            //starting from 1 to avoid the best one
            for(int i = 1; i <= sizeReprPop + 1; i++){
                this.reproductionPop.add(population.getPopulationList().get(i));
            }
        }
    }

    //It deletes 10% of starting population of chromosomes
    private void deleteChromosomes(){
        int sizePop = population.getPopulationList().size();
        int sizeDelPop = (10 * sizePop) / 100;

        if(sizeDelPop == 0){
            sizeDelPop = 1;
        }

        //Delete worst chromosomes. 10% starting from the end of the population ordered based on fitness
        for(int i = sizePop - 1; i > sizePop - sizeDelPop; i--){
            this.population.getPopulationList().remove(i);
        }
    }

    //It starts all threads
    private void runningThreads(){

        HashMap<Mutation, Thread> mutationThreadsHashMap = new HashMap<>();             //Mapping Mutation Thread
        ArrayList<Mutation> mutationThreads = new ArrayList<>();                        //Contain all mutation

        //SAME FOR CROSSOVER
        //ArrayList<Mutation> mutationThreads = new ArrayList<>();

        //sentinel = 1 ---> Crossover
        //sentinel = 0 ---> Mutation
        boolean sentinel = randomGenerator.nextBoolean();


        for(int i = 0; i < reproductionPop.size(); i++){
            Mutation m = new Mutation(reproductionPop.get(i), C);
            Thread t = new Thread(m, String.format("%md", i));          //Name m + i
            if(sentinel){
                t.run();
                mutationThreadsHashMap.put(m, t);
                mutationThreads.add(m);
            }else{
                //TODO
                //CROSSOVER METHOD
            }
        }

        //DELETE 10% OF ORIGINAL POPULATION
        deleteChromosomes();


        //WAIT FOR ALL THREADS TO COMPLETE
        for(Mutation m : mutationThreads){
            try{
                mutationThreadsHashMap.get(m).join();                   //PAUSED TIER 1
            }catch (InterruptedException i){
                System.out.println(i.getMessage());
            }
        }


        //ALL THREADS COMPLETED, IT CHECKS IF THERE ARE SOME DUPLICATE. IT ADDS NEW CHROMOSOMES ONLY IF THEY ARE UNIQUE
        for(Mutation m : mutationThreads){
            //IF IT DOESN'T CONTAIN
            if(!population.getPopulationList().contains(m)){
                population.getPopulationList().add(m.getChromosome());
            }
        }

        //TODO
        //SAME FOR CROSSOVER

    }





}
