package com.company;

import java.util.ArrayList;
import java.util.Random;

public class CrossOver implements Runnable{

	private Chromosome c1,c2;
	private Integer[][] C;
	
	@Override
	public void run() {

		Random rng = new Random();
		
		int b0 = rng.nextInt(this.c1.getExamNum());	//base index to start the cut
		int len = rng.nextInt(this.c2.getExamNum() - b0) + 1;	//lenght of the cut
		
		int temp;
		Integer[] l1 = c1.getTimeSlotList();
		Integer[] l2 = c2.getTimeSlotList();
		for(int i=b0; i < b0 + len; i++){	//loop to swap cells inside the cut
			temp = l1[i];
			l1[i] = l2[i];
			l2[i] = temp;	
		}

		int counter = 0; // Number time slots seen

		Boolean valid = false;	//boolean indicating if the current solution is feasible or not
		while(!valid){	//loop to check if the exams in the current solution are compatible each other
			valid = true;
			for(int i=b0;i<b0+len && valid;i++){
				for(int j=0; j<this.c1.getExamNum() && valid;j++){
					if(l1[i] == l1[j] && i!=j){
						if(C[i][j] != 0){
							valid = false;
							l1[i] = (l1[i]+1)%this.c1.getTmax();
							counter++;
						}
					}
				}

			}
			if(counter >= c1.getExamNum()-1) break;
		}
		
		//Possible infinite loop, probably need to check for a limited number of iterations
		counter = 0;
		//repeating for second chromosome, maybe too much computation 
		valid = false;
		while(!valid){	
			valid = true;
			for(int i=b0;i<b0+len && valid;i++){
				for(int j=0; j<this.c2.getExamNum() && valid;j++){
					if(l2[i] == l2[j] && i!=j){
						if(C[i][j] != 0){
							valid = false;
							l2[i] = (l2[i]+1)%this.c2.getTmax();
							counter++;
							//if(counter >= 12) break;
						}
					}
				}

			}
			if(counter >= c2.getExamNum()-1) break;
		}
		
		if(valid) {
			for (int i = b0; i < b0 + len; i++) {
				c1.setExamTimeslot(i, l1[i]);
				c2.setExamTimeslot(i, l2[i]);
			}

			c1.updateObjectiveFunction(C);
			c2.updateObjectiveFunction(C);

		}


		
//		System.out.println("C1");
//		for(int i=0;i<c1.getExamNum(); i++) {
//			System.out.println(" " + i + " " + c1.getTimeSlotList()[i]);
//		}
//		
//		System.out.println(" " + c1.getObjFunc());
//		System.out.println("C2");
//		for(int i=0;i<c2.getExamNum(); i++) {
//			System.out.println(" " + i + " " + c2.getTimeSlotList()[i]);
//		}
//		
//		System.out.println(" " + c2.getObjFunc());
	}


	public CrossOver(Chromosome c1,Chromosome c2, Integer[][] C){

		//CONFLICT MATRIX
		this.C = C;


		//FIRST CHROMOSOME
		this.c1 = new Chromosome(c1.getTmax(), c1.getExamNum(), c1.getStudentNum(), c1.getTimeSlotList(), new ArrayList[c1.getTmax()]);
		this.c1.setFitness(c1.getFitness());
		ArrayList<Integer>[] geneList1 = new ArrayList[c1.getGeneList().length];
		for(int i = 0; i < c1.getGeneList().length; i++){
			geneList1[i] = (ArrayList<Integer>) c1.getGeneList()[i].clone();
		}
		this.c1.setGeneList(geneList1);
		this.c1.setTimeSlotList(c1.getTimeSlotList().clone());
		this.c1.updateObjectiveFunction(this.C);


		//SECOND CHROMOSOME
		this.c2 = new Chromosome(c2.getTmax(), c2.getExamNum(), c2.getStudentNum(), c2.getTimeSlotList(), c2.getGeneList());
		this.c2.setFitness(c2.getFitness());
		ArrayList<Integer>[] geneList2 = new ArrayList[c2.getGeneList().length];
		for(int i = 0; i < c2.getGeneList().length; i++){
			geneList2[i] = (ArrayList<Integer>) c2.getGeneList()[i].clone();
		}
		this.c2.setGeneList(geneList2);
		this.c2.setTimeSlotList(c2.getTimeSlotList().clone());
		this.c2.updateObjectiveFunction(this.C);

	}


	public Chromosome getC1() {
		return c1;
	}




	public void setC1(Chromosome c1) {
		this.c1 = c1;
	}




	public Chromosome getC2() {
		return c2;
	}




	public void setC2(Chromosome c2) {
		this.c2 = c2;
	}




	public Integer[][] getC() {
		return C;
	}




	public void setC(Integer[][] c) {
		C = c;
	}
	
	
	
	
	
}
