package com.company;

import java.util.ArrayList;

public class Chromosome {

    private Integer objFunc;
    private ArrayList<Integer> timeSlotList;
    private ArrayList<ArrayList<String>> chromosomeList;

    public Chromosome(ArrayList<Integer> timeSlotList, ArrayList<ArrayList<String>> chromosomeList) {
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

    public ArrayList<ArrayList<String>> getChromosomeList() {
        return chromosomeList;
    }

    public void setChromosomeList(ArrayList<ArrayList<String>> chromosomeList) {
        this.chromosomeList = chromosomeList;
    }
}
