package com.company;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class GreedyGeneratorTest {
    private Integer[][] conflictMatrix;
    private Integer populationSize;
    private Integer tmax;
    private Integer studentNum;
    private Integer numExams;
    private ArrayList<Integer>[] population;

    //populationSize = amount of chromosomes that need to be generated
    public GreedyGeneratorTest(Integer[][] conflictMatrix, Integer populationSize, Integer tmax, Integer studentNum, Integer numExams) {
        this.conflictMatrix = conflictMatrix;
        this.populationSize = populationSize;
        this.tmax = tmax;
        this.studentNum = studentNum;
        this.numExams = numExams;
        population = new ArrayList[tmax];
    }


    public void run() {
        Boolean solutionFound = false;
        while (!solutionFound) {
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
                    //solution not valid
                    break;
                }else if(remainingExams.size() == 0){
                    //solution valid
                    solutionFound = true;
                }
            }

            System.out.println(solutionFound);
        }

        Integer[] timeslotList=createTimeslotList(population);
        PrintWriter pw = null;


        try {

            pw=new PrintWriter("output.sol");
            for (int a=0; a < timeslotList.length; a++) {
                pw.println( (a+1) + " " + (timeslotList[a] + 1));
            }
            pw.flush();
            pw.close();

        } catch (FileNotFoundException fnf) {
            fnf.printStackTrace();
        }
        System.out.println("break");

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
