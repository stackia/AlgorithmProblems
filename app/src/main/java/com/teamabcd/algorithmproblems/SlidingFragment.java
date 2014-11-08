package com.teamabcd.algorithmproblems;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

abstract public class SlidingFragment extends Fragment {

    private NavigationBarHandler navigationBarHandler;
    private boolean noTitleAnimationNextTime = false;
    private boolean animating = false;

    abstract public int getNavigationBarTitleResource();

    public NavigationBarHandler getNavigationBarHandler() {
        return navigationBarHandler;
    }

    public void setNavigationBarTitleNoAnimationNextTime() {
        noTitleAnimationNextTime = true;
    }

    public void resetNavigationBarTitle() {
        if (navigationBarHandler == null) {
            return;
        }
        if (noTitleAnimationNextTime) {
            noTitleAnimationNextTime = false;
            navigationBarHandler.setNavigationBarTitle(getNavigationBarTitleResource(), false);
        } else {
            navigationBarHandler.setNavigationBarTitle(getNavigationBarTitleResource(), true);
        }
    }

    public boolean isAnimating() {
        return animating;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!isHidden()) {
            resetNavigationBarTitle();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            resetNavigationBarTitle();
        }
    }

    @Override
    public Animator onCreateAnimator(int transit, boolean enter, int nextAnim) {
        Animator animator = null;
        if (nextAnim > 0) {
            animator = AnimatorInflater.loadAnimator(getActivity(), nextAnim);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    animating = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    animating = false;
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    animating = false;
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
        }
        return animator;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            navigationBarHandler = (NavigationBarHandler) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationBarHandler interface.");
        }
    }
}
