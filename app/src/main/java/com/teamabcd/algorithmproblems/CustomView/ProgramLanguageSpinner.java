package com.teamabcd.algorithmproblems.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

/**
 * Project: AlgorithmProblems
 * Created by: Stackia <jsq2627@gmail.com>
 * Date: 11/10/14
 */
public class ProgramLanguageSpinner extends Spinner {
    public ProgramLanguageSpinner(Context context) {
        super(context);
    }

    public ProgramLanguageSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProgramLanguageSpinner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int spinnerPaddingLeft = this.getPaddingLeft();
        final int spinnerPaddingRight = this.getPaddingRight();
        final int spinnerWidth = this.getWidth();
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST) {
            setMeasuredDimension(Math.min(measureContentWidth(getAdapter()), MeasureSpec.getSize(widthMeasureSpec)), getMeasuredHeight());
        }
    }

    int measureContentWidth(SpinnerAdapter adapter) {
        if (adapter == null) {
            return 0;
        }

        int width;
        View itemView;
        final int widthMeasureSpec =
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        final int heightMeasureSpec =
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);

        itemView = adapter.getView(getSelectedItemPosition(), null, this);
        if (itemView.getLayoutParams() == null) {
            itemView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        itemView.measure(widthMeasureSpec, heightMeasureSpec);
        width = itemView.getMeasuredWidth();

        return width;
    }
}
