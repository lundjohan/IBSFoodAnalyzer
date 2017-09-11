package com.ibsanalyzer.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.ibsanalyzer.inputday.DiaryFragment;
import com.ibsanalyzer.inputday.R;
import com.ibsanalyzer.inputday.StatFragment;
import com.ibsanalyzer.inputday.TemplateFragment;
import com.ibsanalyzer.settings.SettingsFragment;

/**
 * Created by Johan on 2017-04-18.
 * From Android Studio Development Essentials p. 334
 */

public class TabPagerAdapter extends FragmentPagerAdapter {
    int tabCount;
    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

    public TabPagerAdapter(FragmentManager fm, int nrOfTabs) {
        super(fm);
        this.tabCount = nrOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
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

    public DiaryFragment getDiaryFragment() {
        return (DiaryFragment) getRegisteredFragment(1);
    }
    public StatFragment getStatFragment() {
        return (StatFragment) getRegisteredFragment(2);
    }

    @Override
    public int getCount() {
        return tabCount;
    }

    //metods below are needed to get access to specigic fragment directly in code from MainActivity
    //from http://stackoverflow.com/questions/8785221/retrieve-a-fragment-from-a-viewpager
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}
