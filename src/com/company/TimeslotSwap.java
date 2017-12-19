package com.company;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class TimeslotSwap implements Runnable {
    private Chromosome chromosome;
    private Integer[][] C;

    public TimeslotSwap(Chromosome c, Integer[][] C) {
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


    @Override
    public void run() {
        //swap two random timeslots. No need to check feasability, we are not changing the groups of exams
        Random rnd = ThreadLocalRandom.current();
        ArrayList<Integer>[] geneList;
        ArrayList<Integer> tmp;
        Integer timeslot1 = rnd.nextInt(chromosome.getTmax());
        Integer timeslot2;
        //guarantee that the two timeslots are different
        do{
            timeslot2 = rnd.nextInt(chromosome.getTmax());
        }while(timeslot1 == timeslot2);

        geneList = chromosome.getGeneList();
        tmp = geneList[timeslot1];
        geneList[timeslot1] = geneList[timeslot2];
        geneList[timeslot2] = tmp;

        this.chromosome = new Chromosome(chromosome.getTmax(), chromosome.getExamNum(), chromosome.getStudentNum(), createTimeslotList(geneList),geneList);
        this.chromosome.updateObjectiveFunction(C);


    }

    private Integer[] createTimeslotList(ArrayList<Integer>[] geneList){
        Integer[] timeslotList = new Integer[chromosome.getExamNum()];

        for(Integer i = 0 ; i < chromosome.getTmax(); i++){
            for(Integer exam : geneList[i]){
                timeslotList[exam] = i;
            }
        }

        return timeslotList;
    }

    public void setChromosome(Chromosome c) {
        this.chromosome = new Chromosome(c.getTmax(), c.getExamNum(), c.getStudentNum(), c.getTimeSlotList(), c.getGeneList());
    }

    public Chromosome getChromosome() {
        return chromosome;
    }
}
