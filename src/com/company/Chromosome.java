package com.company;

import java.util.ArrayList;

public class Chromosome {

    private Integer objFunc;
    private ArrayList<Integer> timeSlotList;
    private ArrayList<ArrayList<Integer>> chromosomeList;

    public Chromosome(ArrayList<Integer> timeSlotList, ArrayList<ArrayList<Integer>> chromosomeList) {
        this.timeSlotList = timeSlotList;
        this.chromosomeList = chromosomeList;
    }

    public Integer getObjFunc() {
        return objFunc;
    }

    public void setObjFunc(Integer objFunc) {
        this.objFunc = objFunc;
    }

    public ArrayList<Integer> getTimeslotList() {
        return timeSlotList;
    }

    public void setTimeslotList(ArrayList<Integer> timeslotList) {
        this.timeSlotList = timeslotList;
    }

    public ArrayList<ArrayList<Integer>> getChromosomeList() {
        return chromosomeList;
    }

    public void setChromosomeList(ArrayList<ArrayList<Integer>> chromosomeList) {
        this.chromosomeList = chromosomeList;
    }
}
