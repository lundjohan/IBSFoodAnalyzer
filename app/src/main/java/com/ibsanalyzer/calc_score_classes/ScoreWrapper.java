package com.ibsanalyzer.calc_score_classes;

import com.ibsanalyzer.base_classes.Chunk;
import com.ibsanalyzer.tagpoint_classes.TagPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by Johan on 2017-06-25.
 * This class is the Stratgy in Strategy Pattern
 */

public abstract class ScoreWrapper {
    int startHoursAfterEvent;
    int stopHoursAfterEvent;


    public ScoreWrapper(int startHoursAfterEvent, int stopHoursAfterEvent) {
        this.startHoursAfterEvent = startHoursAfterEvent;
        this.stopHoursAfterEvent = stopHoursAfterEvent;
    }

    public ScoreWrapper(int stopHoursAfterEvent) {
        startHoursAfterEvent = 0;
        this.stopHoursAfterEvent = stopHoursAfterEvent;
    }

    public abstract double getScore(TagPoint tp);

    public abstract Map<String, TagPoint> calcScore(List<Chunk> chunks, Map<String, TagPoint>
            tagPoint);

    /**
     * Notice that getScore is coming from inherited methods of this class.
     * @param tagPoints
     * @return
     */
    public List<TagPoint> toSortedList(Map<String, TagPoint> tagPoints) {
        List<TagPoint> toBeSorted = new ArrayList<>( tagPoints.values());
        Collections.sort(toBeSorted, new Comparator<TagPoint>()
                {
                    @Override
                    public int compare(TagPoint t1, TagPoint t2)
                    {
                        return (int)((getScore(t1)- getScore(t2))*100);
                    }
                }
        );
        return toBeSorted;
    }
}

