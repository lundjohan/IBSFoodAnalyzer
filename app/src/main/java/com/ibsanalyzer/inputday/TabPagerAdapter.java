package com.ibsanalyzer.inputday;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

/**
 * Created by Johan on 2017-04-18.
 * From Android Studio Development Essentials p. 334
 */

public class TabPagerAdapter extends FragmentPagerAdapter {
    int tabCount;

    public TabPagerAdapter(FragmentManager fm, int nrOfTabs){
        super(fm);
        this.tabCount = nrOfTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                Log.d("Debugging","Inuti TabPagerAdapter getItem"); //det här sker aldrig...
                TemplateFragment tabT = new TemplateFragment();
                return tabT;
            case 1:
                DiaryFragment tabD = new DiaryFragment();
                return tabD;
            case 2:
                StatFragment tabS = new StatFragment();
                return tabS;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
