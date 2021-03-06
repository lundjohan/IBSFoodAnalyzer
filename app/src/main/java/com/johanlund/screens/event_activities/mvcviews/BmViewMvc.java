package com.johanlund.screens.event_activities.mvcviews;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.johanlund.base_classes.Bm;
import com.johanlund.base_classes.Event;
import com.johanlund.ibsfoodanalyzer.R;

import org.threeten.bp.LocalDateTime;

import java.util.List;

public class BmViewMvc extends EventViewMvcAbstract {
    TextView bristolName;
    SeekBar bristolBar;
    TextView completeName;
    SeekBar completeBar;

    public BmViewMvc(LayoutInflater inflater, ViewGroup container) {
        super(inflater, container);
    }

    private void setBristolNrAndText(int bristolScore) {
        bristolName.setText("(" + bristolScore + ") " + Bm.bristolToText(bristolScore));
    }

    @Override
    protected void initializeSpecViews() {
        bristolName = (TextView) rootView.findViewById(R.id.bristolName);
        bristolBar = (SeekBar) rootView.findViewById(R.id.bristolBar);
        completeName = (TextView) rootView.findViewById(R.id.completeName);
        completeBar = (SeekBar) rootView.findViewById(R.id.completeBar);

        bristolBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int score = ++progress;
                setBristolNrAndText(score);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        completeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int score = ++progress;
                completeName.setText(Bm.completenessScoreToText(score));
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
        return R.layout.activity_bm;
    }

    @Override
    protected int getInfoLayout() {
        return R.layout.info_bm;
    }

    @Override
    protected String getBarTitle() {
        return "New Bowel Movement";
    }

    @Override
    protected void bindEventSpecsToView(Event e) {
        Bm bm = (Bm)e;
        bristolBar.setProgress(bm.getBristol()-1);
        setBristolNrAndText(bm.getBristol());
        completeBar.setProgress(bm.getComplete()-1);
        completeName.setText(Bm.completenessScoreToText(bm.getComplete()));
    }

    @Override
    protected Event makeEventFromView(LocalDateTime ldt, String comment) {
        int complete = completeBar.getProgress() + 1; //scoreBar starts from zero
        int bristol = bristolBar.getProgress() + 1;
        return new Bm(ldt, comment, complete, bristol);
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
