package com.johanlund.custom_views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.johanlund.ibsfoodanalyzer.R;

import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * Created by Johan on 2017-12-03.
 * <p>
 * It seems a bit complicated but the reason for this class is to use the same item base for all
 * events, in case I would like to change something later.
 */

public class EventLayout extends ConstraintLayout {
    public EventLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);

    }

    public EventLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        inflate(getContext(), R.layout.item_base, this);
        TypedArray arr = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.EventLayout,
                0, 0);
        int imgSrc = arr.getResourceId(R.styleable.EventLayout_itemImg, R.styleable
                .EventLayout_itemImg);
        int color = arr.getColor(R.styleable.EventLayout_itemColor, ContextCompat.getColor
                (context, R.color.colorLightBlue));
        boolean hasTags = arr.getBoolean(R.styleable.EventLayout_hasTags, false);

        ImageView icon = (ImageView) findViewById(R.id.item_icon);

        icon.setImageResource(imgSrc);
        icon.setBackgroundColor(color);

        //for Meal and Other events only
        if (hasTags) {
        }
    }
}
