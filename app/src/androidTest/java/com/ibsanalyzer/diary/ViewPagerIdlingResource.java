package com.ibsanalyzer.diary;

import android.support.test.espresso.IdlingResource;
import android.support.v4.view.ViewPager;

/**
 * Created by Johan on 2017-05-22.
 * This class is for espresso testing.
 * ViewPager needs to be waited for, before it finishes changing fragments.
 * Even though animations are turned off on phone, ViewPager will not be affected.
 * This class takes a ViewPager and sets uses the IdlingResource implementation for thread to
 * wait on it. /Johan
 * <p>
 * taken from https://github.com/Karumi/Rosie/blob/master/sample/src/androidTest/java/com/karumi
 * /rosie/sample/idlingresources/ViewPagerIdlingResource.java
 * together with (very similar): https://stackoverflow
 * .com/questions/31056918/wait-for-view-pager-animations-with-espresso
 */

public class ViewPagerIdlingResource implements IdlingResource {


    private boolean mIdle = true; // Default to idle since we can't query the scroll state.

    private ResourceCallback mResourceCallback;

    public ViewPagerIdlingResource(ViewPager viewPager) {
        viewPager.addOnPageChangeListener(new ViewPagerListener());
    }

    @Override
    public String getName() {
        return "View Pager Idling Resource";
    }

    @Override
    public boolean isIdleNow() {
        return mIdle;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        mResourceCallback = resourceCallback;
    }

    private class ViewPagerListener extends ViewPager.SimpleOnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int state) {
            mIdle = (state == ViewPager.SCROLL_STATE_IDLE
                    // Treat dragging as idle, or Espresso will block itself when swiping.
                    || state == ViewPager.SCROLL_STATE_DRAGGING);
            if (mIdle && mResourceCallback != null) {
                mResourceCallback.onTransitionToIdle();
            }
        }
    }
}