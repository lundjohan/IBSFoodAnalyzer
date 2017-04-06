package com.ibsanalyzer.base_classes;

import org.threeten.bp.LocalDateTime;;
import java.util.List;

public class Other extends Event {

	public Other(LocalDateTime time, List<Tag> tags) {
		super(time, tags);
	}
}