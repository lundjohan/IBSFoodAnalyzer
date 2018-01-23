package com.ibsanalyzer.statistics;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ibsanalyzer.diary.R;
import com.ibsanalyzer.external_classes.TinyDB;

import java.util.ArrayList;

/**
 * Created by Johan on 2018-01-23.
 */

class PortionStatRangeAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private final String RANGES_KEY= "portionRanges";
    private ArrayList<PortionStatRange> ranges;
    public PortionStatRangeAdapter(Context context){
        //get intervals from Shared Preferences, via help library TinyDB
        TinyDB tinydb = new TinyDB(context);
        ranges = tinydb.getListPortionRange(RANGES_KEY);
        if (ranges == null){
            ranges = new ArrayList<>();
        }

        tinydb.putListPortionRange(RANGES_KEY, ranges);
        tinydb.getListObject(ArrayList<PortionStatRange>d);

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_range, parent,
                false);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return ranges.size();
    }
}
