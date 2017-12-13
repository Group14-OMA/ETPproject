package com.company;
import java.util.ArrayList;

public class PopulationGenerator {
	private Integer[] exams;
	private Integer timeslot;
	private Integer[] chromosome;
	private Integer numberOfSubset;
	private Integer[] subsetsDimensions;
	private Integer studentNum;
	
	Integer[][] conflictMatrix;
	Genes tempGeneList;
	ArrayList<Genes> geneListCollection = new ArrayList<Genes>();
	ArrayList<Genes> sortedGeneCollection = new ArrayList<Genes>();
	ArrayList<Chromosome> population = new ArrayList<Chromosome>();
	
	public PopulationGenerator(Integer[] exams, Integer timeslot,Integer[][] conflictMatrix, Integer studentNum ) {
		this.timeslot=timeslot;
		this.exams = exams;
		this.studentNum = studentNum;
		tempGeneList= new Genes(timeslot);
		this.chromosome = new Integer[exams.length];
		this.conflictMatrix=conflictMatrix;
	}
	public ArrayList<Chromosome> getPopulation(){
		return population;
	}
	public boolean catchConflict(int examIndex, int timeslot,Genes tempGeneList) {
		 //System.out.println("inside catch conflict, tempGeneList[timeslot].length=" + tempGeneList[timeslot].size());
		 //for (int potentialConflict: tempGeneList[timeslot]) {
		 for(int potentialConflict: tempGeneList.timeslotList(timeslot)) {
			 //System.out.println("examIndex= " + examIndex + " --><-- potentialConflictIndex " + (potentialConflict-1));
	    		if(this.conflictMatrix[examIndex][potentialConflict-1]!=0) {//potentialConflict=esame , potentialConfict-1= indece esame 
	    			//System.out.println("timeslot: "+timeslot+" exam: "+(examIndex+1) +" conflict with: "+potentialConflict);
	    			return true;
	    		}
	    	}
	    	return false;
	    	
	}
	
	//public int chromosomeGenerator(ArrayList<Genes> geneListCollection,Integer[] exams,Integer[] chromosome,Genes tempGeneList,Integer[][] conflictMatrix) {
	public void chromosomeGenerator() {
		 int lastTimeSlot=0;
		 int examIndex=0;
		 int subsetColor;
		 int subsetCounter=0;
		 boolean newSubset=false;
		 Integer[] subsetColors = new Integer[this.exams.length];
		 for (int x=0;x<subsetColors.length;x++) subsetColors[x]=0;	 
		 for(examIndex=0;examIndex<this.conflictMatrix.length;examIndex++) {
			 //System.out.println("xxxxxxxxxxxxxxxxxiteration: " + examIndex);
			 System.out.println();
			 //if(chromosome[examIndex]==null) {
			 Genes tempGeneList1= new Genes(this.tempGeneList.length());
			 if (true) {
				 if(subsetColors[examIndex]==0) {
					 subsetCounter++;
					 subsetColor=subsetCounter;
					 newSubset=true;
				 } else {
					 subsetColor=subsetColors[examIndex];
				 }
				 this.chromosome[examIndex]=0; //assign exam to first slot, since every first chromosome of a subset can't be confilicting 
				 //tempGeneList[0].add(exams[examIndex]);
				 tempGeneList1.add(0,exams[examIndex]);
				 chromosomeRecursion(lastTimeSlot,examIndex,tempGeneList1);
				 tempGeneList1.setSubset(subsetColor);
				 this.geneListCollection.add(tempGeneList1);
				 //print subset genList
				 
				 System.out.println("tempGeneList:" + "subset: " + tempGeneList1.getSubset());
				 /*for(int i=0;i<tempGeneList.length();i++) {
				    	System.out.print("["+i+"]");
				    	if (tempGeneList.size(i)!=0) {
				    		for (int k : tempGeneList.timeslotList(i)) {
				    			System.out.print("-->" + k);
				    		}
				    	}
				    	System.out.println();
				    }*/
				 tempGeneList1.printGene();
				 if (newSubset) {
					 for(int i=0;i<tempGeneList1.length();i++) {
						 for(int k: tempGeneList1.timeslotList(i)) {
							 subsetColors[k-1]=subsetColor;						 
						 }
					 }
				 }
				 // set subset section. chromosomeRecursion needs both subsetCounter and subsetArray
				 // subsetArray should be [1,1,1,2,3,3]
				 newSubset=false;
				 //tempGeneList.removeAll(); weird stuff happen, reminder look at the comment before the function
				 System.out.println();
				 for(int c=0;c<chromosome.length;c++) System.out.print(subsetColors[c]+"|");
				 for(int c=0;c<chromosome.length;c++) this.chromosome[c]=null;
				 
			 }
		 }
		 //for(int i=0;i<subsetColors.length;i++) System.out.print("["+subsetColors[i]+"]");	
		 this.numberOfSubset = subsetCounter;
		 this.subsetsDimensions = new Integer[this.numberOfSubset];
		 return ;
		 
	 }
	
