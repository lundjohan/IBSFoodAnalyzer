package com.ibsanalyzer.statistics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ibsanalyzer.diary.R;
import com.ibsanalyzer.statistics_settings.AvgBmSettingsActivity;
import com.ibsanalyzer.statistics_settings.AvgRatingSettingsActivity;
import com.ibsanalyzer.statistics_settings.PortionStatSettingsActivity;

import static com.ibsanalyzer.diary.R.id.avgBristolItem;
import static com.ibsanalyzer.diary.R.id.avgCompleteItem;
import static com.ibsanalyzer.diary.R.id.avgRatingItem;

/**
 * Gives the user options for statistics
 */
public class StatOptionsFragment extends Fragment implements View.OnClickListener {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stat_menu, container, false);

        //make them clickable
        view.findViewById(avgRatingItem).setOnClickListener(this);
        view.findViewById(avgCompleteItem).setOnClickListener(this);
        view.findViewById(avgBristolItem).setOnClickListener(this);

        /*since some ids are set dynamically through custom views, the builder seem to need some
        help to find them*/
        view.findViewById(getResources().getIdentifier("avgSettingsRatingItem", "id", getContext
                ().getPackageName())).setOnClickListener(this);
        view.findViewById(getResources().getIdentifier("avgSettingsBristolItem", "id", getContext
                ().getPackageName())).setOnClickListener(this);
        view.findViewById(getResources().getIdentifier("avgSettingsCompleteItem", "id", getContext
                ().getPackageName())).setOnClickListener(this);
        view.findViewById(getResources().getIdentifier("portionsSettingsRatingItem", "id",
                getContext().getPackageName())).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        Log.d("Debug", "just above switch");
        switch (v.getId()) {

            case avgRatingItem:
                newStatActivity(new AverageStatActivity());
                break;
            case R.id.avgCompleteItem:
                newStatActivity(new CompleteStatActivity());
                break;
            case avgBristolItem:
                newStatActivity(new BristolStatActivity());
                break;
            case R.id.avgSettingsRatingItem: {
                Intent intent = new Intent(getActivity(), AvgRatingSettingsActivity.class);
                startActivity(intent);
                break;
            }
            //bristol and completeness share settings
            case R.id.avgSettingsBristolItem:
            case R.id.avgSettingsCompleteItem: {
                Intent intent = new Intent(getActivity(), AvgBmSettingsActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.portionsSettingsRatingItem: {
                Intent intent = new Intent(getActivity(), PortionStatSettingsActivity.class);
                startActivity(intent);
                Log.d("Debug", "inside case for starting PortionStatActivity");
                break;
            }
        }
    }

    private void newStatActivity(Activity instance) {
        Intent intent = new Intent(getActivity(), instance.getClass());
        startActivity(intent);
    }
}
