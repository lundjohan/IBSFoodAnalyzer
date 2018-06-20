package com.johanlund.base_classes;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Created by Johan on 2017-05-05.
 */

public abstract class InputEvent extends Event {
    protected List<TagWithoutTime> tags;

    public InputEvent(LocalDateTime time, List<TagWithoutTime> tags) {
        super(time);
        this.tags = tags;
    }
    public InputEvent(LocalDateTime time, String comment, List<TagWithoutTime> tags) {
        super(time, comment);
        this.tags = tags;
    }
    public InputEvent(LocalDateTime time, String comment, boolean hasBreak, List<TagWithoutTime> tags) {
        super(time, comment, hasBreak);
        this.tags = tags;
    }


    public List<TagWithoutTime> getTagsWithoutTime(){
        return tags;
    }

    //exists for legacy reasons, should probably be phased out
    public List<Tag> getTags() {
        List<Tag>tagsToReturn = new ArrayList<>();
        for (TagWithoutTime twt: tags){
            tagsToReturn.add(new Tag(time, twt));
        }
        return tagsToReturn;
    }

    /**
     * Some EventModel classes will override this one (read Bm), therefore it exist.
     *
     * @return
     */
    public Collection<? extends Tag> getInputTags() {
        return getTags();
    }

    @Override
    public String toString() {
        return time.toString();

    }
}
