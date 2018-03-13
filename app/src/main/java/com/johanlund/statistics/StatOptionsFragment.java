package com.johanlund.statistics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.info.InfoActivity;
import com.johanlund.statistics_settings.AvgBmSettingsActivity;
import com.johanlund.statistics_settings.AvgRatingSettingsActivity;
import com.johanlund.statistics_settings.TimeBristolSettingsActivity;
import com.johanlund.statistics_settings.TimeCompleteSettingsActivity;
import com.johanlund.statistics_settings.TimeRatingSettingsActivity;
import com.johanlund.statistics_settings.PortionStatSettingsActivity;

import static com.johanlund.ibsfoodanalyzer.R.id.avgBristolItem;
import static com.johanlund.ibsfoodanalyzer.R.id.avgCompleteItem;
import static com.johanlund.ibsfoodanalyzer.R.id.avgInfoItem;
import static com.johanlund.ibsfoodanalyzer.R.id.avgRatingItem;
import static com.johanlund.ibsfoodanalyzer.R.id.freqInfoItem;
import static com.johanlund.ibsfoodanalyzer.R.id.portionsInfoItem;
import static com.johanlund.ibsfoodanalyzer.R.id.timeBristolItem;
import static com.johanlund.ibsfoodanalyzer.R.id.timeCompleteItem;
import static com.johanlund.ibsfoodanalyzer.R.id.timeInfoItem;
import static com.johanlund.ibsfoodanalyzer.R.id.timeRatingItem;

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

        view.findViewById(timeRatingItem).setOnClickListener(this);
        view.findViewById(timeCompleteItem).setOnClickListener(this);
        view.findViewById(timeBristolItem).setOnClickListener(this);

        /*since some ids are set dynamically through custom views, the builder seem to need some
        help to find them*/

        //info buttons
        view.findViewById(getResources().getIdentifier("avgInfoItem", "id", getContext
                ().getPackageName())).setOnClickListener(this);
        view.findViewById(getResources().getIdentifier("freqInfoItem", "id", getContext
                ().getPackageName())).setOnClickListener(this);
        view.findViewById(getResources().getIdentifier("timeInfoItem", "id", getContext
                ().getPackageName())).setOnClickListener(this);
        view.findViewById(getResources().getIdentifier("portionsInfoItem", "id", getContext
                ().getPackageName())).setOnClickListener(this);

        //settings buttons
        view.findViewById(getResources().getIdentifier("avgSettingsRatingItem", "id", getContext
                ().getPackageName())).setOnClickListener(this);
        view.findViewById(getResources().getIdentifier("avgSettingsCompleteItem", "id", getContext
                ().getPackageName())).setOnClickListener(this);
        view.findViewById(getResources().getIdentifier("avgSettingsBristolItem", "id", getContext
                ().getPackageName())).setOnClickListener(this);

        view.findViewById(getResources().getIdentifier("timeSettingsRatingItem", "id", getContext
                ().getPackageName())).setOnClickListener(this);
        view.findViewById(getResources().getIdentifier("timeSettingsCompleteItem", "id", getContext
                ().getPackageName())).setOnClickListener(this);
        view.findViewById(getResources().getIdentifier("timeSettingsBristolItem", "id", getContext
                ().getPackageName())).setOnClickListener(this);

        view.findViewById(getResources().getIdentifier("portionsSettingsRatingItem", "id",
                getContext().getPackageName())).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        Log.d("Debug", "just above switch");
        switch (v.getId()) {

            //to stat
            case avgRatingItem:
                newStatActivity(new AvgStatActivity());
                break;
            case R.id.avgCompleteItem:
                newStatActivity(new CompleteStatActivity());
                break;
            case avgBristolItem:
                newStatActivity(new BristolStatActivity());
                break;

            case timeRatingItem:
                newStatActivity(new TimeRatingStatActivity());
                break;
            case R.id.timeCompleteItem:
                newStatActivity(new TimeCompleteStatActivity());
                break;
            case timeBristolItem:
                newStatActivity(new TimeBristolStatActivity());
                break;

            //info buttons
            case avgInfoItem:
                InfoActivity.newInfoActivity((AppCompatActivity) getActivity(), getResources().getString(R.string.avg_info));
                break;
            case freqInfoItem:
                InfoActivity.newInfoActivity((AppCompatActivity) getActivity(), getResources().getString(R.string.freq_info));
                break;
            case timeInfoItem:
                InfoActivity.newInfoActivity((AppCompatActivity) getActivity(), getResources().getString(R.string.time_info));
                break;
            case portionsInfoItem:
                InfoActivity.newInfoActivity((AppCompatActivity) getActivity(), getResources().getString(R.string.portions_info));
                break;

            //settings buttons
            //avg
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

            //time
            case R.id.timeSettingsRatingItem: {
                Intent intent = new Intent(getActivity(), TimeRatingSettingsActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.timeSettingsCompleteItem: {
                Intent intent = new Intent(getActivity(), TimeCompleteSettingsActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.timeSettingsBristolItem: {
                Intent intent = new Intent(getActivity(), TimeBristolSettingsActivity.class);
                startActivity(intent);
                break;
            }

            //portions
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
