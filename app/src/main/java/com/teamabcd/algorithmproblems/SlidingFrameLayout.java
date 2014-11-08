package com.teamabcd.algorithmproblems;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class SlidingFrameLayout extends FrameLayout {

    private Point screenSize = new Point();

    public SlidingFrameLayout(Context context) {
        super(context);
    }

    public SlidingFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlidingFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public float getXFraction() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getSize(screenSize);
        return (screenSize.x == 0) ? 0 : getX() / (float) screenSize.x;
    }

    public void setXFraction(float xFraction) {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getSize(screenSize);
        setX((screenSize.x > 0) ? (xFraction * screenSize.x) : 0);
    }
}
