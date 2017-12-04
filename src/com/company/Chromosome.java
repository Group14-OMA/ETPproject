package com.company;

import java.util.ArrayList;

public class Chromosome {

    private Double objFunc;
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


    public ArrayList<Integer> getGene(Integer i){
        return geneList[i];
    }
}
