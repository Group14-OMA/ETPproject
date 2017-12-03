package com.company;
import java.util.*;


public class Main {



    public static void main(String[] args) {
        // write your code here
        //questo metodo fa schifo , non so se fermi i vari thread
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
                    System.exit(0);
                }
            }, time);


            while (true) ;
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

        ArrayList<ArrayList<Integer>>geneList = new ArrayList<>();
        ArrayList<Integer> gene1 = new ArrayList<>();
        gene1.add(1);
        gene1.add(4);
        geneList.add(gene1);
        geneList.add(null);
        ArrayList<Integer> gene2 = new ArrayList<>();
        gene2.add(2);
        geneList.add(gene2);
        geneList.add(null);
        geneList.add(null);
        ArrayList<Integer> gene3 = new ArrayList<>();
        gene3.add(3);
        geneList.add(gene3);

        Chromosome chromosome = new Chromosome(null, geneList, 6);
        ObjectiveFunction of = new ObjectiveFunction();
        System.out.println(of.calculateObjectiveFunction(chromosome, 6, 8, C));*/



    }
}
class TimeTooLow extends Exception { }

class InvalidArgumentNumber extends Exception { }

class InsertTime extends Exception {}
