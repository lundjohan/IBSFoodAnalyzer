package com.johanlund.statistics_general;

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
import com.johanlund.statistics_avg.BristolAvgStatActivity;
import com.johanlund.statistics_avg.CompleteAvgStatActivity;
import com.johanlund.statistics_avg.DeltaAvgStatActivity;
import com.johanlund.statistics_avg.RatingAvgStatActivity;
import com.johanlund.statistics_portions.RatingPortionStatActivity;
import com.johanlund.statistics_settings.AvgBmSettingsActivity;
import com.johanlund.statistics_settings.AvgRatingSettingsActivity;
import com.johanlund.statistics_settings_portions.PortionStatSettingsActivity;
import com.johanlund.statistics_settings.TimeBristolSettingsActivity;
import com.johanlund.statistics_settings.TimeCompleteSettingsActivity;
import com.johanlund.statistics_settings.TimeRatingSettingsActivity;
import com.johanlund.statistics_time.BristolTimeStatActivity;
import com.johanlund.statistics_time.CompleteTimeStatActivity;
import com.johanlund.statistics_time.RatingTimeStatActivity;

import org.threeten.bp.LocalDate;

import static com.johanlund.constants.Constants.RESTART_DATE_REQUEST;
import static com.johanlund.ibsfoodanalyzer.R.id.avgBristolItemTextView;
import static com.johanlund.ibsfoodanalyzer.R.id.avgCompleteItemTextView;
import static com.johanlund.ibsfoodanalyzer.R.id.avgDeltaItemTextView;
import static com.johanlund.ibsfoodanalyzer.R.id.avgInfoItem;
import static com.johanlund.ibsfoodanalyzer.R.id.avgRatingItemTextView;
import static com.johanlund.ibsfoodanalyzer.R.id.freqInfoItem;
import static com.johanlund.ibsfoodanalyzer.R.id.portionsInfoItem;
import static com.johanlund.ibsfoodanalyzer.R.id.portionsRatingItem;
import static com.johanlund.ibsfoodanalyzer.R.id.timeBristolItemTextView;
import static com.johanlund.ibsfoodanalyzer.R.id.timeCompleteItemTextView;
import static com.johanlund.ibsfoodanalyzer.R.id.timeInfoItem;
import static com.johanlund.ibsfoodanalyzer.R.id.timeRatingItemTextView;

/**
 * Gives the user options for statistics
 */
public class StatOptionsFragment extends Fragment implements View.OnClickListener {

    //used for TimeStat
    public interface DiaryStarterActivity {
        void startDiaryWithDate(LocalDate ld);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stat_menu, container, false);

        //make them clickable => this should change to textView instead!
        view.findViewById(avgRatingItemTextView).setOnClickListener(this);
        view.findViewById(avgDeltaItemTextView).setOnClickListener(this);
        view.findViewById(avgCompleteItemTextView).setOnClickListener(this);
        view.findViewById(avgBristolItemTextView).setOnClickListener(this);

        view.findViewById(timeRatingItemTextView).setOnClickListener(this);
        view.findViewById(timeCompleteItemTextView).setOnClickListener(this);
        view.findViewById(timeBristolItemTextView).setOnClickListener(this);

        view.findViewById(portionsRatingItem).setOnClickListener(this);

        /*since some ids are set dynamically through custom views, the builder seems to need some
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
        view.findViewById(getResources().getIdentifier("avgSettingsDeltaItem", "id", getContext
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

        View portionSettingsView = view.findViewById(getResources().getIdentifier("portionsSettingsRatingItem", "id",
                getContext().getPackageName()));
        portionSettingsView.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        Log.d("Debug", "just above switch");
        switch (v.getId()) {

            //to stat
            case avgRatingItemTextView:
                newStatActivity(new RatingAvgStatActivity());
                break;
            case avgDeltaItemTextView:
                newStatActivity(new DeltaAvgStatActivity());
                break;
            case R.id.avgCompleteItemTextView:
                newStatActivity(new CompleteAvgStatActivity());
                break;
            case avgBristolItemTextView:
                newStatActivity(new BristolAvgStatActivity());
                break;

            case timeRatingItemTextView:
                newTimeStatActivity(new RatingTimeStatActivity());
                break;
            case R.id.timeCompleteItemTextView:
                newTimeStatActivity(new CompleteTimeStatActivity());
                break;
            case timeBristolItemTextView:
                newTimeStatActivity(new BristolTimeStatActivity());
                break;
            case portionsRatingItem:
                newStatActivity(new RatingPortionStatActivity());
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
            //uses same settings as Rating
            case R.id.avgSettingsDeltaItem: {
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
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    /**
     * Since it is possible in TimeStatActivities to press a date, and then start diary from that date,
     * we need to use startActivityForResult.
     * @param instance
     */
    private void newTimeStatActivity(Activity instance) {
        Intent intent = new Intent(getActivity(), instance.getClass());
        getActivity().startActivityForResult(intent, RESTART_DATE_REQUEST);
    }

}
