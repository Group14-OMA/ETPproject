package com.company;

import java.util.ArrayList;

/*
*************************************************
*               TEST CLASS
* ***********************************************
*/

public class TestClass {

    //IT CREATES 2 CHROMOSOMES, INITIALIZE A POPULATION, RUN TIER 1 METHOD
    static public void chromosomesTest(){


        Integer[][] C = new Integer[4][4];

        //SETTING ALL CELLS TO 0
        for(int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++)
                C[i][j] = 0;
        }

        //SETTING CONFLICT
        C[0][1] = 2;
        C[0][2] = 3;
        C[1][0] = 2;
        C[1][2] = 2;
        C[2][0] = 3;
        C[2][1] = 2;

        //FIRST CHROMOSOME
        ArrayList<Integer> gene1 = new ArrayList<>();
        gene1.add(0);
        gene1.add(3);
        ArrayList<Integer> gene2 = new ArrayList<>();
        gene2.add(1);
        ArrayList<Integer> gene3 = new ArrayList<>();
        gene3.add(2);

        ArrayList<Integer>[] geneList = new ArrayList[6];
        geneList[0] = gene1;
        geneList[1]= new ArrayList<>();
        geneList[2] = gene2;
        geneList[3]= new ArrayList<>();
        geneList[4]= new ArrayList<>();
        geneList[5]= gene3;

        Integer[] timeslotList = new Integer[4];
        timeslotList[0] = 0;
        timeslotList[1] = 2;
        timeslotList[2] = 5;
        timeslotList[3] = 0;

        Chromosome chromosome = new Chromosome(6, 4,8,timeslotList,geneList);
        chromosome.updateObjectiveFunction(C);



        //SECOND CHROMOSOME
        ArrayList<Integer> gene1_2 = new ArrayList<>();
        gene1_2.add(0);
        ArrayList<Integer> gene2_2 = new ArrayList<>();
        gene2_2.add(3);
        ArrayList<Integer> gene3_2 = new ArrayList<>();
        gene3_2.add(2);
        ArrayList<Integer> gene4_2 = new ArrayList<>();
        gene4_2.add(1);

        ArrayList<Integer>[] geneList_2 = new ArrayList[6];
        geneList_2[0] = gene1_2;
        geneList_2[1]= new ArrayList<>();           //EMPTY
        geneList_2[2] = gene2_2;
        geneList_2[3]= new ArrayList<>();           //EMPTY
        geneList_2[4]= gene4_2;
        geneList_2[5]= gene3_2;

        Integer[] timeslotList_2 = new Integer[4];
        timeslotList_2[0] = 0;
        timeslotList_2[1] = 4;
        timeslotList_2[2] = 5;
        timeslotList_2[3] = 2;

        Chromosome chromosome1 = new Chromosome(6, 4, 8, timeslotList_2, geneList_2);
        chromosome1.updateObjectiveFunction(C);


        Population p = new Population(8, C);
        p.addChromosome(chromosome);
        p.addChromosome(chromosome1);
        Tier_1 tier_1_Prova = new Tier_1(p, C);
        tier_1_Prova.first_tier();
    }


}