	public boolean chromosomeRecursion(int lastTimeSlot,int examIndex,Genes tempGeneList) {
		 
		 int counter=0;
		 boolean exploration=false;
		 boolean placed=false;
		 		 
		 int nextExamIndex=0;
		 int timeslot=0;		
		 for(nextExamIndex=0;nextExamIndex<this.conflictMatrix.length;nextExamIndex++) {
			 if(this.conflictMatrix[examIndex][nextExamIndex]!=0 && this.chromosome[nextExamIndex]==null) {				 
				  for(timeslot=0;timeslot<tempGeneList.length();timeslot++) {
					  if(!catchConflict(nextExamIndex,timeslot,tempGeneList)){
						 //for(int i=0;i<chromosome.length;i++) System.out.print(" " + chromosome[i]);
						 //System.out.println();
								this.chromosome[nextExamIndex]=timeslot;
								tempGeneList.add(timeslot,this.exams[nextExamIndex]);
								//lastTimeSlot=timeslot;
								exploration=true;
								//System.out.println("timeslot= " + timeslot + " nextExamIndex= " + nextExamIndex + "| esame: "+ this.exams[examIndex] + " conflitta con esame" + this.exams[nextExamIndex]);
								placed=chromosomeRecursion(lastTimeSlot,nextExamIndex,tempGeneList);
								//if(placed) tempGeneList[timeslot].remove(exams[nextExamIndex]); //removes 3 , since 3 reconnects with 1 that is already placed
								//if(!placed) return false; //the program has placed all the elements of a subset
					 }
					 if (exploration) break;
				 } 
				 exploration=false;
			 }
		 }
		 return placed;
	 }
	public void subsetGenerator() {
		int dimension=0;
	    for(int subCounter=1;subCounter<=this.numberOfSubset;subCounter++ ) {
	    	this.subsetsDimensions[subCounter-1]=dimension;
	    	for (Genes myGene : this.geneListCollection) {
	    		if (myGene.getSubset()==subCounter) {
	    			this.sortedGeneCollection.add(myGene);
	    			dimension++;
	    		}
	    	}
	    	
	    }
	}
	public void createPopulation(int subsetCounter) {
		 int myCounter=subsetCounter;
		 if(myCounter==-1) myCounter++;
		 //System.out.println(myCounter);
		 //for(int i=0;i<this.chromosome.length;i++) System.out.print("["+this.chromosome[i]+"]");
		 //System.out.println();
		 if (myCounter==this.subsetsDimensions.length) {
			 //System.out.print("bzz");
			 Integer[] finalChromosome = new Integer[this.chromosome.length];
			 for (int i=0;i<chromosome.length;i++) finalChromosome[i]=this.chromosome[i];
			 Genes tempGene = chromosomeToGenes(finalChromosome);
			 Integer examNum = this.exams.length; //brutta roba
			 Chromosome myChromosome = new Chromosome(this.timeslot,examNum,this.studentNum,finalChromosome,tempGene.geneList());
			 this.population.add(myChromosome);	
			 return;
		 }
		 if(myCounter!=this.subsetsDimensions.length-1) {
			 for(int i=this.subsetsDimensions[myCounter];i<this.subsetsDimensions[myCounter+1];i++) {
				 this.chromosome=this.sortedGeneCollection.get(i).chromosomeTranslation(this.chromosome);
				 myCounter++;
				 createPopulation(myCounter);
				 myCounter--;
			 }
		 }else {			 
			 for(int i=this.subsetsDimensions[myCounter];i<this.sortedGeneCollection.size();i++) {
				 chromosome=this.sortedGeneCollection.get(i).chromosomeTranslation(this.chromosome);
				 myCounter++;
				  createPopulation(myCounter);
				  myCounter--;
			 }
		 }	 
		 return;
	 }
	 
	public Genes chromosomeToGenes(Integer[] chromosome) {
		Genes tempGene = new Genes(this.timeslot);
		for(int i=0;i<chromosome.length;i++) {
			tempGene.add(chromosome[i], (i+1));			
		}
		return tempGene;
	}
	
	 /*public void printPopulation() {
		 System.out.println("total number of chromsomes= " + this.population.size());
		 for(Chromosome myChromosome: this.population) {
			 System.out.println();
			  for(int i=0;i<myChromosome.getTimeSlotList().length;i++) {
				 System.out.print("<"+myChromosome.getTimeSlotList()[i]+">");
			 }
		 }
	     return;		 
	 }*/
	
	public void printPopulation() {
	 System.out.println("total number of chromsomes= " + this.population.size());
	 System.out.println("numero esami:" + this.exams.length);
	 System.out.println();
	 for(int i=0;i<this.subsetsDimensions.length;i++) System.out.print("-" + this.subsetsDimensions[i] + "-");
	 System.out.println();
	 for(int i=0;i<this.subsetsDimensions.length;i++) {
		 if(i==this.subsetsDimensions.length-1) System.out.println("subset#" + i + "dimesion:" + (this.exams.length-this.subsetsDimensions[i]));
		 else System.out.println("subset#" + i + "dimesion:" + (this.subsetsDimensions[i+1]-this.subsetsDimensions[i]));
	 }
    return;		 
}
	public void printConflictMatrix() {
		for(int i=0;i<this.conflictMatrix.length;i++) {
			for(int j=0;j<this.conflictMatrix.length;j++) {
				System.out.print(this.conflictMatrix[i][j]);
			}
			System.out.println();
		}
		return;
	}

}

