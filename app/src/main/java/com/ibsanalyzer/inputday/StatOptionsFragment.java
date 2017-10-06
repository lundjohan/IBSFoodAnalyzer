package com.ibsanalyzer.inputday;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class StatOptionsFragment extends Fragment implements View.OnClickListener {

    Button avgBtn;
    public StatOptionsFragment() {
        // Required empty public constructor
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
        Fragment nestedFragment = null;
        switch (v.getId()) {
            case R.id.avgBtn:
                nestedFragment = new AverageStatFragment();
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
        startNestedFragment(nestedFragment);
    }
//nested fragments creation
    private void startNestedFragment(Fragment toBeNested) {
        if (toBeNested != null) {
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.addToBackStack(null);
            transaction.replace(R.id.space, toBeNested).commit();
        }
    }
}
