package com.company;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class ParallelMatrixBuilder implements Runnable {

    private Integer indiceEsame;
    private LinkedList<String>[] conflitti;
    private Integer[] row;


    public Integer[] getRow() {
        return row;
    }

    public Integer getIndiceEsame() {
        return indiceEsame;
    }




    public ParallelMatrixBuilder(Integer indiceEsame, LinkedList<String>[] conflitti) {
        this.indiceEsame = indiceEsame;
        this.conflitti = conflitti;
        row=new Integer[conflitti.length];



    }

    public void run() {

        for(int x=0;x<conflitti.length; x++) {
            if(x==indiceEsame) {
               row[x]=0;

            }
            else {
                Set<String> tmp=new HashSet<>(conflitti[indiceEsame]);
                tmp.retainAll(conflitti[x]);

                row[x]=tmp.size();

            }
        }
    }


}
