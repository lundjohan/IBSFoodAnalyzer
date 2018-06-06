package com.johanlund.screens.statistics.portions_settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.util.Util;

import static com.johanlund.constants.Constants.CHOSEN_FROM_RANGE;
import static com.johanlund.constants.Constants.CHOSEN_TO_RANGE;
import static com.johanlund.constants.Constants.NEW_PORTION_RANGES;

/**
 * This class, and this class alone, takes care of relevant Shared Preferences (the adapter is agnostic about it)
 */
public class PortionStatSettingsActivity extends AppCompatActivity{
    PortionStatRangeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portion_stat_settings);
        setTitle("Portion Stat Settings");
        adapter = new PortionStatRangeAdapter(this);
        RecyclerView portionsView = (RecyclerView) findViewById(R.id.portionsIntervals);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        portionsView.setLayoutManager(linearLayoutManager);
        portionsView.setAdapter(adapter);
        Util.addLineSeparator(portionsView, linearLayoutManager);

        findViewById(R.id.linkToMorePortionSettings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMorePortionsSettings();
            }
        });
    }

    private void goToMorePortionsSettings(){
        Intent intent = new Intent(this, PortionStatSettingsMoreActivity.class);
        startActivity(intent);
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
