package com.company;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Comparator;

public class Population {

    private double bestChromosome;                       //BEST OBJ FUNCTION VALUE IN THE ACTUAL POPULATION
    private ArrayList<Chromosome> populationList;

    //CONSTRUCTOR
    public Population(){
        bestChromosome = 0;
        populationList = new ArrayList<>();
    }



    public double getBestObjectiveFunc() {
        return bestChromosome;
    }

    public Chromosome getChromosome(Integer n){
        return populationList.get(n);
    }

    public void removeChromosome(Integer n){ this.populationList.remove(n); }

    public void addChromosome(Chromosome chromosomeToAdd){
        this.populationList.add(chromosomeToAdd);
    }

    public ArrayList<Chromosome> getPopulationList() {
        return populationList;
    }

    //Set best value for objective function in the actual population
    public void setBestChromosome() {
        for(Chromosome c : populationList){
            if(c.getObjFunc() > this.bestChromosome){ //TODO: c.getObjFunc() < this.bestChromosome, and I need to make sure that at the 1st cycle, bestChromosome is updated (otherwise it will stay at 0)
                this.bestChromosome = c.getObjFunc();
            }
        }
    }
}


