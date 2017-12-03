package com.company;

import java.util.ArrayList;
/**
 * Quick and dirty version of Objective function, so that Tier2 can be created and tested.
 * Probably can be improved, to reduce overall calculation time.
 * */

public class ObjectiveFunction {

    public Double calculateObjectiveFunction(Chromosome chromosome, Integer tmax, Integer s, Integer C[][]){
        Double value = 0.0;

        for(Integer i = 0; i < chromosome.getTmax(); i++){
            ArrayList<Integer> exams = chromosome.getGene(i);
            if(exams != null)
                for(Integer exam : exams){
                    for(Integer j = i+1; j < i+6 && j < tmax; j++){
                        if(chromosome.getGene(j)!= null)
                            for(Integer exam2 : chromosome.getGene(j)){
                                value += C[exam-1][exam2-1] * Math.pow(2, (5-(j-i)));
                            }
                    }
                }

        }

        return value/8;
    }
}
