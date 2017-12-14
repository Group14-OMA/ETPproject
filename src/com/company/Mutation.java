package com.company;

import java.util.ArrayList;
import java.util.Random;

public class Mutation implements Runnable{
    private Chromosome chromosome;
    private Integer[][] C;

    public Mutation(Chromosome c, Integer[][] C) {
        this.chromosome = new Chromosome(c.getTmax(), c.getExamNum(), c.getStudentNum(), c.getTimeSlotList(), c.getGeneList());
        this.chromosome.setFitness(c.getFitness());

        ArrayList<Integer>[] geneList = new ArrayList[c.getGeneList().length];
        for(int i = 0; i < c.getGeneList().length; i++){
            geneList[i] = (ArrayList<Integer>) c.getGeneList()[i].clone();
        }

        this.chromosome.setGeneList(geneList);
        this.chromosome.setTimeSlotList(c.getTimeSlotList().clone());
        this.C = C;
        this.chromosome.updateObjectiveFunction(this.C);
    }

    public void setChromosome(Chromosome c) {
        this.chromosome = new Chromosome(c.getTmax(), c.getExamNum(), c.getStudentNum(), c.getTimeSlotList(), c.getGeneList());
    }

    public Chromosome getChromosome() {
        return chromosome;
    }

    @Override
    public void run() {
        Integer numTimeslotsTested = 0;

        Integer examToChange;
        Integer oldTimeslot;
        Integer newTimeslot;
        Boolean feasable = true;
        Random rn = new Random();

        examToChange = rn.nextInt(chromosome.getExamNum());
        newTimeslot = rn.nextInt(chromosome.getTmax());
        oldTimeslot = chromosome.getExamTimeslot(examToChange);    //timeslots of old exam

        //System.out.println("ExamToChange: " + (examToChange+1) + "\tNewTimeslot: "+(newTimeslot+1));


        //check feasability
        do {

            //I make sure to test all possible solutions before returning the same one as before.
            if(oldTimeslot != newTimeslot) {

                feasable = true;
                for (Integer exam : chromosome.getGene(newTimeslot)) {
                    if (C[examToChange][exam] != 0) {
                        //there's a conflict
                        feasable = false;
                        break;
                    }
                }

            }else {
                feasable = false;
            }

            newTimeslot = (newTimeslot + 1) % chromosome.getTmax();
            numTimeslotsTested++;
        }while(!feasable && numTimeslotsTested < chromosome.getTmax());

        if(feasable){
            newTimeslot--;
            if(newTimeslot < 0){
                newTimeslot = newTimeslot + chromosome.getTmax();
            }

            //update the arrays
            chromosome.setExamTimeslot(examToChange, newTimeslot);

            //update the objective function
            chromosome.updateObjectiveFunction(C);
        }



    }
}
