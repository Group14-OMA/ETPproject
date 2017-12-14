package com.company;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Comparator;

public class Population {

    private double bestChromosome;                       //BEST OBJ FUNCTION VALUE IN THE ACTUAL POPULATION
    private ArrayList<Chromosome> populationList;
    private Integer studentNumber;
    private Integer[][] conflictMatrix;


    //CONSTRUCTOR
    public Population( Integer studentNumber, Integer[][] conflictMatrix, ArrayList<Chromosome> populationList) {
        this.populationList = populationList;
        this.studentNumber = studentNumber;
        this.conflictMatrix = conflictMatrix;
        bestChromosome = Math.pow(2, 5) * this.studentNumber;       //Worst case. For sure a normal feasible solution will have a lower value.
    }

    public Population(){
        this(0, null, null);
    }

    public double getBestObjectiveFunc() {
        return bestChromosome;
    }

    public Chromosome getChromosome(Integer n) {
        return populationList.get(n);
    }

    public void removeChromosome(int n) {
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
        //reset to initial value at each new generation
        bestChromosome = Math.pow(2, 5) * this.studentNumber;
        for (Chromosome c : populationList) {
            if (c.getObjFunc() < this.bestChromosome) {
                this.bestChromosome = c.getObjFunc();
            }
        }
    }

    // ORDERING POPULATION BASED ON FITNESS, INCREASING ORDER
    public void sortPopulation(){
        populationList.sort(Comparator.comparing(Chromosome::getFitness));
    }

    public void setPopulationList(ArrayList<Chromosome> populationList) {
        this.populationList = populationList;
    }

    public Integer getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(Integer studentNumber) {
        this.studentNumber = studentNumber;
    }

    public Integer[][] getConflictMatrix() {
        return conflictMatrix;
    }

    public void setConflictMatrix(Integer[][] conflictMatrix) {
        this.conflictMatrix = conflictMatrix;
    }

}


