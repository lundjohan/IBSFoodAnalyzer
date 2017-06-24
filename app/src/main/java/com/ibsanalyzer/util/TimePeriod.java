package com.ibsanalyzer.util;


import com.ibsanalyzer.base_classes.Chunk;
import com.ibsanalyzer.base_classes.Tag;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

public class TimePeriod {
	LocalDateTime start;
	LocalDateTime end;

	public TimePeriod(LocalDateTime start, LocalDateTime end) {
		this.start = start;
		this.end = end;
	}

	private TimePeriod sliceFromRight(long hoursToRemove) {
		if (isOkToTrim(hoursToRemove)) {

		} else {
			throw new IllegalArgumentException("a TimePeriod cannot be sliced so end is before start");
		}
		end = end.minusHours(hoursToRemove);
		return this;
	}

	private boolean isOkToTrim(long hours) {
		LocalDateTime ldt = end.minusHours(hours);
		return ldt.isAfter(start) || ldt.isEqual(start);
	}

	/**
	 * 
	 * @param timePeriods
	 * @param hoursToRemove
	 * @return List of shortened timeperiods from the right. If end -
	 *         hoursToRemoveFromEnd <= start List becomes shorter
	 */
	public static List<TimePeriod> removeHoursAhead(List<TimePeriod> timePeriods, long hoursToRemove) {
		List<TimePeriod> toReturn = new ArrayList<>();
		for (TimePeriod tp : timePeriods) {
			if (tp.isOkToTrim(hoursToRemove)) {
				toReturn.add(tp.sliceFromRight(hoursToRemove));
			}
		}
		return toReturn;
	}

	public LocalDateTime getStart() {
		return start;
	}

	public LocalDateTime getEnd() {
		return end;
	}

	// =================================================================================================================
	/**
	 * Prerequisute: no overlapping chunks (speaking about time).
	 * 
	 * @param chunks
	 * @param tp
	 * @return
	 */
	public static List<Tag> retrieveTagsForPeriod(List<Chunk> chunks, TimePeriod tp) {
		Chunk chunk = getChunkForPeriod(chunks, tp);
		return chunk.getTagsForPeriod(tp);
	}

	private static Chunk getChunkForPeriod(List<Chunk> chunks, TimePeriod tp) {
		Chunk toReturn = null;
		for (Chunk ch : chunks) {
			if ((tp.getStart().isAfter(ch.getStartTime()) || tp.getStart().isEqual(ch.getStartTime()))
					&& (tp.getEnd().isBefore(ch.getLastTime()) || tp.getEnd().isEqual(ch.getLastTime()))) {
				toReturn = ch;
				break;
			}
		}
		return toReturn;
	}
	// =================================================================================================================
}
