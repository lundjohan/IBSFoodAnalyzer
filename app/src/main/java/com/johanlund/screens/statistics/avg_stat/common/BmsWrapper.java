package com.johanlund.screens.statistics.avg_stat.common;

import com.johanlund.base_classes.Tag;
import com.johanlund.util.ScoreTime;
import com.johanlund.util.TagsWrapperBase;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

public class BmsWrapper implements TagsWrapperBase{
    private List<Tag> tags;
    //bms score either reflects complete or bristol (not both at same time though)
    List<ScoreTime>bms;


    private BmsWrapper(List<Tag> tags, List<ScoreTime> bms) {
        this.tags = tags;
        this.bms = bms;
    }

    @Override
    public List<Tag> getTags() {
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
     * Except for chunkend this is basically a copy of TagsWrapper makeTagsWrappers, only the latter is tested.
     * TODO use smaller methods together
     * @param allTags
     * @param scoreTimes
     * @param breaks
     * @return
     */
    public static List<TagsWrapperBase> makeBmsWrappers(List<Tag> allTags, List<ScoreTime> scoreTimes,
                                                         List<LocalDateTime> breaks) {
        List<TagsWrapperBase>toReturn = new ArrayList<>();
        int indTags = 0;
        int indRatings = 0;
        for (int i = 0; i < breaks.size(); i++){
            LocalDateTime b = breaks.get(i);
            List<Tag>tags = new ArrayList<>();
            List<ScoreTime>ratings = new ArrayList<>();
            while (indTags < allTags.size()){
                if (b.isBefore(allTags.get(indTags).getTime())){
                    break;
                }
                else{
                    tags.add(allTags.get(indTags));
                    indTags++;
                }
            }

            while (indRatings < scoreTimes.size()){
                if (b.isBefore(scoreTimes.get(indRatings).getTime())){
                    break;
                }
                else{
                    ratings.add(scoreTimes.get(indRatings));
                    indRatings++;
                }
            }
            //meaningless to make new RAtingTimes with only chunkEnd (this can happen when breaks are next to each other)
            if (ratings.size() > 0 || tags.size() > 0) {
                toReturn.add(new BmsWrapper(tags, ratings));
            }
        }
        return toReturn;
    }
}
