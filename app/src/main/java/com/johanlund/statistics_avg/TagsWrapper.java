package com.johanlund.statistics_avg;

import com.johanlund.base_classes.Break;
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
        for (int i = 0; i < breaks.size(); i++){
            LocalDateTime b = breaks.get(i);
            //last break
            //if (i == breaks.size()-1){
                //create
            //}

            //loop over dublettes
            if (i<breaks.size()-1 && b.isEqual(breaks.get(i+1))){
                continue;
            }


            List<Tag>tags = new ArrayList<>();
            List<ScoreTime>ratings = new ArrayList<>();
            while (indTags < allTags.size()){
                if (b.isBefore(allTags.get(indTags).getTime())){
                    break;
                }
                else{
                    tags.add(allTags.get(indTags));
                    indTags++;
                    chunkEnd = b;
                }
            }

            while (indRatings < scoreTimes.size()){
                if (b.isBefore(scoreTimes.get(indRatings).getTime())){
                    break;
                }
                else{
                    ratings.add(scoreTimes.get(indRatings));
                    indRatings++;
                    if (chunkEnd == null || b.isAfter(chunkEnd)){
                        chunkEnd = b;
                    }
                }
            }
            for (int k = indRatings;k<scoreTimes.size();k++){
                if (b.isBefore(scoreTimes.get(k).getTime())){
                    break;
                }
                else{
                    ratings.add(scoreTimes.get(k));
                    if (chunkEnd == null || b.isAfter(chunkEnd)){
                        chunkEnd = b;
                        indRatings = k;
                    }
                }

            }
            //meaningless to make new RAtingTimes with only chunkEnd (this can happen when breaks are next to each other)
            if (ratings.size() > 0 || tags.size() > 0) {
                toReturn.add(new TagsWrapper(tags, new RatingTimes(ratings, chunkEnd)));
            }
        }
        return toReturn;
    }
}
