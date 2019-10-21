package com.nerosong.sittingmonitor;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyViewPager extends ViewPager {
    private boolean result = false;

    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (result) {

            return super.onInterceptTouchEvent(ev);
        } else {

            return false;

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (result) {
            return super.onTouchEvent(ev);
        } else {
            return false;
        }
    }
}

