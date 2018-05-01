package com.johanlund.statistics_avg;

import com.johanlund.base_classes.Tag;
import com.johanlund.util.RatingTimes;
import com.johanlund.util.ScoreTime;
import com.johanlund.util.TagsWrapperBase;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Replacement for Chunk
 *
 * TODO remove RatingTimes and use its components instead
 */
public class TagsWrapper implements TagsWrapperBase {
    private List<Tag> tags;
    private RatingTimes ratings;

    public TagsWrapper(List<Tag> tags, RatingTimes ratings) {
        this.tags = tags;
        this.ratings = ratings;
    }

    public TagsWrapper(List<Tag> pts, List<ScoreTime> scoreTimes, LocalDateTime chunkEnd) {
        tags = pts;
        ratings = new RatingTimes (scoreTimes, chunkEnd);
    }

    public List<Tag> getTags() {
        return tags;
    }

    @Override
    public List<ScoreTime> getScoreTimes() {
        return ratings.getScoreTimes();
    }

    @Override
    public LocalDateTime getChunkEnd() {
        return ratings.getChunkEnd();
    }

    public RatingTimes getRatings() {
        return ratings;
    }

    /**
     *
     * @param allTags
     * @param scoreTimes
     * @param breaks should at least be of size one => the last one is the chunkEnd.
     * @return
     */
    //Breaks shall split allTags and scoretimes into TagsWrapperBases.
    //TODO this method could use same methods as Break.getRatingTimes, we only have to write smaller methods
    public static List<TagsWrapperBase> makeTagsWrappers(List<Tag> allTags, List<ScoreTime> scoreTimes,
                                        List<LocalDateTime> breaks) {
        List<TagsWrapperBase>toReturn = new ArrayList<>();
        int indTags = 0;
        int indRatings = 0;
        LocalDateTime chunkEnd = null;
        for (LocalDateTime b: breaks){
            List<Tag>tags = new ArrayList<>();
            List<ScoreTime>ratings = new ArrayList<>();
            for (int i = indTags;i<allTags.size();i++){
                if (b.isBefore(allTags.get(i).getTime())){
                    break;
                }
                else{
                    tags.add(allTags.get(i));
                    chunkEnd = b;
                }
                indTags = i;
            }
            for (int j = indRatings;j<scoreTimes.size();j++){
                if (b.isBefore(scoreTimes.get(j).getTime())){
                    break;
                }
                else{
                    ratings.add(scoreTimes.get(j));
                }
                indRatings = j;
            }
            //meaningless to make new RAtingTimes with only chunkEnd (this can happen when breaks are next to each other)
            if (ratings.size() > 0 || tags.size() > 0) {
                toReturn.add(new TagsWrapper(tags, new RatingTimes(ratings, chunkEnd)));
            }
        }
        return toReturn;
    }
}
