package com.company;

import java.util.ArrayList;

public class Chromosome {

    private Double objFunc;
    private Double fitness;
    private Integer tmax;
    private Integer examNum;
    private Integer studentNum;
    private Integer[] timeSlotList;
    private ArrayList<Integer>[] geneList;


    public Chromosome(Integer tmax, Integer examNum, Integer studentNum, Integer[] timeSlotList, ArrayList<Integer>[] geneList) {
        this.tmax = tmax;
        this.examNum = examNum;
        this.studentNum = studentNum;
        this.timeSlotList = timeSlotList;
        this.geneList = geneList;
    }

    public Double getObjFunc() {
        return objFunc;
    }

    public void setObjFunc(Double objFunc) {
        this.objFunc = objFunc;
    }

    public Integer[] getTimeSlotList() {
        return timeSlotList;
    }

    public void setTimeSlotList(Integer[] timeSlotList) {
        this.timeSlotList = timeSlotList;
    }

    public ArrayList<Integer>[] getGeneList() {
        return geneList;
    }

    public void setGeneList(ArrayList<Integer>[] geneList) {
        this.geneList = geneList;
    }

    public Integer getTmax() {
        return tmax;
    }

    public Integer getExamNum() {
        return examNum;
    }

    public Integer getStudentNum() {
        return studentNum;
    }


    public Double getFitness() {
        return fitness;
    }

    public void setFitness(Double fitness) {
        this.fitness = fitness;
    }


    public ArrayList<Integer> getGene(Integer i){
        return geneList[i];
    }

    public void updateObjectiveFunction(Integer C[][]){
        Double value = 0.0;

        for(Integer i = 0; i < this.tmax; i++){
            ArrayList<Integer> exams = this.getGene(i);
            if(exams != null)
                for(Integer exam : exams){
                    for(Integer j = i+1; j < i+6 && j < tmax; j++){
                        if(this.getGene(j)!= null)
                            for(Integer exam2 : this.getGene(j)){
                                value += C[exam-1][exam2-1] * Math.pow(2, (5-(j-i)));
                            }
                    }
                }

        }

        this.objFunc = value/this.studentNum;
    }
}
