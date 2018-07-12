package com.johanlund.screens.event_activities.mvcviews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.johanlund.base_classes.Event;
import com.johanlund.base_classes.Meal;
import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.util.Util;

import org.threeten.bp.LocalDateTime;

public class MealViewMvc extends TagEventViewMvcAbstract {
    private TextView portionView;
    LayoutInflater inflater;
    public MealViewMvc(LayoutInflater inflater, ViewGroup container) {
        super(inflater, container);
        this.inflater = inflater;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_meal;
    }

    @Override
    protected int getInfoLayout() {
        return R.layout.info_meal;
    }

    @Override
    protected String getBarTitle() {
        return "New Meal";
    }

    @Override
    protected void initializeSpecViews() {
        super.initializeSpecViews();
        portionView = rootView.findViewById(R.id.portionSize);
        portionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.useNumberPickerDialog( portionView,inflater, context);
            }
        });
    }

    @Override
    protected void bindEventSpecsToView(Event e) {
        super.bindEventSpecsToView(e);
        portionView.setText(Double.toString(((Meal) e).getPortions()));
    }

    @Override
    protected String getTextForAddTagsBtn() {
        return "Add Meal Components";
    }

    @Override
    protected Event makeEventFromView(LocalDateTime ldt, String comment) {
        return new Meal(ldt, comment, eventHasBreak, tagsList, Double.parseDouble(portionView
                .getText().toString()));
    }
}
