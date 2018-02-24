package com.johanlund.info;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static com.johanlund.constants.Constants.LAYOUT_RESOURCE;

/**
 * Created by Johan on 2018-02-19.
 */

public class InfoFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle b = getArguments();
        int resource = b.getInt(LAYOUT_RESOURCE);
        View view = inflater.inflate(resource, container, false);
        return view;
    }
}
