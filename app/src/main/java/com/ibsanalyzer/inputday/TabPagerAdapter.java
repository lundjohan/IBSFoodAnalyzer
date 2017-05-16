package com.ibsanalyzer.inputday;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ibsanalyzer.base_classes.Event;
import com.ibsanalyzer.template.TemplateAdderFragment;

import java.util.List;

import static android.os.Build.VERSION_CODES.N;

/**
 * Created by Johan on 2017-04-18.
 * From Android Studio Development Essentials p. 334
 */

public class TabPagerAdapter extends FragmentPagerAdapter {
    int tabCount;
    private Fragment fragmentAtPos1;
    FragmentManager fm;
    // SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

    public TabPagerAdapter(FragmentManager fm, int nrOfTabs) {
        super(fm);
        this.tabCount = nrOfTabs;
        this.fm = fm;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                TemplateFragment tabT = new TemplateFragment();
                return tabT;

            //place for DiaryFragment, but at this position also an EventsTemplateAdderFragment can be created.
            //
            case 1:
                if (fragmentAtPos1 == null) {
                    //place is empty means that mainActivity has not created other fragments to fill this place
                    //=> restart a DiaryFragment
                    fragmentAtPos1 = DiaryFragment.newInstance(new MiddlePageFragmentListener() {
                        public void onSwitchToTemplateAdderFragment(List<Event>events)
                        {
                            fm.beginTransaction().remove(fragmentAtPos1).commit();
                            fragmentAtPos1 = TemplateAdderFragment.newInstance(events);
                            notifyDataSetChanged();
                        }
                    });
                }
                return fragmentAtPos1;
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

    //metods below are needed to get access to specigic fragment directly in code from MainActivity
    //from http://stackoverflow.com/questions/8785221/retrieve-a-fragment-from-a-viewpager
 /*   @Override
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
    }*/


    //problems with creating fragment in MainActivity when it is already using a ViewPager.
    //the problem is that the new fragment doesnt show up, the container just leaves an empty area.
    // for further discussion and where this code has been loaned from see:
    //http://stackoverflow.com/questions/7723964/replace-fragment-inside-a-viewpager/18612147#18612147
    @Override
    public int getItemPosition(Object object) {
        if (object instanceof DiaryFragment && fragmentAtPos1 instanceof TemplateAdderFragment)
            return POSITION_NONE;
        return POSITION_UNCHANGED;
    }
    public interface MiddlePageFragmentListener {
        void onSwitchToTemplateAdderFragment(List<Event> events);
    }




}

