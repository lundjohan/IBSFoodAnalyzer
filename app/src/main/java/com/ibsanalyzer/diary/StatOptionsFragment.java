package com.ibsanalyzer.diary;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onClick(View v) {
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
        }
    }

    private void newStatActivity(StatActivity instance) {
        Intent intent = new Intent((Activity) this.callback, instance.getClass());
        startActivity(intent);
    }
}
