package com.company;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class sortedPopulationGenerator {
	private int counter=0; //testing
    private Integer[] exams;
    private Integer timeslot;
    //private Integer[] chromosome;
    private Integer numberOfSubset;
    private Integer[] subsetsDimensions;
    private Integer studentNum;
    private ArrayList<Integer>[] conflictList;


    private Integer[][] conflictMatrix;
    private Integer[][] fastConflictMatrix;
    private Genes tempGeneList;
    //private ArrayList<Integer>[] geneList = (ArrayList<Integer>[])new ArrayList[timeslot];
    private ArrayList<Genes> geneListCollection = new ArrayList<Genes>();
    private ArrayList<Genes> sortedGeneCollection = new ArrayList<Genes>();
    private ArrayList<Chromosome> population = new ArrayList<Chromosome>();
    private Exam[] examArray;

    


    public void generatePop(){
    	//System.out.println("GENERATING POPULATION");

    	conflictList();
    	//printConflictList();
    	buildFastConflictMatrix();
    	buildExamArray();
    	identifySubset();
    	//printSubsets();
    	setExamArrayConflicts();
    	//printSubsets();
    	sortExamArray();
    	//printSubsets();
    	sortedGenerator(); //ALSO GENERATE THE POPULATION. POPULATION SIZE HARDCODE, SET TO 100
    	//printFastConflictMatrix();
    	//fastGenerator();
        //chromosomeGenerator();
        //subsetGenerator();
        //createPopulation(-1);
        //printPopulation();
        //printConflictMatrix();
          
      
    }

    
    
    public sortedPopulationGenerator(Integer[] exams, Integer timeslot, Integer[][] conflictMatrix, Integer studentNum) {
        this.timeslot = timeslot;
        this.exams = exams;
        this.studentNum = studentNum;
        tempGeneList = new Genes(timeslot);
        this.conflictList = (ArrayList<Integer>[])new ArrayList[exams.length];
        //this.chromosome = new Integer[exams.length];
        this.conflictMatrix = conflictMatrix;
        this.fastConflictMatrix = new Integer[exams.length][timeslot];
        this.examArray= new Exam[exams.length];
        //for(int i=0;i<timeslot;i++) this.geneList[i] = new ArrayList<Integer>();
        
        
    }
    
    public void buildExamArray() {
    	for(int i=0;i<exams.length;i++) {
    		examArray[i]=new Exam(i);
    	}
    }
    
    public void identifySubset() {
    	int subsetCounter=0;
    	//Integer[] subsetColor = new Integer[exams.length];
    	Integer[] placed = new Integer[exams.length];
    	for(int i=0;i<exams.length;i++) {
    		//subsetColor[i]=0;
    		placed[i]=0;
    	}
    	for(int examIndex=0;examIndex<exams.length;examIndex++) {
    		if(placed[examIndex]==0) {
    			subsetCounter++;
    			placed[examIndex]=1;
    			examArray[examIndex].setSubset(subsetCounter);
    			setIdentificationRecursion(examIndex,subsetCounter,placed,this.examArray);
    			   			
    		}
    	}
    }
    
    public void setIdentificationRecursion(int examIndex,int subsetCounter, Integer[] placed,Exam[] examArray) {
    	for(int nextExamIndex: this.conflictList[examIndex]) {
    		if(placed[nextExamIndex]==0) {
     					examArray[nextExamIndex].setSubset(subsetCounter);
    					placed[nextExamIndex]=1;
    					setIdentificationRecursion(nextExamIndex,subsetCounter,placed,examArray);
    			}
    		}
    	
    	return;
    }
    
    public void printSubsets() {
    	System.out.println("PRINT SUBSETS:");
    	
    	for(Exam e:examArray) {
    		System.out.println("exam: " + e.getIndex() + " conflicts:  " + e.getNumConlict() + " subset: " + e.getSubset());
    	}
    }
    //SET TEHE NUMBER OF CONFLICT 
    public void setExamArrayConflicts() {
    	for(Exam e: examArray) {
    		e.setNumConlict(conflictList[e.getIndex()].size());
    	}
    }
    
    //SORT EXAMS BY NUMBER OF CONFLICT
    public void sortExamArray() {
    	Exam temp;
    	for(int j=0;j<examArray.length;j++) {
    		for(int i=(examArray.length-1);i>j;i--) {
    			if(examArray[i].getNumConlict()>=examArray[i-1].getNumConlict()) {
    				temp=examArray[i-1];
    				examArray[i-1]=examArray[i];
    				examArray[i]=temp;
    			}
    		}	    		
    	}
    	return;
    }
    //place exams in order from the most to least conflicting
    //cannot place 48
    public void sortedGenerator() {
    	boolean explored=false;
    	Integer[] chromosome = new Integer[exams.length];
    	for(int i=0;i<exams.length;i++) chromosome[i] = -1;
    	for(Exam e: examArray) {
    		if(!e.isPlaced()) {
    			//for(int myTimeslot=0; myTimeslot<timeslot;myTimeslot++) {
    			for(int myTimeslot=0; myTimeslot<timeslot; myTimeslot++) {
    				if(!fastCheckConflict(e.getIndex(),myTimeslot)) {
    					explored=true;
    					e.setPlaced(true);
    					chromosome[e.getIndex()]=myTimeslot;
    					fillFastConflictMatrix(e.getIndex(),myTimeslot);
    				}    				
    				if(explored) break;
    				//leave the section below commented out , im working on it 16/12/17
    				
    				if(myTimeslot==(timeslot-1) && !explored) {
    					if(!outOfMyWay(e.getIndex(),chromosome,e)) {
    						System.out.println("FAIL on exam: " + (e.getIndex()+1));
    						//printFastConflictMatrix();
    						return;   					
    					}
    				
    				} 			
    			}
    		}
    		explored=false;
    		if(!myChecker(chromosome)) return;
    	}
    	//prints the cromosome in the format (exam - timeslot) so i can test it
    	//System.out.println("first chromosome:"); 
    	for(int i=0;i<chromosome.length;i++) {
    		//System.out.println((i+1)+" " + (chromosome[i]+1) + " subset: " + subsetColor[i]);
    		System.out.println( (i+1) +" " + ((chromosome[i])+1));
    	}
    	
    	//if an exam has timeslot equal to -1 that means it has not been placed
    	for(int i=0;i<chromosome.length;i++) {
    		if (chromosome[i]==-1) {
    			System.out.println("unable to place: " + (i+1));	
    		}
    	}
    	
    	generateSortedPopulation(chromosome);
    	   	
    }
    //GENERATE POPULATION BY SHUFFLING THE GROUPS BETWEEN TIMESLOT, NOT VERY GOOD BUT IT WORKS
    public void generateSortedPopulation(Integer[] chromosome) {
    	System.out.println("-----------------------------------------------");
    	int populationSize=100;
    	
    	Genes myGene = new Genes(this.timeslot);
    	Integer[] finalChromosome = new Integer[exams.length];
		for (int i=0;i<exams.length;i++) finalChromosome[i]=chromosome[i];
		myGene.chromosomeToGene(finalChromosome);
		population.add(new Chromosome(this.timeslot,exams.length,this.studentNum,finalChromosome,myGene.geneList()));  	
		
    	for(int p=1;p<populationSize;p++) {
    		shuffleArray(myGene.geneList());
    		finalChromosome=myGene.chromosomeTranslation(finalChromosome);
    		//print for testing shuffled solution
    		for(int i=0;i<finalChromosome.length;i++) {
        		//System.out.println((i+1)+" " + (chromosome[i]+1) + " subset: " + subsetColor[i]);
        		System.out.println( (i+1) +" " + ((finalChromosome[i])+1));
        	}
    		population.add(new Chromosome(this.timeslot,exams.length,this.studentNum,finalChromosome,myGene.geneList()));   		
    	} 	
    }
    
    
    public boolean myChecker(Integer[] chromosome) {
    	int myTimeslot=0;
    	for(int i=0; i<chromosome.length;i++) {
    		if(chromosome[i]!=-1) {
	    		myTimeslot=chromosome[i];
	    		for(int j=0; j<chromosome.length;j++) {
	    			if(i!=j && chromosome[j]==myTimeslot) {
	    				if(conflictMatrix[i][j]!=0){
	    					System.out.println("CONFLICT FOUND");
	    					return false;
	    				}
	    			}   			
	    		}
    		}
    	}
    	return true;
    }
    public boolean secondChecker(Integer[] chromosome) {
    	for(int myTimeslot=0; myTimeslot<timeslot; myTimeslot++) {
    		for(int index=0; index<chromosome.length; index++) {
    			if(chromosome[index]==myTimeslot) {
	    			for(int index2=0; index2<chromosome.length;index2++) {
	    				if(index!=index2 && chromosome[index]==chromosome[index2] && doesConflict(index,index2)) {
	    					return false;
	    				}
	    			}
    			}
    		}
    	}
    	return true;
    }
    //repetition in the argument , must be fixed
    //tutti i print e i vari checker sono solo per trouble shooting
    public boolean outOfMyWay(int examIndex,Integer[] chromosome,Exam e) {
    	/*
    	int counter=0;
    	
    	for(int j=0;j<timeslot;j++) {
    		counter+=fastConflictMatrix[examIndex][j];
    		System.out.print("<"+fastConflictMatrix[examIndex][j]+">");
    	}
    	System.out.println("  number: " + counter);
    	*/
    	int numConflict; 
    	for(numConflict=1;numConflict<exams.length;numConflict++) { //TRY TO MAKE SPACE IN THE TIMESLOTS WITH LESS CONFLICT
    	//checkSum();
	    	for(int myTimeslot=0;myTimeslot<this.timeslot;myTimeslot++) {
	    		if(fastConflictMatrix[examIndex][myTimeslot]==numConflict) {
			    	for(int i=0;i<chromosome.length;i++) {
			    		if(chromosome[i]==myTimeslot && doesConflict(examIndex,i)) { // i is the index of the exam to move out of the way
			    			moveExam(i,myTimeslot,chromosome);
			    		}
			    	}
			    	//if (!secondChecker(chromosome)) {System.out.println("before placing");return false;}
			    	if(!fastCheckConflict(examIndex,myTimeslot)) {
			    		/*
			    		checkSum();
			    		System.out.println("myTimeslot=" + myTimeslot);
			    		for(int j=0;j<timeslot;j++) {
			        		counter+=fastConflictMatrix[examIndex][j];
			        		System.out.print("<"+fastConflictMatrix[examIndex][j]+">");
			        	}
			        	System.out.println("  number: " + counter);
			        	counter=0;
			        	*/
			    		fillFastConflictMatrix(examIndex,myTimeslot);
			    		e.setPlaced(true);
			    		chromosome[examIndex]=myTimeslot;	
			    		
			    		//if(!myChecker(chromosome)) {System.out.println("after placing");return false;}
			    		
			    		return true;
			    	}
		    	}
	    	}
    	}
    	return false;
    }
    //checkSum , troubleshooting related
    public void checkSum() {
    	int sum=0;
    	for(int i=0;i<exams.length;i++) {
    		for(int j=0;j<timeslot;j++) {
    			if(fastConflictMatrix[i][j]<0) System.out.println("LALALALALALAL");sum+=fastConflictMatrix[i][j];    			
    		}
    	}
    	System.out.println("sum: " + sum);
    	return;
    }
    
    public boolean doesConflict(int examIndex, int potentialConflict) {
    	for(int exam: conflictList[examIndex]) {
    		if(exam==potentialConflict) return true;
    	}
    	return false;
    	
    }
    public void moveExam(int examIndex,int oldTimeslot,Integer[] chromosome) {
    	boolean moved=false;
    	for(int myTimeslot=0;myTimeslot<this.timeslot && moved==false ;myTimeslot++) {
    		if(myTimeslot!=oldTimeslot && !fastCheckConflict(examIndex,myTimeslot)) {
    			//fastConflictMatrix[examIndex][oldTimeslot]--;
    			removeFromFastConflictMatrix(examIndex,oldTimeslot);
    			//fastConflictMatrix[examIndex][myTimeslot]++;
    			fillFastConflictMatrix(examIndex,myTimeslot);
    			chromosome[examIndex]=myTimeslot;
    			moved=true;
    		}
    	}    	    	
    }
    
    public boolean fastCheckConflict(int examIndex,int timeslot) {
 	   if(this.fastConflictMatrix[examIndex][timeslot]!=0) return true;
 	   return false;
    }
    
    public void fillFastConflictMatrix(int examIndex,int timeslot) {
 	   for(Integer conflictingExamIndex : this.conflictList[examIndex]) {
 		   this.fastConflictMatrix[conflictingExamIndex][timeslot]++;
 	   }	   
 	   return;
    }
   
    public void removeFromFastConflictMatrix(int examIndex,int timeslot) {
    	for(Integer conflictingExamIndex : this.conflictList[examIndex]) {
  		   this.fastConflictMatrix[conflictingExamIndex][timeslot]--;
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
    	return;
    }
    
    public void buildFastConflictMatrix() {
    	
    	for(int i=0;i<this.exams.length;i++) {
    		for(int j=0;j<this.timeslot;j++) {
    			this.fastConflictMatrix[i][j]=0;
    		}
    	}
    	return;
    }
    
    public void printConflictList() {
    	for(int i=0;i<conflictList.length;i++) {
    		System.out.print("<" + i + ">");
    		for(Integer exam: conflictList[i]) {
    			System.out.print("->[" + (exam+1) + "]"); //PRINT THE EXAM NUMBER NOT THE EXAM INDEX
    		}
    		System.out.println();
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

    public boolean catchConflict(int examIndex, int timeslot, Genes tempGeneList) {
        for (int potentialConflict : tempGeneList.timeslotList(timeslot)) {
            if (this.conflictMatrix[examIndex][potentialConflict] != 0) {
                return true;
            }
        }
        return false;

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
    /*
    public Integer[] getChromosome() {
        return chromosome;
    }

    public void setChromosome(Integer[] chromosome) {
        this.chromosome = chromosome;
    }
	*/
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
    
    public void shuffleArray(ArrayList[] ar)
	{
	    // If running on Java 6 or older, use `new Random()` on RHS here
	    Random rnd = ThreadLocalRandom.current();
	    for (int i = ar.length - 1; i > 0; i--)
	    {
	        int index = rnd.nextInt(i + 1);
	        // Simple swap
	        ArrayList<Integer> a = ar[index];
	        ar[index] = ar[i];
	        ar[i] = a;
	    }
	}
    	


   

}

