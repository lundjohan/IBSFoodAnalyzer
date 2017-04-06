package com.ibsanalyzer.base_classes;

import org.threeten.bp.LocalDateTime;

//write for example spinach@green_leaves => 2 tags: spinach &
//green_leaves
public class Tag {
	private String name;
	private double size;
	protected LocalDateTime time;

	public Tag(String name) {
		this.name = name;
		this.size = 1;	//default 1

	}

	public Tag(LocalDateTime time, String name, double portions) {
		this.name = name;
		this.size = portions;
		this.time = time;
	}

	public String getName() {
		return name;
	}

	public void setName(String s) {
		name = s;

	}

	public LocalDateTime getTime() {
		return time;
	}

	public double getSize() {
		return size;
	}



}