package com.company;

import java.util.ArrayList;

public class Population {

    private ArrayList<Chromosome> populationList;

    public Chromosome getChromosome(Integer n){
        return populationList.get(n);
    }

    public void addChromosome(Chromosome chromosomeToAdd){
        this.populationList.add(chromosomeToAdd);
    }

    public ArrayList<Chromosome> getPopulationList() {
        return populationList;
    }
}
