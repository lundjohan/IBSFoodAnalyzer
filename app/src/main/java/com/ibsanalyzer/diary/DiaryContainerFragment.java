package com.ibsanalyzer.diary;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.threeten.bp.LocalDate;

import static com.ibsanalyzer.constants.Constants.LOCALDATE;
import static com.ibsanalyzer.constants.Constants.SWIPING_TO_DATE;

/**
 * This class uses an adapter that is using DiaryFragment
 *
 */
public class DiaryContainerFragment extends Fragment {
    //preferably even number, variables used for setting correct date after swipe
    private static int MAX_SLIDES = 1000;
    private static int START_POS_VIEWPAGER = MAX_SLIDES/2;
    ViewPager pager;
    DiarySlidePagerAdapter adapter;
    LocalDate startDate;
    int currentPagerItem;

    public DiaryContainerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diary_container, container, false);


        Bundle args = getArguments();
        if (args!= null) {
            startDate = (LocalDate) args.getSerializable(LOCALDATE);
        }
        if (startDate == null) {
            startDate = LocalDate.now();
        }
        adapter = new DiarySlidePagerAdapter(getChildFragmentManager());
        pager = (ViewPager) view.findViewById(R.id.pager);
        pager.setAdapter(adapter);
        pager.setCurrentItem(MAX_SLIDES / 2);
        currentPagerItem = pager.getCurrentItem();
        return view;
    }

    //Internal adapter class
    private class DiarySlidePagerAdapter extends FragmentStatePagerAdapter {
        public DiarySlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         *
         * @param position is used to set correct date also after swipe
         * @return
         */
        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new DiaryFragment();
            Bundle args = new Bundle();
            LocalDate nextDate = startDate.plusDays(position - START_POS_VIEWPAGER);
            args.putSerializable(SWIPING_TO_DATE, nextDate);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return MAX_SLIDES;
        }

    }
}
