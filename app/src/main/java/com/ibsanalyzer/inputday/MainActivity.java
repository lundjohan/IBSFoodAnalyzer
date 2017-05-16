package com.ibsanalyzer.inputday;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ibsanalyzer.base_classes.Event;
import com.ibsanalyzer.model.EventsTemplate;
import com.ibsanalyzer.template.TemplateAdderFragment;

import java.io.Serializable;
import java.util.List;

import static com.ibsanalyzer.constants.Constants.LIST_OF_EVENTS;

public class MainActivity extends AppCompatActivity implements DiaryFragment.DiaryFragmentListener,
        TemplateAdderFragment.TemplateAdderListener, TabPagerAdapter.MiddlePageFragmentListener {
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
  /*  private void newTemplateAdderActivity(View v) {
        Intent intent = new Intent(this, TemplateAdderFragment.class);
        //lägg in markerade event.
        List<Event> eventsToSend = new ArrayList<>();
        for (int i: eventsMarked){
            eventsToSend.add(eventList.get(i));
        }
        Gson gson = new Gson();
        String objAsJSON = gson.toJson(eventsToSend);
        intent.putExtra(MARKED_EVENTS_JSON, objAsJSON);
        startActivity(intent);
    }*/

    /*
    The AppBar has 2 different versions, depending on whether items are marked or not in DiaryFragment
     */
    /*public void changeToTabbedMenu() {
        tabLayout.setVisibility(View.VISIBLE);
    }

    public void changeToMarkedMenu() {
        tabLayout.setVisibility(View.INVISIBLE);
    }*/


    //receivingData from TemplateAdderFragment and posting it to TemplateFragment
    /*public void eventsToTemplateFragment(String jsonWithEvents){
        TemplateFragment templateFragment = (TemplateFragment)adapter.getRegisteredFragment(0);
        templateFragment.retrieveEventsAsJSON (jsonWithEvents);
    }*/


    //==============================================================================================
    // Communication between thos Activity and child Fragments regarding EventsTemplates
    //==============================================================================================
    //start TemplateAdderFragment
    //=> events
    //<= EventsTemplate
    // start TemplateFragment
    //=> EventsTemplate
    //klar!
    //from DiaryFragment
    @Override
    public void eventsToTemplateAdderFragment(List<Event> events) {
        onSwitchToTemplateAdderFragment(events);
        //start TemplateAdderFragment
        //p. 252

    }

    //from TemplateAdderFragment
    @Override
    public void startTemplateFragment() {

        Log.d("Debug", "Inside MainActivity:startTemplateFragment");


        //same as Fragment templateFragment = new TemplateFragment();
        Fragment templateFragment = adapter.getItem(0);
        getSupportFragmentManager().beginTransaction().replace(R.id.pager, templateFragment).commit();
    }

    @Override
    public void onSwitchToTemplateAdderFragment(List<Event> events) {
        TemplateAdderFragment taf = new TemplateAdderFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(LIST_OF_EVENTS, (Serializable) events);
        taf.setArguments(bundle);

        //osäker om pager (container view) är rätt id to pass
        getSupportFragmentManager().beginTransaction().replace(R.id.pager, taf).commit();
    }

    //==============================================================================================
}

