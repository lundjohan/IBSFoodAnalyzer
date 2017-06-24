package com.ibsanalyzer.tagpoint_classes;

import com.ibsanalyzer.model.TagTemplate;

import java.util.List;

/**
 * Keep this class simple! Used for statistics
 */
public abstract class TagPoint {
	private TagTemplate tagTemplate;
	private double quantity;

	public TagPoint(TagTemplate tagTemplate) {
		this.tagTemplate = tagTemplate;
	}

	public String getName() {
		return tagTemplate.get_tagname();
	}

	//should return List<String> in future
	public String  getIsA(){
		return tagTemplate.get_is_a1().get_tagname();
	}

	public double getQuantity() {
		return quantity;
	}

	//this method returns the score of the implemented TagPoint class.
	public abstract double getScore();

	//this method returns the score of the implemented TagPoint class.
	public abstract void calculate();
}
