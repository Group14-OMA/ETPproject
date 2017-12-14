package com.company;

import javax.print.attribute.standard.ReferenceUriSchemesSupported;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.io.PrintWriter;


public class Tier_1 {

    private Population population;
    private ArrayList<Chromosome> reproductionPop;          //10% of starting populations used to generate new chromosome
    private Integer[][] C;
    private String fileName;
    private Random randomGenerator = new Random();
    private ExecutorService executorService;



    //CONSTRUCTOR
    public Tier_1(Population population, Integer[][] C,String instanceName){
        this.population = population;
        this.C = C;
        fileName=instanceName + "_OMAAL_group14.sol";

    }


    //Called by main to start generating new generations
    public void first_tier(){

            //while(true)
            for(int i = 0; i < 500; i++) { //put this for to try testing, not that the algorithm slows down a lot around 50 cycles in. I haven't been able to make it run completly.
                //CALCULATING FITNESS AND ORDERING
                fitnessCalc();

                //SELECT 10% OF BEST FITNESS, ALWAYS AN EVEN NUMBER
                selectReproductionPop();

                //START N THREADS TO CREATE NEW GENERATION, HALF MUTATION, HALF CROSSOVER
                runningThreads();

                //AS SOON AS ALL THREADS HAVE TERMINATED, WRITE TO FILE.
                //Daghero's Output class
                //THE FIRST ELEMENT OF POPULATION WILL BE THE BEST CHROMOSOME FOUND SO FAR
                System.out.println(i);
            }
            System.out.println("Done");
            System.out.println("ObjFunc: " + population.getBestObjectiveFunc());
            for(ArrayList<Integer> gene : population.getChromosome(0).getGeneList()){
                System.out.print(gene + "\t\t");
            }

    }// end first_tier


    //It calculates and sets the fitness for all chromosome in the actual population
    private void fitnessCalc(){

        //Set best objective function value for the actual population
        this.population.setBestChromosome();

        double actualFitness;

        //number between 1 and 2 so that all fitness will be higher than 1 (current best solution)
        double random = Math.random() + 1;


        //THE BEST SOLUTION WILL HAVE FITNESS 1, SO ALL OTHERS SOLUTIONS SHOULD HAVE AN HIGHER VALUE
        //ORDERING IN INCREASING ORDER WILL LEAVE THE BEST CHROMOSME AS FIRST
        for(Chromosome c : this.population.getPopulationList()){
            actualFitness = c.getObjFunc() / this.population.getBestObjectiveFunc();

            if(actualFitness == 1){
                //The best chromosome will have fitness set to 1, it will be always first
                c.setFitness(actualFitness);
            }else{
                actualFitness *= random;
                c.setFitness(actualFitness);
            }

        }// end for
        this.population.sortPopulation();

    }// end fitnessCalc

    //It takes the best 10% chromosomes from the total population, plus the best one
    private void selectReproductionPop(){
        //Resetting arraylist
        this.reproductionPop = new ArrayList<>();

        int sizePop = population.getPopulationList().size();
        int sizeReprPop = (10 * sizePop) / 100;


        if(sizeReprPop == 0){
            sizeReprPop = 1;
        }

        for(int i = 0; i < sizeReprPop; i++){
            this.reproductionPop.add(population.getPopulationList().get(i));
        }

    }// end selectReproductionPop


    //It deletes 10% of starting population of chromosomes
    private void deleteChromosomes(){

        int sizePop = population.getPopulationList().size();

        if(sizePop == 1){
            return;
        }

        int sizeDelPop = (10 * sizePop) / 100;

        if(sizeDelPop == 0){
            sizeDelPop = 1;
        }

        //Delete worst chromosomes. 10% starting from the end of the population ordered based on fitness
        for(int i = sizePop; i > sizePop - sizeDelPop; i--){
            this.population.removeChromosome(i - 1);
        }
    } // end deleteChromosome


    //It starts all threads
    private void runningThreads(){

        executorService = Executors.newCachedThreadPool();

        //HashMap<Mutation, Thread> mutationThreadsHashMap = new HashMap<>();             //Mapping Mutation Thread
        ArrayList<Mutation> mutationThreads = new ArrayList<>();                        //Contain all mutation

        for(int i = 0; i < reproductionPop.size(); i++){
            Mutation m = new Mutation(reproductionPop.get(i), C);
            mutationThreads.add(m);
        }



        //SAME FOR CROSSOVER
        //ArrayList<Mutation> mutationThreads = new ArrayList<>();

        //sentinel = 1 ---> Crossover
        //sentinel = 0 ---> Mutation
        //boolean sentinel = randomGenerator.nextBoolean();

        boolean sentinel = true;                                //TODO CHANGE WHEN CROSSOVER READY



        //reproductionPop.size()
        for(Mutation m : mutationThreads){
            //Thread t = new Thread(m, String.format("m%d", i));          //Name m + i

            if(sentinel){

                //t.run();
                executorService.submit(m);
                //mutationThreadsHashMap.put(m, t);
                //sentinel = false;
            }else{
                //TODO
                //CROSSOVER METHOD
            }
        }

        executorService.shutdown();
        //DELETE 10% OF ORIGINAL POPULATION
        deleteChromosomes();


        //WAIT FOR ALL THREADS TO COMPLETE

            try{
                //mutationThreadsHashMap.get(m).join();                   //PAUSED TIER 1
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

        //TODO
        //SAME FOR CROSSOVER

    }// end runningThreads

    //AKA Daghero's Output function!
    private void writeOutput() {
        Chromosome bestOne=population.getChromosome(0);
        Integer[] timeslotList=bestOne.getTimeSlotList();
        PrintWriter pw=null;


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

}// end Tier_1

