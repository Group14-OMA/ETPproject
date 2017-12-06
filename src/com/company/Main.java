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

        ArrayList<Integer>[] geneList = new ArrayList[6];
        ArrayList<Integer> gene1 = new ArrayList<>();
        gene1.add(1);
        gene1.add(4);
        geneList[0] = gene1;
        geneList[1]= null;
        ArrayList<Integer> gene2 = new ArrayList<>();
        gene2.add(2);
        geneList[2] = gene2;
        geneList[3]= null;
        geneList[4]= null;
        ArrayList<Integer> gene3 = new ArrayList<>();
        gene3.add(3);
        geneList[5]= gene3;

        Chromosome chromosome = new Chromosome(6, 4,8,null,geneList);
        ObjectiveFunction of = new ObjectiveFunction();
        System.out.println(of.calculateObjectiveFunction(chromosome, 6, 8, C));

        */

    }
}
class TimeTooLow extends Exception { }

class InvalidArgumentNumber extends Exception { }

class InsertTime extends Exception {}
