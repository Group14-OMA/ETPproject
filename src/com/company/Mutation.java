package com.company;

import java.util.ArrayList;
import java.util.Random;

public class Mutation implements Runnable{
    private Chromosome chromosome;
    private Integer[][] C;

    public Mutation(Chromosome chromosome, Integer[][] c) {
        this.chromosome = chromosome;
        C = c;
    }

    public void setChromosome(Chromosome chromosome) {
        this.chromosome = chromosome;
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
        oldTimeslot = chromosome.getExamTimeslot(examToChange);

        //check feasability
        do {

            //I make sure to test all possible solutions before returning the same one as before.
            if(oldTimeslot != newTimeslot) {

                feasable = true;
                for (Integer exam : chromosome.getGene(newTimeslot)) {
                    if (C[examToChange][exam] != 0) {
                        //there's a conflict
                        feasable = false;
                    }
                }

            }else {
                feasable = false;
            }

            newTimeslot = (newTimeslot + 1) % chromosome.getTmax();
        }while(!feasable && numTimeslotsTested < chromosome.getTmax());

        if(feasable){
            newTimeslot --;

            //update the array of length E
            chromosome.setExamTimeslot(examToChange, newTimeslot);

            //update the array of length tmax
            ArrayList<Integer>[] newGeneList = chromosome.getGeneList();
            newGeneList[oldTimeslot].remove(examToChange);
            newGeneList[newTimeslot].add(examToChange);
            chromosome.setGeneList(newGeneList);
        }


    }
}
