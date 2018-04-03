package com.johanlund.statistics_settings_portions;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.external_classes.TinyDB;
import com.johanlund.statistics_settings_portions.PortionStatRange;

import java.util.ArrayList;

/**
 * Created by Johan on 2018-01-23.
 */

/**
 * NB. Uses shared preferences!
 */
class PortionStatRangeAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private final String RANGES_KEY= "portionRanges";
    private ArrayList<PortionStatRange> ranges;
    private final TinyDB tinydb;



    public PortionStatRangeAdapter(Context context){
        //get intervals from Shared Preferences, via help library TinyDB
        tinydb = new TinyDB(context);
        ranges = tinydb.getListPortionRange(RANGES_KEY);
        if (ranges == null){
            ranges = new ArrayList<>();
        }

    }

    public void addRange(PortionStatRange portionStatRange) {
        ranges.add(portionStatRange);

        //put in shared preferences to be able to retrieve the same settings at upstart
        tinydb.putListPortionRange(RANGES_KEY, ranges);

    }

    public class RangeHolder extends RecyclerView.ViewHolder {
        TextView rangesFrom;
        TextView rangesTo;
        Switch switchedOn;

        public RangeHolder(View v) {
            super(v);
            rangesFrom = (TextView) v.findViewById(R.id.rangeFrom);
            rangesTo = (TextView) v.findViewById(R.id.rangeTo);
            switchedOn = (Switch)v.findViewById(R.id.switch1);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.item_range, parent,
                false);
        return new RangeHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RangeHolder rangeHolder = (RangeHolder) holder;
        PortionStatRange range = ranges.get(position);
        rangeHolder.rangesFrom.setText(Float.toString(range.getRangeStart()));
        rangeHolder.rangesTo.setText(Float.toString(range.getRangeStop()));
        rangeHolder.switchedOn.setChecked(range.isTurnedOn());
    }

    @Override
    public int getItemCount() {
        return ranges.size();
    }
}
