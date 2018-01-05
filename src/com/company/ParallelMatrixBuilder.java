package com.company;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class ParallelMatrixBuilder implements Runnable {

    private Integer indiceEsame;
    private LinkedList<String>[] conflitti;

    private Integer[] cMatrix;




    public ParallelMatrixBuilder(Integer indiceEsame, LinkedList<String>[] conflitti, Integer[] cMatrix) {
        this.indiceEsame = indiceEsame;
        this.conflitti = conflitti;
        this.cMatrix=cMatrix;

    }

    public void run() {
        for(int x=0;x<conflitti.length; x++) {
            if(x==indiceEsame) {
                cMatrix[x]=0;
            }
            else {
                Set<String> tmp=new HashSet<>(conflitti[indiceEsame]);
                tmp.retainAll(conflitti[x]);

                cMatrix[x]=tmp.size();
            }
        }
    }


}
