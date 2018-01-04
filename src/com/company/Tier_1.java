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
    private Boolean continueLoop;

    private Chromosome previousIterationBestChromosome = null;
    private Boolean chromosomeHasChanged = true;


    //CONSTRUCTOR

    public Tier_1(Population population, Integer[][] C,String instanceName){
        this.population = population;
        this.C = C;
        fileName=instanceName + "_OMAAL_group14.sol";
        continueLoop = true;

    }


    //Called by main to start generating new generations
    public void first_tier() {
        Integer i = 0;

        while(continueLoop){
        //for (int y = 0; y < 10000; y++) { //put this for to try testing, not that the algorithm slows down a lot around 50 cycles in. I haven't been able to make it run completly.
            //CALCULATING FITNESS AND ORDERING
            fitnessCalc();

            //SELECT 10% OF BEST FITNESS, ALWAYS AN EVEN NUMBER
            selectReproductionPop();

            //START N THREADS TO CREATE NEW GENERATION, HALF MUTATION, HALF CROSSOVER
            runningThreads();

            setBest();

            //AS SOON AS ALL THREADS HAVE TERMINATED, WRITE TO FILE.
            //TODO Daghero's Output class
            if(chromosomeHasChanged) {
                //System.out.println("Updated: " + population.getChromosome(0).getObjFunc());
                output();
                chromosomeHasChanged = false;
            }

            i++;
            //System.out.println(i);
        }
        System.out.println("Done, exiting...");
        /*System.out.println("ObjFunc: " + previousIterationBestChromosome.getObjFunc());
        for (ArrayList<Integer> gene : previousIterationBestChromosome.getGeneList()) {
            System.out.println(gene);
        }*/
        //for(int k = 0; k < previousIterationBestChromosome.getExamNum(); k++){
            //System.out.println((k+1) + " " + (previousIterationBestChromosome.getExamTimeslot(k) + 1));
        //}

        //output();

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
        //ORDERING IN INCREASING ORDER WILL LEAVE THE WORSE CHROMOSME AS FIRST
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

        //CHANGED i > sizePop - ... to i >= sizePop - ... to avoid making the population smaller than the initial population size.
        for(int i = sizePop - 1; i >= sizePop - sizeReprPop; i--){
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

        //Delete worst chromosomes. 10% starting from the top of the population ordered based on increasing fitness. Doesn't remove the best chromosome from the population.
        Integer i = 0;
        Integer j = 0;
        while(i < sizeDelPop){
            if(previousIterationBestChromosome != null && this.population.getChromosome(j).getObjFunc() != previousIterationBestChromosome.getObjFunc()){
                //this is not the best chromosome, we can delete it
                this.population.removeChromosome(j);
                i++;
            }else if (previousIterationBestChromosome == null){
                this.population.removeChromosome(j);
                i++;
            }
            j++;
        }
    } // end deleteChromosome

    private void deleteChromosomes2(Integer sizeDelPop){

        int sizePop = population.getPopulationList().size();

        if(sizePop == 1){
            return;
        }

        //Delete worst chromosomes. 10% starting from the top of the population ordered based on increasing fitness. Doesn't remove the best chromosome from the population.
        Integer i = 0;
        Integer j = 0;
        while(i < sizeDelPop){
            if(previousIterationBestChromosome != null && this.population.getChromosome(j).getObjFunc() != previousIterationBestChromosome.getObjFunc()){
                //this is not the best chromosome, we can delete it
                this.population.removeChromosome(j);
                i++;
            }else if (previousIterationBestChromosome == null){
                this.population.removeChromosome(j);
                i++;
            }
            j++;
        }
    } // end deleteChromosome


    //It starts all threads
    private void runningThreads(){

        //It manages threads
        executorService = Executors.newCachedThreadPool();

        //To keep track of running threads and being able to recover results
        ArrayList<Mutation> mutationThreads = new ArrayList<>();
        ArrayList<CrossOver> crossOverThreads = new ArrayList<>();
        ArrayList<TimeslotSwap> timeSlotSwapThreads = new ArrayList<>();



        //sentinel = 1 ---> Crossover
        //sentinel = 0 ---> Mutation
        int sentinel = rng.nextInt(3);



        //Creation of threads, passing values
        for(int i = 0; i < reproductionPop.size() - 1; i++) {

            switch (sentinel){

                case 0:
                    //STARTING MUTATION
                    Mutation m = new Mutation (reproductionPop.get(i), this.C);
                    mutationThreads.add(m);
                    executorService.submit(m);
                    sentinel = rng.nextInt(3);
                    break;
                case 1:
                    //STARTING CROSSOVER
                    CrossOver c = new CrossOver(reproductionPop.get(i), reproductionPop.get(i + 1), this.C);
                    crossOverThreads.add(c);
                    executorService.submit(c);
                    i++;                                                //I have used 2 chromosomes, i need to increment i 2 times
                    sentinel = rng.nextInt(3);
                    break;
                case 2:
                    //STARTING TIMESLOTSWAP
                    TimeslotSwap t = new TimeslotSwap(reproductionPop.get(i), this.C);
                    timeSlotSwapThreads.add(t);
                    executorService.submit(t);
                    sentinel = rng.nextInt(3);
                    break;

            }



        }//end for

        //no new tasks runned
        executorService.shutdown();

        //DELETE THE SAME AMOUNT OF CHROMOSOMES AS THE AMOUNT THAT WILL BE ADDED
        //deleteChromosomes();
        deleteChromosomes2(mutationThreads.size() + (crossOverThreads.size() * 2) + timeSlotSwapThreads.size());


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
            if(canInsert) {
                //this.population.addChromosome(m.getChromosome());
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
                //this.population.addChromosome(crv.getC1());
                this.population.addChromosome(crv.getC1());

                //this.population.addChromosome(crv.getC2());
                this.population.addChromosome(crv.getC2());


            }
        }//end for

        //ALL THREADS COMPLETED, IT CHECKS IF THERE ARE SOME DUPLICATE. IT ADDS NEW CHROMOSOMES ONLY IF THEY ARE UNIQUE
        for(TimeslotSwap t : timeSlotSwapThreads){
            boolean canInsert = true;
            for(Chromosome c : population.getPopulationList()){
                if(c.getTimeSlotList().equals(t.getChromosome().getTimeSlotList())){
                    canInsert = false;
                    break;
                }
            }
            if(canInsert){
                //this.population.addChromosome(t.getChromosome());
                this.population.addChromosome(t.getChromosome());
            }
        }


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


        if(previousIterationBestChromosome == null || previousIterationBestChromosome.getObjFunc() > this.population.getChromosome(0).getObjFunc()){
            //obj function has improved or we are on the 1st cycle
            chromosomeHasChanged = true;
        }


        //SELECTING BEST CHROMOSOME IN THIS POPULATION
        previousIterationBestChromosome = this.population.getChromosome(0);
    }

    public void stopLoop(){
        this.continueLoop = false;
    }

}// end Tier_1

