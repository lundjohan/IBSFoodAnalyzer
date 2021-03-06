package com.johanlund.custom_views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.TextView;

import com.johanlund.ibsfoodanalyzer.R;

/**
 * Created by Johan on 2018-01-28.
 */

public class StatMenuItemLayout extends ConstraintLayout {
    public StatMenuItemLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);

    }

    public StatMenuItemLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        inflate(getContext(), R.layout.item_stat_menu, this);
        TypedArray arr = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.StatMenuItemLayout,
                0, 0);
        //set text and id of the textview
        TextView statText = (TextView) findViewById(R.id.name_of_stat_option);
        String text = arr.getString(R.styleable.StatMenuItemLayout_textStatMenu);
        statText.setText(text);
        int idTextView = arr.getResourceId(R.styleable.StatMenuItemLayout_idOfSettingsTextView, 0);
        statText.setId(idTextView);

        //set id of settingsBtn
        ImageButton settingsBtn = (ImageButton) findViewById(R.id.settings_btn_inside_listView);
        int idBtn = arr.getResourceId(R.styleable.StatMenuItemLayout_idOfSettingsBtn, 0);
        settingsBtn.setId(idBtn);
    }
}