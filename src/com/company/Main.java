package com.company;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

//import test.PopulationGenerator;


public class Main {

    private static final int THREADS_NUMBER = 10;
    private static Input inputProject;


    public static void main(String[] args) {
        // write your code here




        //Checking arguments
        try {
            if (args.length < 3) {
                throw new InvalidArgumentNumber();
            }
                if (!args[1].contentEquals("-t")) {
                throw new InsertTime();
            }

            //Timer setting
            Timer timer = new Timer();
            long time = Long.valueOf(args[2]) * 1000; //To have the timer in seconds

            if (time <= 0) {
                throw new TimeTooLow();
            }

            //Timer is off atm
            /*
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    System.exit(0);
                }
            }, time);

            */
        } catch (NumberFormatException nfe) {
            System.out.println("The third argument must be a number");
            System.exit(1);
        } catch (InvalidArgumentNumber argumentNumber) {
            System.out.println("Not enough arguments");
            System.exit(1);
        } catch (TimeTooLow timeTooLow) {
            System.out.println("Increase time value");
            System.exit(1);
        } catch (InsertTime insertTime) {
            System.out.println("Tip:Try again with -t");
            System.exit(1);
        }

        inputProject=new Input(args[0]);
        inputProject.startInput();                          //Getting input from file

        //Test dell'input
        /*
        System.out.println("This is the input for the instance " + inputProject.getInstanceName());
        System.out.println("The number of exams is: " + inputProject.getExamNumber().toString());
        System.out.println("The number of students is " + inputProject.getStudentNumber().toString());
        System.out.println("The number of timeslots is " + inputProject.getTimeslots().toString());

        System.out.println("The conflict matrix is: ");
        Integer [][] cmatrix=inputProject.getConflictMatrix();
        for(Integer[] row:cmatrix) {
            System.out.println(Arrays.toString(row));
        }
        */

        //TestClass.chromosomesTest();

	    //Generating and Starting tier 1
	    Tier_1 tier_1 = new Tier_1(generatePopulation(), inputProject.getConflictMatrix());
        tier_1.first_tier();
        //System.exit(0);
    }


    //IT MANAGES THE POPULATION GENERATORS WITH DIFFERENT THREADS
    private static Population generatePopulation(){

        Integer[] exams = new Integer[inputProject.getExamNumber()];
        for(int i=0; i<exams.length;i++) exams[i]=i+1;

        //LORENZO'S POPULATION
        //Generating first population
        //PopulationGenerator pop = new PopulationGenerator(exams,inputProject.getTimeslots(),inputProject.getConflictMatrix(),inputProject.getStudentNumber());
        //pop.generatePop();
        //Creating population Class
        //Population p = new Population(pop.getStudentNum(), pop.getConflictMatrix(), pop.getPopulation());


        Population p = new Population(inputProject.getStudentNumber(), inputProject.getConflictMatrix(), null);
        ExecutorService executorService = Executors.newCachedThreadPool();
        ArrayList<RandomPopGenerator> randomPopThreads = new ArrayList<>();
        ArrayList<Chromosome> chromosomes = new ArrayList<>();

        //Running 10 threads to generate 1000 chromosomes each.
        for(int i = 0; i < THREADS_NUMBER; i++){
            RandomPopGenerator randPop = new RandomPopGenerator(inputProject.getConflictMatrix(), 1000, inputProject.getTimeslots(), inputProject.getStudentNumber(), inputProject.getExamNumber());
            executorService.submit(randPop);
            randomPopThreads.add(randPop);
        }

        executorService.shutdown();

        //Waiting for termination of all threads
        try{
            executorService.awaitTermination(1, TimeUnit.HOURS);
        }catch(InterruptedException e){
            System.out.println(e.getMessage());
        }

        // gathering result
        for(RandomPopGenerator h : randomPopThreads){
            chromosomes.addAll(h.getPopulation());
        }


        p.setPopulationList(chromosomes);
        return p;

    }

}



class TimeTooLow extends Exception { }

class InvalidArgumentNumber extends Exception { }

class InsertTime extends Exception {}





