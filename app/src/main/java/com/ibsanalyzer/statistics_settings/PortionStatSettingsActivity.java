package com.ibsanalyzer.statistics_settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.ibsanalyzer.diary.R;
import com.ibsanalyzer.statistics.NewPortionRangeActivity;
import com.ibsanalyzer.statistics.PortionStatRange;
import com.ibsanalyzer.util.Util;

import static com.ibsanalyzer.constants.Constants.CHOSEN_FROM_RANGE;
import static com.ibsanalyzer.constants.Constants.CHOSEN_TO_RANGE;
import static com.ibsanalyzer.constants.Constants.NEW_PORTION_RANGES;

/**
 * This class, and this class alone, takes care of Shared Preferences (the adapter is agnostic about it)
 */
public class PortionStatSettingsActivity extends AppCompatActivity {
    PortionStatRangeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Portion Stat Settings");
        setContentView(R.layout.activity_portion_stat_settings);
        adapter = new PortionStatRangeAdapter(getBaseContext());


        RecyclerView portionsView = (RecyclerView) findViewById(R.id.portionsIntervals);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        portionsView.setLayoutManager(linearLayoutManager);
        portionsView.setAdapter(adapter);
        Util.addLineSeparator(portionsView, linearLayoutManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_tagtemplate_menu, menu);
        menu.findItem(R.id.menu_add_new).setOnMenuItemClickListener(new MenuItem
                .OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                newPortionRangeActivity();
                return true;
            }
        });
        return true;
    }

    private void newPortionRangeActivity() {
        Intent intent = new Intent(this, NewPortionRangeActivity.class);
        startActivityForResult(intent, NEW_PORTION_RANGES);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NEW_PORTION_RANGES) {
            if (data.hasExtra(CHOSEN_FROM_RANGE) && data.hasExtra(CHOSEN_TO_RANGE)) {
                //create a new element in RecyclerView
                float from = data.getFloatExtra(CHOSEN_FROM_RANGE, 0.0f);
                float to = data.getFloatExtra(CHOSEN_TO_RANGE, 1.0f);
                adapter.addRange(new PortionStatRange(from, to, true));
                adapter.notifyDataSetChanged();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);


    }
}