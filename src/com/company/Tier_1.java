package com.company;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Random;
import java.util.concurrent.TimeUnit;



public class Tier_1 {

    private final int PERCENTAGE_TO_REPRODUCTION = 10;

    private Population population;
    private ArrayList<Chromosome> reproductionPop;          //10% of starting populations used to generate new chromosome
    private Integer[][] C;
    private Random rng = new Random();
    private String fileName;
    private Random randomGenerator = new Random();
    private ExecutorService executorService;

    private Chromosome previousIterationBestChromosome = null;


    //CONSTRUCTOR

    public Tier_1(Population population, Integer[][] C,String instanceName){
        this.population = population;
        this.C = C;
        fileName=instanceName + "_OMAAL_group14.sol";

    }


    //Called by main to start generating new generations
    public void first_tier() {


        //while(true)
        for (int i = 0; i < 10000; i++) { //put this for to try testing, not that the algorithm slows down a lot around 50 cycles in. I haven't been able to make it run completly.
            //CALCULATING FITNESS AND ORDERING
            fitnessCalc();

            //SELECT 10% OF BEST FITNESS, ALWAYS AN EVEN NUMBER
            selectReproductionPop();

            //START N THREADS TO CREATE NEW GENERATION, HALF MUTATION, HALF CROSSOVER
            runningThreads();

            setBest();

            //AS SOON AS ALL THREADS HAVE TERMINATED, WRITE TO FILE.
            //TODO Daghero's Output class

            System.out.println(i);
        }
        System.out.println("Done");
        System.out.println("ObjFunc: " + previousIterationBestChromosome.getObjFunc());
        for (ArrayList<Integer> gene : previousIterationBestChromosome.getGeneList()) {
            System.out.print(gene + "\t\t");
        }

        output();

    }// end first_tier

    //It calculates and sets the fitness for all chromosome in the actual population
    private void fitnessCalc() {

        double bestObjFunction;

        //Set best objective function value for the actual population

        this.population.setBestChromosome();
        bestObjFunction = this.population.getBestObjectiveFunc();



        double actualFitness;

        //number between 1 and 2 so that all fitness will be higher than 1 (current best solution)
        double random = Math.random();


        //THE BEST SOLUTION WILL HAVE FITNESS 1, SO ALL OTHERS SOLUTIONS SHOULD HAVE AN HIGHER VALUE
        //ORDERING IN INCREASING ORDER WILL LEAVE THE BEST CHROMOSME AS FIRST
        for (Chromosome c : this.population.getPopulationList()) {
            //The higher the obj function the lower the probability to reproduce
            //The bigger the fitness the higher the probability to reproduce
            actualFitness = 1 / (c.getObjFunc()/ bestObjFunction);
            actualFitness *= random;
            c.setFitness(actualFitness);
            random = Math.random();
        }// end for
        this.population.sortPopulationFitness();
    }// end fitnessCalc

    //It takes the best 10% chromosomes from the total population, plus the best one
    private void selectReproductionPop(){
        //Resetting arraylist
        this.reproductionPop = new ArrayList<>();

        int sizePop = population.getPopulationList().size();
        int sizeReprPop = (PERCENTAGE_TO_REPRODUCTION * sizePop) / 100;


        if(sizeReprPop == 0){
            sizeReprPop = 1;
        }

        for(int i = sizePop - 1; i > sizePop - sizeReprPop; i--){
            this.reproductionPop.add(population.getPopulationList().get(i));
        }



    }// end selectReproductionPop

    //It deletes a percentage of starting population of chromosomes
    //Based on fitness
    private void deleteChromosomes(){

        int sizePop = population.getPopulationList().size();

        if(sizePop == 1){
            return;
        }

        int sizeDelPop = (PERCENTAGE_TO_REPRODUCTION * sizePop) / 100;

        if(sizeDelPop == 0){
            sizeDelPop = 1;
        }

        //Delete worst chromosomes. 10% starting from the top of the population ordered based on increasing fitness
        for(int i = 0; i < sizeDelPop; i++){
            this.population.removeChromosome(i);
        }

        if(this.previousIterationBestChromosome != null){
            this.population.addChromosome(this.previousIterationBestChromosome);
        }
    } // end deleteChromosome


