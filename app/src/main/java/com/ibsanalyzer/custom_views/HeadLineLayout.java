package com.ibsanalyzer.custom_views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.widget.TextView;

import com.ibsanalyzer.diary.R;


/**
 * Created by Johan on 2018-01-28.
 */

public class HeadLineLayout extends ConstraintLayout {
    public HeadLineLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);

    }

    public HeadLineLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        inflate(getContext(), R.layout.custom_layout_stat_headline, this);
        TypedArray arr = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.HeadLineLayout,
                0, 0);
        //for the text string in textview
        String text = arr.getString(R.styleable.HeadLineLayout_textString);
        TextView headLineText = (TextView) findViewById(R.id.headLineText);
        headLineText.setText(text);

        boolean notReady = arr.getBoolean(R.styleable.HeadLineLayout_notReady, false);
        //in case function is not ready for production, do the following
        if (notReady){
            headLineText.setTextColor(getResources().getColor(R.color.colorWeakGrey,null));
        }
    }
}
