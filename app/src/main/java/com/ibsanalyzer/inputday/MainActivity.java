package com.ibsanalyzer.inputday;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ViewSwitcher;

import com.ibsanalyzer.base_classes.Event;

import java.io.Serializable;
import java.util.List;

import static com.ibsanalyzer.constants.Constants.LIST_OF_EVENTS;

public class MainActivity extends AppCompatActivity implements DiaryFragment.DiaryFragmentListener
        /*TemplateAdderActivity.TemplateAdderListener*/ {
    TabLayout tabLayout;
    ViewPager viewPager;
    TabPagerAdapter adapter;

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        //see Android Studio Development essentials p. 337
        viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new TabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        //start from DiaryFragment
        viewPager.setCurrentItem(1);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });

    }
    @Override
    public ViewSwitcher getTabsLayoutSwitcher() {
        return (ViewSwitcher) findViewById(R.id.tabLayoutSwitcher);
    }
    public void changeToTemplateFragment() {
        viewPager.setCurrentItem(0);
    }
    //==============================================================================================

    //this method gets marked events from DiaryFragment and calls EventsTemplateAdder to store them.
    @Override
    public void doEventsTemplateAdder(List<Event> events) {
        Intent intent = new Intent(this, TemplateAdderActivity.class);
        //Gson gson = new Gson();
        //String objAsJSON = gson.toJson(events);
        intent.putExtra(LIST_OF_EVENTS, (Serializable) events);
        startActivity(intent);
    }
}

