package com.ibsanalyzer.statistics;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.ibsanalyzer.diary.R;

public class PortionStatSettingsActivity extends AppCompatActivity {
    PortionStatRangeAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Portion Stat Settings");
        setContentView(R.layout.activity_portion_stat_settings);
        adapter = new PortionStatRangeAdapter();
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

    }
}
