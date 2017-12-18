package com.company;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class PopulationGenerator {
	private int counter=0; //testing
    private Integer[] exams;
    private Integer timeslot;
    private Integer[] chromosome;
    private Integer numberOfSubset;
    private Integer[] subsetsDimensions;
    private Integer studentNum;
    private ArrayList<Integer>[] conflictList;


    private Integer[][] conflictMatrix;
    private Integer[][] fastConflictMatrix;
    private Genes tempGeneList;
    private ArrayList<Genes> geneListCollection = new ArrayList<Genes>();
    private ArrayList<Genes> sortedGeneCollection = new ArrayList<Genes>();
    private ArrayList<Chromosome> population = new ArrayList<Chromosome>();
    private Integer[][] optimizedConflictMatrix;
    


    public void generatePop(){
    	conflictList();
    	buildFastConflictMatrix();
    	//printFastConflictMatrix();
    	fastGenerator();
        //chromosomeGenerator();
        //subsetGenerator();
        //createPopulation(-1);
        //printPopulation();
        //printConflictMatrix();
    }

    
    public PopulationGenerator(Integer[] exams, Integer timeslot, Integer[][] conflictMatrix, Integer studentNum) {
        this.timeslot = timeslot;
        this.exams = exams;
        this.studentNum = studentNum;
        tempGeneList = new Genes(timeslot);
        this.conflictList = (ArrayList<Integer>[])new ArrayList[exams.length];
        this.chromosome = new Integer[exams.length];
        this.conflictMatrix = conflictMatrix;
        this.fastConflictMatrix = new Integer[exams.length][timeslot];
        
        
    }
    
    public void buildOptimizedConflictMatrix() {
    	optimizedConflictMatrix = new Integer[conflictList.length][];
    	for(int i=0;i<conflictList.length;i++) {
    		this.optimizedConflictMatrix[i] = new Integer[conflictList[i].size()];
    		for(int j=0; j<conflictList[i].size();j++) {
    			this.optimizedConflictMatrix[i][j]=conflictList[i].get(j);
    		}
    	}
    	return;
    }
    
    public void conflictList() {    	
    	for(int i=0;i<this.conflictMatrix.length;i++) {
    		conflictList[i]=new ArrayList<Integer>();
    		for(int j=0;j<this.conflictMatrix.length;j++) {
    			if(conflictMatrix[i][j]!=0) {
    				this.conflictList[i].add(j); 
    			}
    		}
    	}
    }
    
    public void buildFastConflictMatrix() {
    	
    	for(int i=0;i<this.exams.length;i++) {
    		for(int j=0;j<this.timeslot;j++) {
    			this.fastConflictMatrix[i][j]=0;
    		}
    	}
    }
    
    public void printFastConflictMatrix() {
    	for(int i=0;i<this.exams.length;i++) {
    		System.out.println();
    		System.out.print("exam" + (i+1)+ " ");
    		for(int j=0;j<this.timeslot;j++) {
    			System.out.print(fastConflictMatrix[i][j]);
    		}
    	}
    }
    public void printConflictList() {
    	for(int i=0;i<conflictList.length;i++) {
    		for(Integer exam: conflictList[i]) {
    			System.out.print("->[" + (exam+1) + "]"); //PRINT THE EXAM NUMBER NOT THE EXAM INDEX
    		}
    		System.out.println();
    	}
    }
    
    public void printOptimizedConflictMatrix() {
    	for(int i=0; i<this.optimizedConflictMatrix.length;i++) {
    		for(int j=0;j<this.optimizedConflictMatrix[i].length;j++) {
    			System.out.print("["+optimizedConflictMatrix[i][j]+"]"); //UNLIKE PRINTCONFLICT MATRIX IT PRINTS THE EXAMS INDEXES NOT THE EXAM THEMSELVES
    		}
    		System.out.println(" --" + this.optimizedConflictMatrix[i].length);
    	}
    }

    public boolean catchConflict(int examIndex, int timeslot, Genes tempGeneList) {
        for (int potentialConflict : tempGeneList.timeslotList(timeslot)) {
            if (this.conflictMatrix[examIndex][potentialConflict] != 0) {
                return true;
            }
        }
        return false;

    }
   
    //public int chromosomeGenerator(ArrayList<Genes> geneListCollection,Integer[] exams,Integer[] chromosome,Genes tempGeneList,Integer[][] conflictMatrix) {
    public void chromosomeGenerator() {
        int examIndex = 0;
        int subsetColor;
        int subsetCounter = 0;
        boolean newSubset = false;
        Integer[] subsetColors = new Integer[this.exams.length];
        for (int x = 0; x < subsetColors.length; x++) subsetColors[x] = 0;
        for (examIndex = 0; examIndex < this.conflictMatrix.length; examIndex++) {
            //System.out.println("xxxxxxxxxxxxxxxxxiteration: " + examIndex);
            //System.out.println();
            //if(chromosome[examIndex]==null) {
            Genes tempGeneList1 = new Genes(this.tempGeneList.length());
            if (true) {
                if (subsetColors[examIndex] == 0) {
                    subsetCounter++;
                    subsetColor = subsetCounter;
                    newSubset = true;
                } else {
                    subsetColor = subsetColors[examIndex];
                }
                this.chromosome[examIndex] = 0; //assign exam to first slot, since every first chromosome of a subset can't be confilicting
                //tempGeneList[0].add(exams[examIndex]);
                tempGeneList1.add(0, examIndex);
                //chromosomeRecursion(examIndex, tempGeneList1);
                newchromosomeRecursion(examIndex, tempGeneList1);
                tempGeneList1.setSubset(subsetColor);
                this.geneListCollection.add(tempGeneList1);
                //print subset genList                
				 tempGeneList1.printGene();
				 if (newSubset) {
					 for(int i=0;i<tempGeneList1.length();i++) {
						 for(int k: tempGeneList1.timeslotList(i)) {
							 subsetColors[k]=subsetColor;						 
						 }
					 }
				 }
				 // set subset section. chromosomeRecursion needs both subsetCounter and subsetArray
				 // subsetArray should be [1,1,1,2,3,3]
				 newSubset=false;
				 //tempGeneList.removeAll(); weird stuff happen, reminder look at the comment before the function
				 //System.out.println();
				 //for(int c=0;c<chromosome.length;c++) System.out.print(subsetColors[c]+"|");
				 for(int c=0;c<chromosome.length;c++) this.chromosome[c]=null;
				 
			 }
		 }
		 //for(int i=0;i<subsetColors.length;i++) System.out.print("["+subsetColors[i]+"]");	
		 this.numberOfSubset = subsetCounter;
		 this.subsetsDimensions = new Integer[this.numberOfSubset];
		 return ;
		 
	 }
	
	public boolean chromosomeRecursion(int examIndex,Genes tempGeneList) {
		 
		 int counter=0;
		 boolean exploration=false;
		 boolean placed=false;
		 		 
		 int nextExamIndex=0;
		 int timeslot=0;		
		 for(nextExamIndex=0;nextExamIndex<this.conflictMatrix.length;nextExamIndex++) {
			 if(this.conflictMatrix[examIndex][nextExamIndex]!=0 && this.chromosome[nextExamIndex]==null) {				 
				  for(timeslot=0;timeslot<tempGeneList.length();timeslot++) {
					  if(!catchConflict(nextExamIndex,timeslot,tempGeneList)){
						 		this.chromosome[nextExamIndex]=timeslot;
								tempGeneList.add(timeslot,nextExamIndex);
								exploration=true;
								//System.out.println("timeslot= " + timeslot + " nextExamIndex= " + nextExamIndex + "| esame: "+ this.exams[examIndex] + " conflitta con esame" + this.exams[nextExamIndex]);
								placed=chromosomeRecursion(nextExamIndex,tempGeneList);
								
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
			 myChromosome.updateObjectiveFunction(conflictMatrix);
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
			tempGene.add(chromosome[i], (i));			
		}
		return tempGene;
	}
	
	 public void printPopulation() {
        System.out.println("total number of chromsomes= " + this.population.size());
        System.out.println("numero esami:" + this.exams.length);
        System.out.println();
        for (int i = 0; i < this.subsetsDimensions.length; i++) System.out.print("-" + this.subsetsDimensions[i] + "-");
        System.out.println();
        for (int i = 0; i < this.subsetsDimensions.length; i++) {
            if (i == this.subsetsDimensions.length - 1)
                System.out.println("subset#" + i + "dimesion:" + (this.exams.length - this.subsetsDimensions[i]));
            else
                System.out.println("subset#" + i + "dimesion:" + (this.subsetsDimensions[i + 1] - this.subsetsDimensions[i]));
        }
        return;
    }

    public void printConflictMatrix() {
        for (int i = 0; i < this.conflictMatrix.length; i++) {
            for (int j = 0; j < this.conflictMatrix.length; j++) {
                System.out.print(this.conflictMatrix[i][j]);
            }
            System.out.println();
        }
        return;
    }



    //SETTER & GETTER

    public Integer[] getExams() {
        return exams;
    }

    public void setExams(Integer[] exams) {
        this.exams = exams;
    }

    public Integer getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(Integer timeslot) {
        this.timeslot = timeslot;
    }

    public Integer[] getChromosome() {
        return chromosome;
    }

    public void setChromosome(Integer[] chromosome) {
        this.chromosome = chromosome;
    }

    public Integer getNumberOfSubset() {
        return numberOfSubset;
    }

    public void setNumberOfSubset(Integer numberOfSubset) {
        this.numberOfSubset = numberOfSubset;
    }

    public Integer[] getSubsetsDimensions() {
        return subsetsDimensions;
    }

    public void setSubsetsDimensions(Integer[] subsetsDimensions) {
        this.subsetsDimensions = subsetsDimensions;
    }

    public Integer getStudentNum() {
        return studentNum;
    }

    public void setStudentNum(Integer studentNum) {
        this.studentNum = studentNum;
    }

    public Integer[][] getConflictMatrix() {
        return conflictMatrix;
    }

    public void setConflictMatrix(Integer[][] conflictMatrix) {
        this.conflictMatrix = conflictMatrix;
    }

    public Genes getTempGeneList() {
        return tempGeneList;
    }

    public void setTempGeneList(Genes tempGeneList) {
        this.tempGeneList = tempGeneList;
    }

    public ArrayList<Genes> getGeneListCollection() {
        return geneListCollection;
    }

    public void setGeneListCollection(ArrayList<Genes> geneListCollection) {
        this.geneListCollection = geneListCollection;
    }

    public ArrayList<Genes> getSortedGeneCollection() {
        return sortedGeneCollection;
    }

    public void setSortedGeneCollection(ArrayList<Genes> sortedGeneCollection) {
        this.sortedGeneCollection = sortedGeneCollection;
    }

    public void setPopulation(ArrayList<Chromosome> population) {
        this.population = population;
    }

    public ArrayList<Chromosome> getPopulation() {
        return population;
    }

    public void newchromosomeRecursion(int examIndex,Genes tempGeneList) {
		 
		 boolean exploration=false;
		 int timeslot=0;
		 
		 for(Integer nextExamIndex: this.conflictList[examIndex]) {
			 if(this.chromosome[nextExamIndex]==null) {				 
				  for(timeslot=0;timeslot<tempGeneList.length();timeslot++) {
					  if(!catchConflict(nextExamIndex,timeslot,tempGeneList)){
						 		this.chromosome[nextExamIndex]=timeslot;
								tempGeneList.add(timeslot,nextExamIndex);
								exploration=true;
								newchromosomeRecursion(nextExamIndex,tempGeneList);
								
					 }
					 if (exploration) break;
				 } 
				 exploration=false;
			 }
		 }
		 
		 /*
		 for(int nextExamIndex=0;nextExamIndex<this.optimizedConflictMatrix[examIndex].length;nextExamIndex++) {
			 if(this.chromosome[nextExamIndex]==null) {				 
				  for(timeslot=0;timeslot<tempGeneList.length();timeslot++) {
					  if(!catchConflict(nextExamIndex,timeslot,tempGeneList)){
						 		this.chromosome[nextExamIndex]=timeslot;
								tempGeneList.add(timeslot,nextExamIndex);
								exploration=true;
								newchromosomeRecursion(nextExamIndex,tempGeneList);
								
					 }
					 if (exploration) break;
				 } 
				 exploration=false;
			 }
		 }
		 */
		 return;
	 }
    
    public void fastGenerator() {
    	int examsNumber = this.exams.length;
    	int timeslotSize = this.timeslot;
    	int subsetCounter=0;
    	Integer[] subsetColor = new Integer[examsNumber];
    	Integer[] chromosome = new Integer[examsNumber];
    	Genes tempGene = new Genes(this.timeslot);
    	for(int i=0;i<subsetColor.length;i++) {
    		subsetColor[i]=0;
    		chromosome[i]=null;
    	}
    	for(int examIndex=0;examIndex<examsNumber;examIndex++) {
    		
    		if(subsetColor[examIndex]==0) {
    			System.out.println("new subset");
    			subsetCounter++;
    			subsetColor[examIndex]=subsetCounter;
    			chromosome[examIndex]=0;
    			tempGene.add(0, examIndex);
    			fillFastConflictMatrix(examIndex,0);
    			if (examIndex==463) {
    				tempGene.printGene();
    				for(int i: conflictList[463]) {
    					System.out.println();
    					System.out.print(i+"-");
    				}
    				printFastConflictMatrix();
    				return;
    			}
    			fastRecursion(examIndex,subsetCounter,chromosome,subsetColor,tempGene);
    		}    		
    	}
    	System.out.print("first chromosome as array of integer:");
    	for(int i=0;i<chromosome.length;i++) {
 
    		//System.out.println((i+1)+" " + (chromosome[i]+1) + " subset: " + subsetColor[i]);
    		System.out.println((i+1)+" " + (chromosome[i]+1));
    	}
    	//TEST
    	//END TEST
    	return;
    }
    	
    public void fastRecursion(int examIndex,int subsetCounter,Integer[] chromosome,Integer[] subsetColor,Genes tempGene) {
    	boolean explored=false;
    	for(int nextExamIndex: this.conflictList[examIndex]) {
    		if(chromosome[nextExamIndex]==null) {
    			for(int myTimeslot=0;myTimeslot<this.timeslot;myTimeslot++) {
    				//if(!catchConflict(nextExamIndex,myTimeslot,tempGene)) {
    				if(!fastCheckConflict(nextExamIndex,myTimeslot)) {
    					fillFastConflictMatrix(nextExamIndex,myTimeslot);
    					chromosome[nextExamIndex]=myTimeslot;
    					subsetColor[nextExamIndex]=subsetCounter;
    					tempGene.add(myTimeslot, nextExamIndex);
    					explored=true;
    					fastRecursion(nextExamIndex,subsetCounter,chromosome,subsetColor,tempGene);
    					
    				}
    				if(explored) break;
    			}
    		}
    		explored=false;
    	}
    	return;
    }
    	
   public boolean fastCheckConflict(int examIndex,int timeslot) {
	   if(this.fastConflictMatrix[examIndex][timeslot]!=0) return true;
	   return false;
   }
   
   public void fillFastConflictMatrix(int examIndex,int timeslot) {
	   for(Integer conflictingExamIndex : this.conflictList[examIndex]) {
		   this.fastConflictMatrix[conflictingExamIndex][timeslot]=1;
	   }	   
	   return;
   }


}

