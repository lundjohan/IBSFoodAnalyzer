package com.ibsanalyzer.base_classes;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

public class BM extends Event {
	private double size;
	private int bristol;
	private int complete = 0;

	public BM(LocalDateTime time, List<Tag> tags, double size, int bristol) {
		super(time, tags);
		//preliminary solution, better to have complete handling in import.
		for (Tag tag:tags){
			if (tag.getName().equalsIgnoreCase("complete")){
				complete += 1;
			}
			else if (tag.getName().equalsIgnoreCase("incomplete")){
				complete -= 1;
			}

		}
		this.size = size;
		this.bristol = bristol;
	}

	public double getSize() {
		return size;
	}
	public double getBristol() {
		return bristol;
	}

	/**
	 * BM tags are, in general, not input-tags.
	 */
	@Override
	public List<Tag>getInputTags(){
		List<Tag>toReturn = new ArrayList<>();
		List<Tag>allTags = getTags();
		for (Tag t:allTags){
			if (t.getName().equalsIgnoreCase("squat") || t.getName().equalsIgnoreCase("squatting")) {
				toReturn.add(t);
			}
		}
		return toReturn;
	}

	public int isComplete() {
		return complete;
	}
}
