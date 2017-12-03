package com.company;

import java.util.ArrayList;

public class Chromosome {

    private Double objFunc;
    private Integer tmax;
    private ArrayList<Integer> timeSlotList;
    private ArrayList<ArrayList<Integer>> geneList;

    public Chromosome(ArrayList<Integer> timeSlotList, ArrayList<ArrayList<Integer>> geneList, Integer tmax) {
        this.timeSlotList = timeSlotList;
        this.geneList = geneList;
        this.tmax = tmax;

    }

    public Double getObjFunc() {
        return objFunc;
    }

    public void setObjFunc(Double objFunc) {
        this.objFunc = objFunc;
    }

    public ArrayList<Integer> getTimeslotList() {
        return timeSlotList;
    }

    public void setTimeslotList(ArrayList<Integer> timeslotList) {
        this.timeSlotList = timeslotList;
    }

    public ArrayList<ArrayList<Integer>> getGeneList() {
        return geneList;
    }

    public ArrayList<Integer> getGene(Integer i){
        return geneList.get(i);
    }

    public void setGeneList(ArrayList<ArrayList<Integer>> geneList) {
        this.geneList = geneList;
    }

    public Integer getTmax() {
        return tmax;
    }
}
