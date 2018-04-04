package com.johanlund.statistics_settings_portions;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.johanlund.database.DBHandler;
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
public class PortionStatRangeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<PortionStatRange> ranges;
    private final TinyDB tinydb;
    Context context;


    public PortionStatRangeAdapter(Context context) {
        this.context = context;
        //get intervals from Shared Preferences, via help library TinyDB (TinyDB
        // takes care of conversion to and from String)
        tinydb = new TinyDB(context);
        ranges = tinydb.getListPortionRange(context.getResources().getString(R.string.portions_ranges_key));
        if (ranges == null) {
            ranges = new ArrayList<>();
        }
    }

    public class RangeHolder extends RecyclerView.ViewHolder {
        TextView rangesFrom;
        TextView rangesTo;
        Switch switchedOn;
        ConstraintLayout container;

        public RangeHolder(View v) {
            super(v);
            container = (ConstraintLayout) v.findViewById(R.id.itemRangeContainer);
            rangesFrom = (TextView) v.findViewById(R.id.rangeFrom);
            rangesTo = (TextView) v.findViewById(R.id.rangeTo);
            switchedOn = (Switch) v.findViewById(R.id.switch1);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_range, parent,
                false);
        return new RangeHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        RangeHolder rangeHolder = (RangeHolder) holder;
        PortionStatRange range = ranges.get(position);
        rangeHolder.rangesFrom.setText(Float.toString(range.getRangeStart()));
        rangeHolder.rangesTo.setText(Float.toString(range.getRangeStop()));
        rangeHolder.switchedOn.setChecked(range.isTurnedOn());
        rangeHolder.container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                doDeleteMenuPopUp(v, position);
                return true;
            }
        });
    }

    private void doDeleteMenuPopUp(View v, final int pos) {
        PopupMenu popup = new PopupMenu(context, v);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.delete_menu, popup
                .getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.del_range_item) {
                    deleteRange(pos);

                }
                return true;
            }
        });
        popup.show();
    }

    @Override
    public int getItemCount() {
        return ranges.size();
    }

    public void addRange(PortionStatRange portionStatRange) {
        ranges.add(portionStatRange);

        //put in shared preferences to be able to retrieve the same settings at upstart
        tinydb.putListPortionRange(context.getResources().getString(R.string.portions_ranges_key), ranges);

    }

    private void deleteRange(int pos) {
        ranges.remove(pos);
        this.notifyItemRemoved(pos);
        this.notifyItemChanged(pos);
        //put in shared preferences to be able to retrieve the same settings at upstart
        tinydb.putListPortionRange(context.getResources().getString(R.string.portions_ranges_key), ranges);

    }
}
