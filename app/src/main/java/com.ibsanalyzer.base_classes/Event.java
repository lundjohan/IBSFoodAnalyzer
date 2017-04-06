package com.ibsanalyzer.base_classes;

import org.threeten.bp.LocalDateTime;;
import java.util.Collection;
import java.util.List;

//baseclass only exists to avoid duplication of code.
public abstract class Event {
	protected LocalDateTime time;
	protected List<Tag> tags;

	public LocalDateTime getTime() {
		return time;
	}

	public List<Tag> getTags() {
		return tags;
	}
	/**
	 * Compares string with name of tag.
	 * @return Tag with same name as string, or null if it doesn't exist.
	 */
	public Tag getTag(String string) {
		Tag t = null;
		for (Tag tag: tags){
			if (tag.getName().equals(string)){
				t = tag;
				break;
			}
		}
		return t;
	}


	public Event(LocalDateTime time, List<Tag> tags) {
		this.time = time;
		this.tags = tags;
	}

	/**Some Event classes will override this one (read BM), therefore it exist.
	 *
	 * @return
	 */
	public Collection<? extends Tag> getInputTags() {
		return getTags();
	}
	@Override
	public String toString(){
		return time.toString();

	}



}