    //It starts all threads
    private void runningThreads(){

        //It manages threads
        executorService = Executors.newCachedThreadPool();

        //To keep track of running threads and being able to recover results
        ArrayList<Mutation> mutationThreads = new ArrayList<>();
        ArrayList<CrossOver> crossOverThreads = new ArrayList<>();



        //sentinel = 1 ---> Crossover
        //sentinel = 0 ---> Mutation
        boolean sentinel = rng.nextBoolean();



        //Creation of threads, passing values
        for(int i = 0; i < reproductionPop.size() - 1; i++) {

            if (sentinel) {
                //STARTING MUTATION
                Mutation m = new Mutation(reproductionPop.get(i), C);
                mutationThreads.add(m);
                executorService.submit(m);
                sentinel = false;
            } else {
                //CROSSOVER METHOD
                CrossOver c = new CrossOver(reproductionPop.get(i), reproductionPop.get(i + 1), this.C);
                crossOverThreads.add(c);
                executorService.submit(c);
                i++;
                sentinel = true;
            }
        }//end for

        //no new tasks runned
        executorService.shutdown();

        //DELETE A PERCENTAGE OF ORIGINAL POPULATION
        deleteChromosomes();


        //WAIT FOR ALL THREADS TO COMPLETE
        try{
            executorService.awaitTermination(10, TimeUnit.MINUTES);
        }catch (InterruptedException i){
            System.out.println(i.getMessage());
        }



        //ALL THREADS COMPLETED, IT CHECKS IF THERE ARE SOME DUPLICATE. IT ADDS NEW CHROMOSOMES ONLY IF THEY ARE UNIQUE
        for(Mutation m : mutationThreads){
            boolean canInsert = true;
            for(Chromosome c : population.getPopulationList()){
                if(c.getTimeSlotList().equals(m.getChromosome().getTimeSlotList())){
                    canInsert = false;
                    break;
                }
            }
            if(canInsert){
                this.population.addChromosome(m.getChromosome());
            }
        }

        //ALL THREADS COMPLETED, IT CHECKS IF THERE ARE SOME DUPLICATE. IT ADDS NEW CHROMOSOMES ONLY IF THEY ARE UNIQUE
        for(CrossOver crv : crossOverThreads){
            boolean canInsert = true;
            for(Chromosome c : population.getPopulationList()) {
                if (c.getTimeSlotList().equals(crv.getC1().getTimeSlotList())) {
                    canInsert = false;
                    break;
                } else if (c.getTimeSlotList().equals(crv.getC2().getTimeSlotList())){
                    canInsert = false;
                    break;
                    }
                }//end for
            if(canInsert){
                this.population.addChromosome(crv.getC1());
                this.population.addChromosome(crv.getC2());
            }
        }//end for

    }// END RUNNING_THREAD

    //AKA Daghero's Output function!!
    private void output() {
        Chromosome bestOne=population.getChromosome(0);
        Integer[] timeslotList=bestOne.getTimeSlotList();
        PrintWriter pw = null;


        try {

            pw=new PrintWriter(fileName);
            for (int a=0; a < timeslotList.length; a++) {
                pw.println( (a+1) + " " + (timeslotList[a] + 1));
            }
            pw.flush();
            pw.close();

        } catch (FileNotFoundException fnf) {
            fnf.printStackTrace();
        }

    }

    //Set current population best chromosome
    public void setBest(){
        //SORT BASED ON OBJ FUNCTION
        this.population.sortPopulationObjFunction();

        //SELECTING BEST CHROMOSOME IN THIS POPULATION
        previousIterationBestChromosome = this.population.getChromosome(0);
    }

}// end Tier_1

