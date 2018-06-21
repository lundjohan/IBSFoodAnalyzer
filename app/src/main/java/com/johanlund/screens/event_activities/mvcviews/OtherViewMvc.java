package com.johanlund.screens.event_activities.mvcviews;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.johanlund.base_classes.Event;
import com.johanlund.base_classes.Other;
import com.johanlund.ibsfoodanalyzer.R;

import org.threeten.bp.LocalDateTime;

public class OtherViewMvc extends TagEventViewMvcAbstract {
    public OtherViewMvc(LayoutInflater inflater, ViewGroup container) {
        super(inflater, container);
    }

    @Override
    protected String getTextForAddTagsBtn() {
        return "Add Other Components";
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_other;
    }

    @Override
    protected int getInfoLayout() {
        return R.layout.info_other;
    }

    @Override
    protected String getBarTitle() {
        return "New Other";
    }

    @Override
    protected Event makeEventFromView(LocalDateTime ldt, String comment) {
        return new Other(ldt, comment, eventHasBreak, tagsList);
    }
}
