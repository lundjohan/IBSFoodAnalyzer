package com.ibsanalyzer.statistics;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.ibsanalyzer.diary.LoadEventsTemplateActivity;
import com.ibsanalyzer.diary.R;

import static com.ibsanalyzer.constants.Constants.EVENTSTEMPLATE_TO_LOAD;
import static com.ibsanalyzer.constants.Constants.LOAD_EVENTS_FROM_EVENTSTEMPLATE;
import static com.ibsanalyzer.constants.Constants.NEW_PORTION_RANGES;

public class PortionStatSettingsActivity extends AppCompatActivity {
    PortionStatRangeAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Portion Stat Settings");
        setContentView(R.layout.activity_portion_stat_settings);
        adapter = new PortionStatRangeAdapter(getBaseContext());
        RecyclerView portionsView = (RecyclerView) findViewById(R.id.portionsIntervals);
        portionsView.setAdapter(adapter);

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
        super.onActivityResult(requestCode, resultCode, data);


    }
}
