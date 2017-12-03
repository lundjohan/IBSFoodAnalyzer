package com.ibsanalyzer.base_classes;

import org.threeten.bp.LocalDateTime;

import java.util.Collection;
import java.util.List;


/**
 * Created by Johan on 2017-05-05.
 */

public abstract class InputEvent extends Event {
    protected List<Tag> tags;

    public InputEvent(LocalDateTime time, List<Tag> tags) {
        super(time);
        this.tags = tags;
    }
    public InputEvent(LocalDateTime time, String comment, List<Tag> tags) {
        super(time, comment);
        this.tags = tags;
    }




    public List<Tag> getTags() {
        return tags;
    }

    /**
     * Compares string with name of tag.
     *
     * @return TagModel with same name as string, or null if it doesn't exist.
     */
    public Tag getTag(String string) {
        Tag t = null;
        for (Tag tag : tags) {
            if (tag.getName().equals(string)) {
                t = tag;
                break;
            }
        }
        return t;
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
