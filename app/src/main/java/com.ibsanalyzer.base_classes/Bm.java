package com.ibsanalyzer.base_classes;

import org.threeten.bp.LocalDateTime;

public class Bm extends Event {
	//bristol should be between 1 and 7;
	private int bristol;
	//complete should be between 1 and 5
	private int complete = 0;

	public Bm(LocalDateTime time, int complete, int bristol) {
		super(time);
		this.complete = complete;
		this.bristol = bristol;

	}

	/*public double getSize() {
		return size;
	}*/
	public int getBristol() {
		return bristol;
	}


	public int getComplete() {
		return complete;
	}

	public static String completenessScoreToText(int score){
		String text = "OUT OF RANGE";
		switch(score){
			case 1:
				text = "Abysmal";
				break;
			case 2:
				text = "Bad";
				break;
			case 3:
				text = "Deficient";
				break;
			case 4:
				text = "Good";
				break;
			case 5:
				text = "Phenomenal";
				break;
		}
		return text;
	}

	//se pic here https://en.wikipedia.org/wiki/Bristol_stool_scale
    public static String bristolToText(int score) {
		String text = "OUT OF RANGE";
		switch(score){
			case 1:
				text = "Seperate hard lumps";
				break;
			case 2:
				text = "Lumpy Sausage";
				break;
			case 3:
				text = "Sausage with cracked surface";
				break;
			case 4:
				text = "Smooth sausage";
				break;
			case 5:
				text = "Soft blobs with clear edges";
				break;
			case 6:
				text = "Mushy with ragged edges";
				break;
			case 7:
				text = "Liquid";
				break;
		}
		return text;
	}
}
