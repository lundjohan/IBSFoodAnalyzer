package com.ibsanalyzer.drawer;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ibsanalyzer.base_classes.Event;
import com.ibsanalyzer.diary.DiaryContainerFragment;
import com.ibsanalyzer.diary.DiaryFragment;
import com.ibsanalyzer.diary.R;
import com.ibsanalyzer.diary.StatOptionsFragment;
import com.ibsanalyzer.diary.TemplateFragment;

import java.util.List;

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DiaryFragment
        .DiaryFragmentListener, StatOptionsFragment.StatOptionsListener, TemplateFragment
        .TemplateFragmentListener {

    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string
                .navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(true);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initiateFragment();
    }

    private void initiateFragment() {
        Fragment fragment = new DiaryContainerFragment();
        FragmentManager transaction = getSupportFragmentManager();
        transaction.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            getSupportFragmentManager().popBackStackImmediate();
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.three_dots_menu_item_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        //this is solely used from TemplateFragment
        if (id == android.R.id.home) {
            backToDiaryFragment();

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_diary) {
            Fragment fragment = new DiaryContainerFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();

        } else if (id == R.id.nav_statistics) {
            Fragment fragment = new StatOptionsFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void startTemplateFragment() {
        //toggle.setHomeAsUpIndicator(null);
        Fragment fragment = new TemplateFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    public void doEventsTemplateAdder(List<Event> events) {

    }

    @Override
    public void addEventsFromEventsTemplateToDiary(List<Event> events) {
        // DiaryFragment diary = accessDiaryFragment();
        // diary.addEventsToDiary(events);
    }

    @Override
    public void fixToolBarForTemplateFragment() {
        toolbar.setTitle("Templates");
        toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string
                .navigation_drawer_close);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);// show back button
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //without this hamburger button is shown instead of back button
        //see https://stackoverflow.com/questions/27742074/up-arrow-does-not-show-after-calling
        // -actionbardrawertoggle-setdrawerindicatorena
        toggle.setDrawerIndicatorEnabled(false);
        toggle.syncState();


    }
    //called after back button of TemplateFragment is pressed
    //this is not nice code but couldn't get backstack to work together with drawer
    private void backToDiaryFragment(){
       // getSupportFragmentManager().popBackStackImmediate();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);// unshow back button
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string
                .navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(true);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        Fragment fragment = new DiaryContainerFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
