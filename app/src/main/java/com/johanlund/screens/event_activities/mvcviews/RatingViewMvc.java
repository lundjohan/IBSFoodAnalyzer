package com.johanlund.screens.event_activities.mvcviews;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.johanlund.base_classes.Event;
import com.johanlund.base_classes.Rating;
import com.johanlund.ibsfoodanalyzer.R;

import org.threeten.bp.LocalDateTime;

import java.util.List;

public class RatingViewMvc extends EventViewMvcAbstract {
    TextView scoreName;
    SeekBar scoreBar;

    public RatingViewMvc(LayoutInflater inflater, ViewGroup container) {
        super(inflater, container);
    }

    @Override
    protected void initializeSpecViews() {
        scoreName = (TextView) rootView.findViewById(R.id.intensityName);
        scoreBar = (SeekBar) rootView.findViewById(R.id.intensityBar);
        scoreBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int score = ++progress;
                scoreName.setText(Rating.pointsToText(score));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_rating;
    }

    @Override
    protected int getInfoLayout() {
        return R.layout.info_rating;
    }

    @Override
    protected String getBarTitle() {
        return "New Rating";
    }

    @Override
    protected void bindEventSpecsToView(Event e) {
        int score = ((Rating)e).getAfter();
        scoreName.setText(Rating.pointsToText(score));
        scoreBar.setProgress(score-1);
    }

    @Override
    protected Event makeEventFromView(LocalDateTime ldt, String comment) {
        int after = scoreBar.getProgress() + 1; //scoreBar starts from zero
        return new Rating(ldt, comment, after);
    }

    @Override
    public void bindAddedTagToView(String tagName) {/*no tags to bind in this view*/}

    @Override
    public void removeTagFromView(String tagName) {
        /*
        not applicable
         */
    }

    @Override
    public List<String> getTagNames() {
        /*
        not applicable
         */
        return null;
    }
}
