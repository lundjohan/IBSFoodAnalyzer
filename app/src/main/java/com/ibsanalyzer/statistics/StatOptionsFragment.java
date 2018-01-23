package com.ibsanalyzer.statistics;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ibsanalyzer.diary.LoadEventsTemplateActivity;
import com.ibsanalyzer.diary.R;
import com.ibsanalyzer.settings.StatSettingsActivity;
import com.ibsanalyzer.statistics.AverageStatActivity;
import com.ibsanalyzer.statistics.BlueZoneStatActivity;
import com.ibsanalyzer.statistics.BristolStatActivity;
import com.ibsanalyzer.statistics.CompleteStatActivity;

import static com.ibsanalyzer.constants.Constants.EVENTSTEMPLATE_TO_LOAD;
import static com.ibsanalyzer.constants.Constants.LOAD_EVENTS_FROM_EVENTSTEMPLATE;
import static com.ibsanalyzer.diary.R.id.avgBtn;


/**
 * A simple {@link Fragment} subclass.
 */
public class StatOptionsFragment extends Fragment implements View.OnClickListener {
    StatOptionsListener callback;
    public StatOptionsFragment() {
        // Required empty public constructor
    }
    public interface StatOptionsListener{
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (StatOptionsListener) context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stat_options, container, false);

        //make buttons clickable
        ((Button) view.findViewById(R.id.avgBtn)).setOnClickListener(this);
        ((Button) view.findViewById(R.id.blueZoneBtn)).setOnClickListener(this);
        ((Button) view.findViewById(R.id.completeBtn)).setOnClickListener(this);
        ((Button) view.findViewById(R.id.bristolBtn)).setOnClickListener(this);
        ((Button) view.findViewById(R.id.portionBtn)).setOnClickListener(this);
        ((Button) view.findViewById(R.id.stat_settings)).setOnClickListener(this);

        // Inflate the layout for this fragment
        return view;
    }

    /*
    Notice that buttons need to be set clickable before, see above
     */
    @Override
    public void onClick(View v) {
        Log.d("Debug", "just above switch");
        switch (v.getId()) {

            case avgBtn:
                newStatActivity(new AverageStatActivity());
                break;
            case R.id.blueZoneBtn:
                newStatActivity(new BlueZoneStatActivity());
                break;
            case R.id.completeBtn:
                newStatActivity(new CompleteStatActivity());
                break;
            case R.id.bristolBtn:
                newStatActivity(new BristolStatActivity());
                break;
            case R.id.portionBtn:
                Intent intent = new Intent((Activity) this.callback, PortionStatSettingsActivity.class);
                startActivity(intent);
                Log.d("Debug", "inside case for starting PortionStatActivity");
                break;
            case R.id.stat_settings:
                newStatActivity(new StatSettingsActivity());
                break;
        }
    }

    private void newStatActivity(Activity instance) {
        Intent intent = new Intent((Activity) this.callback, instance.getClass());
        startActivity(intent);
    }
}
