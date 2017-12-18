package com.company;

public class Exam {


	private int numConlict;
	private int subset;
	private int index;
	private boolean placed;
	
	
	public Exam(int index) {
		this.index=index;
		this.subset=0;
		this.numConlict=0;
		placed=false;
		
	}
	
	public boolean isPlaced() {
		return placed;
	}

	public void setPlaced(boolean placed) {
		this.placed = placed;
	}

	
	public int getIndex() {
		return index;
	}
	
	public int getNumConlict() {
		return numConlict;
	}

	public void setNumConlict(int numConlict) {
		this.numConlict = numConlict;
	}

	public int getSubset() {
		return subset;
	}

	public void setSubset(int subset) {
		this.subset = subset;
	}

}
