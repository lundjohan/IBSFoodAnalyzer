package com.johanlund.screens.common.mvcviews;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.johanlund.ibsfoodanalyzer.R;

public abstract class ViewMvcAbstract implements WithOptionsMenuViewMvc {
    protected View rootView;

    @Override
    public View getRootView() {
        return rootView;
    }


    @Override
    public boolean createOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.info_menu, menu);
        inflater.inflate(R.menu.done_menu, menu);
        menu.findItem(R.id.menu_done).setOnMenuItemClickListener(new MenuItem
                .OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                doneClicked(null);
                return true;
            }
        });
        menu.findItem(R.id.menu_info).setOnMenuItemClickListener(new MenuItem
                .OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                    getListener().showInfo(getBarTitle(), getInfoLayout());
                return true;
            }
        });
        return true;
    }

    protected abstract ViewMvc.Listener getListener();
    protected abstract void doneClicked(View v);

    protected abstract int getInfoLayout();

    protected abstract String getBarTitle();


}
