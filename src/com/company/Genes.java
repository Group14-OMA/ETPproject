package com.company;

import java.util.ArrayList;

public class Genes{
    int numtimeslot;
    int subset=0;
    ArrayList<Integer>[] myGeneList;
    public Genes(int timeslot) {
        this.numtimeslot=timeslot;
        this.myGeneList=  (ArrayList<Integer>[])new ArrayList[timeslot];
        for(int i=0;i<myGeneList.length;i++) myGeneList[i]=new ArrayList<Integer>();
    }

    public void add(int timeslot,Integer exam) {
        myGeneList[timeslot].add(exam);
    }
    public void remove(int timeslot,Integer exam) {
        myGeneList[timeslot].remove(exam);
    }
    public int length() {
        return myGeneList.length;
    }
    public int size(int timeslot) {
        return myGeneList[timeslot].size();
    }

    public ArrayList<Integer> timeslotList(int timeslot){
        return myGeneList[timeslot];
    }

    public void removeAll() {
        for(int i=0;i<this.myGeneList.length;i++) {
            this.myGeneList[i].clear();
        }
    }

    //TODO WE HAVE TO SOLVE THIS SHIT
    public ArrayList<Integer>[] geneList(){
        /*for(int i = 0; i < this.myGeneList.length; i++){
            for(int j = 0; j < this.myGeneList[i].size(); j++){
                Integer tmp = this.myGeneList[i].get(j);
                tmp--;
                this.myGeneList[i].remove(j);
                this.myGeneList[i].add(j, tmp);

            }
        }
        return this.myGeneList;*/
    	return this.myGeneList;
    }
    
    public void setSubset(int sub) {
        this.subset = sub;
    }

    public int getSubset() {
        return this.subset;
    }

    public Integer[] ToSubChromosome(int numExams) {
        Integer[] subChromosome = new Integer[numExams];
        for(int timeslot=0;timeslot<this.myGeneList.length; timeslot++) {
            for(int exam: this.myGeneList[timeslot]) {
                //examIndex = exam-1;
                subChromosome[exam-1]=timeslot;
            }
        }
        return subChromosome;
    }

    public Integer[] chromosomeTranslation(Integer[] chromosome) {
        for(int timeslot=0;timeslot<this.myGeneList.length; timeslot++) {
            for(int exam: this.myGeneList[timeslot]) {
                //examIndex = exam-1;
                chromosome[exam]=timeslot;
            }
        }
        return chromosome;
    }

    public void printGene() {
        for(int timeslot=0;timeslot<this.myGeneList.length;timeslot++) {
            System.out.print("["+timeslot+"]");
            if(this.myGeneList[timeslot].size()!=0){
                for(int k: this.myGeneList[timeslot]) {
                    System.out.print("-->" + k);
                }

            }
            System.out.println();
        }
        System.out.println();
    }



}

