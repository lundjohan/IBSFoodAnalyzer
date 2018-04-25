package com.johanlund.statistics_avg;

import com.johanlund.base_classes.Tag;
import com.johanlund.base_classes.TagBase;
import com.johanlund.util.RatingTimes;
import com.johanlund.util.ScoreTime;
import com.johanlund.util.TagsWrapperBase;

import org.threeten.bp.LocalDateTime;

import java.sql.Wrapper;
import java.util.ArrayList;
import java.util.List;

public class BmsWrapper implements TagsWrapperBase{
    private List<TagBase> tags;
    //bms score either reflects complete or bristol (not both at same time though)
    List<ScoreTime>bms;


    private BmsWrapper(List<TagBase> tags, List<ScoreTime> bms) {
        this.tags = tags;
        this.bms = bms;
    }

    @Override
    public List<TagBase> getTags() {
        return tags;
    }

    @Override
    public List<ScoreTime> getScoreTimes() {
        return bms;
    }

    //this will never be used for BMS
    @Override
    public LocalDateTime getChunkEnd() {
        return null;
    }

    /**
     * Except for chunkend this is basically a copy of TagsWrapper makeTagsWrappers.
     * TODO use smaller methods together
     * @param tags1
     * @param scoreTimes
     * @param breaks
     * @return
     */
    public static List<TagsWrapperBase> makeBmsWrappers(List<Tag> tags1, List<ScoreTime> scoreTimes,
                                                         List<LocalDateTime> breaks) {
        List<TagsWrapperBase>toReturn = new ArrayList<>();
        int indTags = 0;
        int indRatings = 0;
        for (LocalDateTime b: breaks){
            List<TagBase>tags = new ArrayList<>();
            List<ScoreTime>bms = new ArrayList<>();
            for (int i = indTags;i<tags1.size();i++){
                if (b.isBefore(tags1.get(i).getTime())){
                    break;
                }
                else{
                    tags.add(tags1.get(i));
                }
                indTags = i;
            }
            for (int j = indRatings;j<scoreTimes.size();j++){
                if (b.isBefore(scoreTimes.get(j).getTime())){
                    break;
                }
                else{
                    bms.add(scoreTimes.get(j));
                }
                indRatings = j;
            }
            //meaningless to make new RAtingTimes with only chunkEnd (this can happen when breaks are next to each other)
            if (bms.size() > 0 || tags.size() > 0) {
                toReturn.add(new BmsWrapper(tags, bms));
            }
        }
        return toReturn;
    }
}
