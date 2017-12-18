package com.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomPopGenerator implements Runnable
{



    private Integer[][] conflictMatrix;
    private Integer populationSize;
    private ArrayList<Chromosome> population;
    private Integer tmax;
    private Integer studentNum;
    private Integer numExams;

    //populationSize = amount of chromosomes that need to be generated
    public RandomPopGenerator(Integer[][] conflictMatrix, Integer populationSize, Integer tmax, Integer studentNum, Integer numExams) {
        this.conflictMatrix = conflictMatrix;
        this.populationSize = populationSize;
        this.tmax = tmax;
        this.studentNum = studentNum;
        this.numExams = numExams;
        this.population = new ArrayList<>();
    }

    public ArrayList<Chromosome> getPopulation() {
        return population;
    }


    @Override
    public void run() {
        Boolean placed;
        Boolean feasable;
        numExams = conflictMatrix.length;

        for(Integer i = 0; i < populationSize; i++) {
            ArrayList<Integer> randomArray = new ArrayList<>();
            Integer[] timeslotList = new Integer[numExams];
            ArrayList<Integer>[] geneList = new ArrayList[tmax];
            feasable = true;

            //initialize geneList with empty ArrayLists
            for(Integer j = 0; j < tmax; j++){
                geneList[j] = new ArrayList<>();
            }

            //randomArray dicatates in which order exams will be picked to be placed in geneList.
            for(Integer j = 0; j < numExams; j++){
                randomArray.add(j);
            }
            Collections.shuffle(randomArray);

            //place exams one by one in geneList
            for(Integer j = 0; j < numExams; j++){
                //run through different time slots until a valid one is found
                Integer k = 0;
                placed = false;
                while (k < tmax && placed == false){
                    placed = true;
                    //check if the timeslot is valid or not
                    for(Integer exam : geneList[k]){

                        if(conflictMatrix[randomArray.get(j)][exam] != 0){
                            //there is a conflict in this timeslot (k)
                            placed = false;
                        }
                    }
                    k++;
                }

                if(placed == true){
                    geneList[k-1].add(randomArray.get(j));
                }else{
                    feasable = false;
                    break;
                }


            }


            //Shuffle the timeslots that the different groups of non conflicting exams are in, so the aren't all collected in the first cells.
            shuffleArray(geneList);
            Chromosome newChromosome = new Chromosome(tmax, numExams, studentNum, createTimeslotList(geneList), geneList);
            newChromosome.updateObjectiveFunction(conflictMatrix);
            if(feasable) {
                population.add(newChromosome);
            }

        }
    }

    // Implementing Fisher–Yates shuffle
    private void shuffleArray(ArrayList[] ar)
    {
        // If running on Java 6 or older, use `new Random()` on RHS here
        Random rnd = ThreadLocalRandom.current();
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            ArrayList<Integer> a = ar[index];
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
