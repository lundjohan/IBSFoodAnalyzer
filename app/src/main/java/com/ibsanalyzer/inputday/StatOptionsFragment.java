package com.ibsanalyzer.inputday;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import static com.ibsanalyzer.inputday.EventsContainer.NEW_MEAL;


/**
 * A simple {@link Fragment} subclass.
 */
public class StatOptionsFragment extends Fragment implements View.OnClickListener {

    Button avgBtn;
    StatOptionsListener callback;
    public StatOptionsFragment() {
        // Required empty public constructor
    }
    public interface StatOptionsListener{
        void startNestedFragment(Fragment f);
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
        avgBtn = (Button) view.findViewById(R.id.avgBtn);
        avgBtn.setOnClickListener(this);


        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.avgBtn:
                newAverageStatActivity();
                break;
            case R.id.blueZoneBtn:
                //nestedFragment = new BlueZoneStatFragment();
                break;
            case R.id.completeBtn:
                //nestedFragment = new CompleteStatFragment();
                break;
            case R.id.bristolBtn:
                //nestedFragment = new BristolStatFragment();
                break;
        }
    }
//nested fragments creation
    private void newAverageStatActivity() {
        Intent intent = new Intent((Activity) this.callback, AverageStatActivity.class);
        startActivity(intent);
    }
}
