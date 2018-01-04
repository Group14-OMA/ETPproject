package com.company;

public class Exam {


	private int numConlict;
	private int subset;
	private int index;
	private int timeslot=-1;
	private int position=-1;
	

	

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
	
	public int getTimeslot() {
		return timeslot;
	}

	public void setTimeslot(int timeslot) {
		this.timeslot = timeslot;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
}
