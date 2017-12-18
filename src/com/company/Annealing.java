package com.company;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Annealing {
    private Integer[][] conflictMatrix;
    private Integer populationSize;
    private Integer tmax;
    private Integer studentNum;
    private Integer numExams;
    private ArrayList<Integer>[] population;

    //populationSize = amount of chromosomes that need to be generated
    public Annealing(Integer[][] conflictMatrix, Integer populationSize, Integer tmax, Integer studentNum, Integer numExams) {
        this.conflictMatrix = conflictMatrix;
        this.populationSize = populationSize;
        this.tmax = tmax;
        this.studentNum = studentNum;
        this.numExams = numExams;
        population = new ArrayList[tmax];
        for(Integer i = 0; i < tmax; i++){
            population[i] = new ArrayList<>();
        }
    }

    public void run(){
Random rnd = ThreadLocalRandom.current();
        ArrayList<Integer>[] newPopulation;
        Integer oldConflicts = 0;
        Integer repeats = 0;
        Integer x;
        Double temperature = 100.0;
        Double coolingRate = 0.003;

        //create an unfeasable starting point
        greedyGenerator();

        //calculate the number of conflicts
        x = numberOfConflicts(population);

        System.out.println(x);

        while(x != 0) {
            Integer newX;

            if(oldConflicts == x){
                repeats++;
            }else{
                repeats = 0;
                oldConflicts = x;

            }

            if(repeats == 70*numExams) {
                newPopulation = modifyPopulationGroupMove();
                temperature *= 1-coolingRate;
            }else{
                newPopulation = modifyPopulationMutation();
            }


            newX = numberOfConflicts(newPopulation);

            if(probability(temperature, x, newX) > 0.8){
                population = newPopulation;
                x = newX;
            }


            System.out.println("Temp: " + temperature + "\t\tConflicts: " + x);
        }

        Integer[] timeslotList = createTimeslotList(population);
        for(Integer i = 0; i < numExams; i++){
            System.out.println((i+1) + " " + (timeslotList[i]+1));
        }



    }


    private void randomGenerator(){
        Random rnd = ThreadLocalRandom.current();

        for(Integer exam = 0; exam < numExams; exam++){
            population[rnd.nextInt(tmax)].add(exam);
        }
    }

    private Integer numberOfConflicts(ArrayList<Integer>[] population){
        Integer conflicts = 0;

        for(Integer i = 0; i < tmax; i++){
            for(Integer exam1 : population[i]){
                for(Integer exam2 : population[i]){
                    //don't compare exams with themselves, and check if there is a conflict
                    if(exam1 != exam2 && conflictMatrix[exam1][exam2] != 0){
                        conflicts++;
                    }
                }
            }
        }
        //each conflict is counted twice, once for each exam
        return conflicts/2;
    }

    private Double probability(Double temperature, Integer x, Integer xNew){
        Double prob;
        prob = Math.exp(-(xNew - x)/temperature);
        return prob;
    }

    private ArrayList<Integer>[] modifyPopulationSwap(){
        Random rnd = ThreadLocalRandom.current();
        ArrayList<Integer>[] newPopulation = this.population;
        Boolean repeat = false;
        Integer exam1 = 0;
        Integer exam2 = 0;
        Integer timeslot1;
        Integer timeslot2;

        do {
            timeslot1 = rnd.nextInt(tmax);
            timeslot2 = rnd.nextInt(tmax);
            if (population[timeslot1].size() != 0 && population[timeslot2].size() != 0 && timeslot1 != timeslot2) {
                exam1 = population[timeslot1].get(rnd.nextInt(population[timeslot1].size()));
                exam2 = population[timeslot2].get(rnd.nextInt(population[timeslot2].size()));
                repeat = false;
                newPopulation[timeslot1].remove(exam1);
                newPopulation[timeslot1].add(exam2);
                newPopulation[timeslot2].remove(exam2);
                newPopulation[timeslot2].add(exam1);
            } else if (population[timeslot1].size() != 0) {
                exam1 = population[timeslot1].get(rnd.nextInt(population[timeslot1].size()));
                newPopulation[timeslot1].remove(exam1);
                newPopulation[timeslot2].add(exam1);
            }else{
                repeat = true;
            }
        }while(repeat);


        return newPopulation;
    }

    private ArrayList<Integer>[] modifyPopulationGroupMove(){
        Random rnd = ThreadLocalRandom.current();
        ArrayList<Integer>[] newPopulation = this.population;
        Integer i = rnd.nextInt((Integer)numExams/5);
        for(; i > 0; i--){
            Integer timeslot = rnd.nextInt(tmax);
            if(population[timeslot].size() != 0) {
                Integer exam = population[timeslot].get(rnd.nextInt(population[timeslot].size()));
                newPopulation[timeslot].remove(exam);
                newPopulation[rnd.nextInt(tmax)].add(exam);
            }
        }

        return newPopulation;
    }

    private ArrayList<Integer>[] modifyPopulationMutation(){
        Random rnd = ThreadLocalRandom.current();
        ArrayList<Integer>[] newPopulation = this.population;
        Integer[] randomTimeslot = new Integer[tmax];
        Integer timeslot;
        Integer moveExam;

        timeslot = rnd.nextInt(tmax);
        for(Integer i = 0; i < tmax; i++){
            randomTimeslot[i] = i;
        }

        shuffleArray(randomTimeslot);


        if(newPopulation[timeslot].size() != 0){
            Integer i = 0;
            Boolean set = false;
            moveExam = newPopulation[timeslot].get(rnd.nextInt(newPopulation[timeslot].size()));
            while(set == false && i < tmax){
                set = true;
                for(Integer exam : newPopulation[randomTimeslot[i]]){
                    if(conflictMatrix[moveExam][exam] != 0){
                        set = false;
                    }
                }

                //moveExam can be moved to this slot
                if(set == true){
                    newPopulation[timeslot].remove(moveExam);
                    newPopulation[randomTimeslot[i]].add(moveExam);
                }
                i++;
            }

        }

        return newPopulation;
    }

    private void greedyGenerator(){
        Random rnd = ThreadLocalRandom.current();
        Integer node;
        Integer index = 0;
        ArrayList<Integer> remainingExams = new ArrayList<>();


        for (Integer i = 0; i < numExams; i++) {
            remainingExams.add(i);
        }

        while (remainingExams.size() > 0) {
            ArrayList<Integer> removedExams = new ArrayList<>();
            ArrayList<Integer> independentSet = new ArrayList<>();

            node = remainingExams.get(rnd.nextInt(remainingExams.size()));


            while (remainingExams.size() > 0) {

                independentSet.add(node);
                remainingExams.remove(node);
                //remove all exams in conflict with node
                for (Integer exam : remainingExams) {
                    if (conflictMatrix[node][exam] != 0) {

                        removedExams.add(exam);
                    }
                }

                for (Integer exam : removedExams) {
                    remainingExams.remove(exam);
                }

                //find the exam with fewest overall conflicts
                Integer nextExam;

                Integer fewestConflicts = numExams + 1;
                nextExam = 0;
                for (Integer exam : remainingExams) {
                    Integer conflicts = 0;
                    for (Integer exam2 : remainingExams) {
                        if (conflictMatrix[exam][exam2] != 0) {
                            conflicts++;
                        }
                    }
                    if (conflicts < fewestConflicts) {
                        fewestConflicts = conflicts;
                        nextExam = exam;
                    }

                }



                node = nextExam;

            }
            population[index] = independentSet;
            remainingExams = removedExams;
            index++;
            if(index == tmax && remainingExams.size() > 0){
                break;
            }
        }

        //place last exams randomly
        for(Integer i = 0; i < remainingExams.size(); i++){
            remainingExams.get(i);
            population[rnd.nextInt(tmax)].add(remainingExams.get(i));
        }



        System.out.println("break");
    }

    // Implementing Fisher–Yates shuffle
    private void shuffleArray(Integer[] ar) {
        // If running on Java 6 or older, use `new Random()` on RHS here
        Random rnd = ThreadLocalRandom.current();
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            Integer a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    private Integer[] createTimeslotList(ArrayList<Integer>[] geneList){
        Integer[] timeslotList = new Integer[numExams];

        for(Integer i = 0 ; i < tmax; i++){
            for(Integer exam : geneList[i]){
                timeslotList[exam] = i;
            }
        }

        return timeslotList;
    }



}
