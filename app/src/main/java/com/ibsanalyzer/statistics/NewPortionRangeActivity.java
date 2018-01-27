package com.ibsanalyzer.statistics;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ibsanalyzer.diary.R;
import com.ibsanalyzer.util.Util;

import static com.ibsanalyzer.constants.Constants.CHOSEN_FROM_RANGE;
import static com.ibsanalyzer.constants.Constants.CHOSEN_TO_RANGE;

public class NewPortionRangeActivity extends AppCompatActivity {
    TextView from;
    TextView to;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Add Portion Range");
        setContentView(R.layout.activity_new_portion_range);
        from = (TextView) findViewById(R.id.fromRangePortions);
        final Activity here = this;
        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.useNumberPickerDialog(here, from);
            }
        });

        to = (TextView) findViewById(R.id.toRangePortions);
        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.useNumberPickerDialog(here, to);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.done_menu, menu);
        menu.findItem(R.id.menu_done).setOnMenuItemClickListener(new MenuItem
                .OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                doneClicked();
                return true;
            }
        });
        return true;
    }

    //send data back
    private void doneClicked() {
        addRangeToIntent();
        finish();
    }

    private void addRangeToIntent() {
        Intent data = getIntent();
        data.putExtra(CHOSEN_FROM_RANGE, Float.parseFloat(from.getText().toString()));
        data.putExtra(CHOSEN_TO_RANGE, Float.parseFloat(to.getText().toString()));
        setResult(RESULT_OK,data);
    }
}
