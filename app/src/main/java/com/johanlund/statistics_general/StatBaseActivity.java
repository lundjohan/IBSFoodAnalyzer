package com.johanlund.statistics_general;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.johanlund.base_classes.Break;
import com.johanlund.base_classes.Chunk;
import com.johanlund.base_classes.Event;
import com.johanlund.database.DBHandler;
import com.johanlund.ibsfoodanalyzer.R;
import com.johanlund.info.InfoActivity;
import com.johanlund.statistics_point_classes.PointBase;
import com.johanlund.util.ScoreTimesBase;
import com.johanlund.util.TagsWrapperBase;

import org.threeten.bp.LocalDateTime;

import java.util.List;

import static com.johanlund.constants.Constants.HOURS_AHEAD_FOR_BREAK_BACKUP;

public abstract class StatBaseActivity <E extends PointBase> extends AppCompatActivity {
    protected RecyclerView recyclerView;
    protected StatAdapter<E> adapter;
    protected LinearLayoutManager layoutManager;
    protected StatAsyncTask asyncThread;

    /**
     * This method is used in onCreate lower in hierarchy
     */
    protected void continueOnCreate() {

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager
                .VERTICAL, true);
        recyclerView.setLayoutManager(layoutManager);

        //Don't reduce this two lines to one. Variable adapter is needed later in AsyncTask and must be initialized.
        adapter = getStatAdapter();
        recyclerView.setAdapter(adapter);
        //add line separator
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView
                .getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);
        getSupportActionBar().setTitle(getStringForTitle());
        calculateStats();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        if (asyncThread!= null && !asyncThread.isCancelled()){
            asyncThread.cancel(true);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_info, menu);
        menu.findItem(R.id.menu_info).setOnMenuItemClickListener(new MenuItem
                .OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                infoClicked();
                return true;
            }
        });
        return true;
    }

    private void infoClicked() {
        InfoActivity.newInfoActivity(this, getInfoStr());
    }

    protected abstract String getInfoStr();


    protected abstract void calculateStats();


    @Override
    public void onBackPressed() {
        finish();
    }

    //in case API<21 onBackPressed is not called
    //this is blocking natural behavoiur of backbutton
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    public abstract StatAdapter<E> getStatAdapter();

    public abstract String getStringForTitle();
    public abstract ScoreWrapperBase<E> getScoreWrapper();
    //public abstract List<ScoreTimesBase> getScoreTimesBases(List<LocalDateTime> allBreaks);
}
