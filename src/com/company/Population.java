package com.company;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Comparator;

public class Population {

    private double bestChromosome;                       //BEST OBJ FUNCTION VALUE IN THE ACTUAL POPULATION
    private ArrayList<Chromosome> populationList;
    private Integer timeslots;
    private Integer studentNumber;
    private Integer examNumber;
    private Integer[][] conflictMatrix;


    //CONSTRUCTOR
    public Population(Integer timeSlots, Integer studentNumber, Integer examNumber, Integer[][] conflictMatrix) {
        populationList = new ArrayList<>();
        this.timeslots = timeSlots;
        this.studentNumber = studentNumber;
        this.examNumber = examNumber;
        this.conflictMatrix = conflictMatrix;
        bestChromosome = Math.pow(2, 5) * this.studentNumber;       //Worst case. For sure a normal feasible solution will have a lower value.
    }


    public double getBestObjectiveFunc() {
        return bestChromosome;
    }

    public Chromosome getChromosome(Integer n) {
        return populationList.get(n);
    }

    public void removeChromosome(Integer n) {
        this.populationList.remove(n);
    }

    public void addChromosome(Chromosome chromosomeToAdd) {
        this.populationList.add(chromosomeToAdd);
    }

    public ArrayList<Chromosome> getPopulationList() {
        return populationList;
    }

    //Set best value for objective function in the actual population
    public void setBestChromosome() {
        for (Chromosome c : populationList) {
            if (c.getObjFunc() < this.bestChromosome) {
                this.bestChromosome = c.getObjFunc();

            }
        }
    }
}


