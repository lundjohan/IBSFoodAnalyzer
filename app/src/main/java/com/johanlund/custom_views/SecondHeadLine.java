package com.johanlund.custom_views;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.util.AttributeSet;
import android.widget.TextView;

import com.johanlund.ibsfoodanalyzer.R;

/**
 * Created by Johan on 2018-02-19.
 */

public class SecondHeadLine extends ConstraintLayout {

    public SecondHeadLine(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SecondHeadLine(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        inflate(getContext(), R.layout.custom_layout_second_headline, this);
        TypedArray arr = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.SecondHeadLineLayout,
                0, 0);
        //for the text string in textview
        String text = arr.getString(R.styleable.SecondHeadLineLayout_textForH2);
        TextView headLineText = (TextView) findViewById(R.id.h2);
        headLineText.setText(text);
    }
}
