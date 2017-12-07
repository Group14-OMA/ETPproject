package com.company;
import java.util.*;


public class Main {



    public static void main(String[] args) {
        // write your code here
        //questo metodo fa schifo , non so se fermi i vari thread
        Input inputProject;

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

        System.out.println("Passing " + args[0]);
        inputProject=new Input(args[0]);
        //Test dell'input
        System.out.println("This is the input for the instance " + inputProject.getInstanceName());
        System.out.println("The number of exams is: " + inputProject.getExamNumber().toString());
        System.out.println("The number of students is " + inputProject.getStudentNumber().toString());
        System.out.println("The number of timeslots is " + inputProject.getTimeslots().toString());

        System.out.println("The conflict matrix is: ");
        Integer [][] cmatrix=inputProject.getConflictMatrix();
        for(Integer[] row:cmatrix) {
            System.out.println(Arrays.toString(row));
        }






        //commented code was used for testing ObjectiveFunction.

        /*Integer[][] C = new Integer[4][4];

        for(int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++)
                C[i][j] = 0;
        }
        C[0][1] = 2;
        C[0][2] = 3;
        C[1][0] = 2;
        C[1][2] = 2;
        C[2][0] = 3;
        C[2][1] = 2;

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                System.out.print(C[i][j] + " ");
            }
            System.out.println(" ");
        }
        System.out.println(" ");

        ArrayList<Integer>[] geneList = new ArrayList[6];
        ArrayList<Integer> gene1 = new ArrayList<>();
        gene1.add(0);
        gene1.add(3);
        geneList[0] = gene1;
        geneList[1]= new ArrayList<>();
        ArrayList<Integer> gene2 = new ArrayList<>();
        gene2.add(1);
        geneList[2] = gene2;
        geneList[3]= new ArrayList<>();
        geneList[4]= new ArrayList<>();
        ArrayList<Integer> gene3 = new ArrayList<>();
        gene3.add(2);
        geneList[5]= gene3;

        Integer[] timeslotList = new Integer[4];
        timeslotList[0] = 0;
        timeslotList[1] = 2;
        timeslotList[2] = 5;
        timeslotList[3] = 0;


        Chromosome chromosome = new Chromosome(6, 4,8,timeslotList,geneList);
        chromosome.updateObjectiveFunction(C);
        System.out.println(chromosome.getObjFunc());

        for(Integer i = 0; i < 4; i++){
            System.out.println("Exam: " + (i+1) + "\tTimeslot:" + (chromosome.getExamTimeslot(i)+1) );
        }

        Mutation mutationTest = new Mutation(chromosome, C);
        mutationTest.run();

        System.out.println(chromosome.getObjFunc());


        for(Integer i = 0; i < 4; i++){
            System.out.println("Exam: " + (i+1) + "\tTimeslot:" + (chromosome.getExamTimeslot(i)+1) );
        }*/

        System.exit(0);
    }
}
class TimeTooLow extends Exception { }

class InvalidArgumentNumber extends Exception { }

class InsertTime extends Exception {}
