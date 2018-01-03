package com.company;

import java.util.ArrayList;

public class Chromosome {

    private Double objFunc;
    private Double fitness;
    private Integer tmax;
    private Integer examNum;
    private Integer studentNum;
    private Integer[] timeSlotList; //Length examNum, each cell contains the timeslot of the corresponding exam (timeSlotList[0] == 3 --> Exam 1 is in time slot 3).
    private ArrayList<Integer>[] geneList; //Length tmax, cells contain exams for the corresponding time slot (geneList[2] == {0,3,5} --> Exams 1, 4, 6 are in the 3rd time slot)


    public Chromosome(){
        this.objFunc = 0.0;
        this.fitness = 0.0;
        this.tmax = 0;
        this.examNum = 0;
        this.studentNum = 0;
        this.timeSlotList = null;
        this.geneList = null;
    }

    public Chromosome(Integer tmax, Integer examNum, Integer studentNum, Integer[] timeSlotList, ArrayList<Integer>[] geneList) {
        this.tmax = tmax;
        this.examNum = examNum;
        this.studentNum = studentNum;
        this.timeSlotList = timeSlotList;
        this.geneList = geneList;
    }

    public Boolean isValid(Integer C[][]){
        Integer conflicts = 0;
        for(int i = 0; i < tmax; i++) {
            for (Integer exam1 : geneList[i]) {
                for (Integer exam2 : geneList[i]) {
                    if (exam1 != exam2 && C[exam1][exam2] != 0) {
                        conflicts++;
                    }
                }
            }
        }
        return conflicts == 0;
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

    public Integer getExamTimeslot(Integer exam){
        return timeSlotList[exam];
    }

    //updates both arrays (timeSlotList and geneList) to avoid having diverging values.
    public void setExamTimeslot(Integer exam, Integer newTimeslot){
        Integer oldTimeslot = timeSlotList[exam];
        timeSlotList[exam] = newTimeslot;

        geneList[oldTimeslot].remove(exam);
        geneList[newTimeslot].add(exam);

    }

    public ArrayList<Integer> getGene(Integer i){
        return geneList[i];
    }

    public void updateObjectiveFunction(Integer C[][]){
        Double value = 0.0;

        for(Integer i = 0; i < this.tmax; i++) {
            ArrayList<Integer> exams = this.getGene(i);
            for (Integer exam : exams) {
                //make sure that timeslotList is correct
                timeSlotList[exam] = i;
                for (Integer j = i + 1; j < i + 6 && j < tmax; j++) {
                    if (this.getGene(j) != null) {
                        for (Integer exam2 : this.getGene(j)) {
                            value += C[exam][exam2] * Math.pow(2, (5 - (j - i)));
                        }
                    }
                }
            }
        }

        this.objFunc = value/this.studentNum;
    }

}
