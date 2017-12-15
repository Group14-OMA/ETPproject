package com.company;
import java.util.*;

//import test.PopulationGenerator;


public class Main {



    public static void main(String[] args) {
        // write your code here


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
        Integer[] exams = new Integer[inputProject.getExamNumber()];
        for(int i=0; i<exams.length;i++) exams[i]=i+1; 
        
        //Generating first population
        //PopulationGenerator Pop = new PopulationGenerator(exams,inputProject.getTimeslots(),inputProject.getConflictMatrix(),inputProject.getStudentNumber());
        //Pop.conflictList();
        //Pop.printConflictList();
        //Pop.buildOptimizedConflictMatrix();
        //Pop.printOptimizedConflictMatrix();
        sortedPopulationGenerator sortedPop = new sortedPopulationGenerator(exams,inputProject.getTimeslots(),inputProject.getConflictMatrix(),inputProject.getStudentNumber());
        sortedPop.generatePop();
        /*
	    //Creating population Class
	    Population p = new Population(Pop.getStudentNum(), Pop.getConflictMatrix(), Pop.getPopulation());
	    
	    //Generating and Starting tier 1
	    Tier_1 tier_1 = new Tier_1(p, inputProject.getConflictMatrix());
        tier_1.first_tier();
        //System.exit(0);
         * 
         */
        return;
         
    }

}



class TimeTooLow extends Exception { }

class InvalidArgumentNumber extends Exception { }

class InsertTime extends Exception {}




