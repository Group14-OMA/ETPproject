package com.company;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

//import test.PopulationGenerator;


public class Main {

    private static Boolean timerFinished = false;
    private static Tier_1 tier_1;
    private static Annealing2 annealing;
    private static ExecutorService executorService;
    private static Integer phase = 0; //0-->input, 1-->pop generation, 2-->tier1



    public static void main(String[] args) {
        // write your code here
        //TestClass.annealingTest();
        //System.exit(0);

        Input inputProject;

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


            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    System.out.println("Timer finished, asking threads to shutdown...");
                    timerFinished = true;
                    if(phase == 1) {
                        annealing.endAnnealing();
                        executorService.shutdownNow();
                    }else if(phase == 2) {
                        tier_1.stopLoop();
                    }
                }
            }, time);


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

        System.out.println("Reading file and elaborating...");
        inputProject=new Input(args[0]);
        inputProject.startInput();                          //Getting input from file
        //TEST
        //Annealing2 annealing = new Annealing2(inputProject.getConflictMatrix(),500, inputProject.getTimeslots(), inputProject.getStudentNumber(), inputProject.getExamNumber());
        //annealing.run();
        //System.exit(0);
        //END TEST

        Integer[] exams = new Integer[inputProject.getExamNumber()];
        for(int i=0; i<exams.length;i++) exams[i]=i;

        Population p = null;
        if(!timerFinished) {
            System.out.println("Generating an initial population...");
            phase = 1;

            executorService = Executors.newCachedThreadPool();
            sortedPopulationGenerator sortedPop = new sortedPopulationGenerator(exams, inputProject.getTimeslots(), inputProject.getConflictMatrix(), inputProject.getStudentNumber(), 200);
            annealing = new Annealing2(inputProject.getConflictMatrix(), 200, inputProject.getTimeslots(), inputProject.getStudentNumber(), inputProject.getExamNumber());
            executorService.submit(sortedPop);
            executorService.submit(annealing);

            executorService.shutdown();

            Boolean waitingForPop = true;
            while (waitingForPop) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (sortedPop.getSolFound()) {
                    annealing.endAnnealing();
                    try {
                        executorService.awaitTermination(10, TimeUnit.MINUTES);
                    } catch (InterruptedException i) {
                        System.out.println(i.getMessage());
                    }
                    p = new Population(sortedPop.getStudentNum(), sortedPop.getConflictMatrix(), sortedPop.getPopulation());
                    waitingForPop = false;
                } else if (annealing.getPopGenerated()) {
                    p = new Population(sortedPop.getStudentNum(), sortedPop.getConflictMatrix(), annealing.getChromosomesPop());
                    waitingForPop = false;
                }
            }
        }

        //sortedPop.generatePop();

        if(!timerFinished) {
            //Generating and Starting tier 1
            System.out.println("Finding best solution...");
            phase = 2;
            tier_1 = new Tier_1(p, inputProject.getConflictMatrix(), args[0]);
            tier_1.first_tier();
        }

        System.exit(0);
    }

}



class TimeTooLow extends Exception { }

class InvalidArgumentNumber extends Exception { }

class InsertTime extends Exception {}



