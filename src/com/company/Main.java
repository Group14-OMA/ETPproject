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

    }
}
class TimeTooLow extends Exception { }

class InvalidArgumentNumber extends Exception { }

class InsertTime extends Exception {}
