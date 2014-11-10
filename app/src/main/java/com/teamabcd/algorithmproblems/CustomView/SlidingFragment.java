package com.teamabcd.algorithmproblems.CustomView;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import com.teamabcd.algorithmproblems.CustomBackStackController.FragmentBackStackManager;

/**
 * Project: Algorithm Problems
 * Created by: Stackia <jsq2627@gmail.com>
 * Date: 11/6/14
 */
abstract public class SlidingFragment extends Fragment {

    private FragmentBackStackManager.NavigationBarHandler navigationBarHandler;
    private boolean noTitleAnimationNextTime = false;
    private boolean noActionButtonAnimationNextTime = false;
    private boolean animating = false;

    abstract public int getNavigationBarTitleResource();

    abstract public FragmentBackStackManager.NavigationBarHandler.NavigationBarActionButton getNavigationBarActionButton();

    public FragmentBackStackManager.NavigationBarHandler getNavigationBarHandler() {
        return navigationBarHandler;
    }

    public void setNoTitleAnimationNextTime() {
        noTitleAnimationNextTime = true;
    }

    public void setNoActionBarAnimationNextTime() {
        noActionButtonAnimationNextTime = true;
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

    public void resetNavigationBarActionButton() {
        if (navigationBarHandler == null) {
            return;
        }
        FragmentBackStackManager.NavigationBarHandler.NavigationBarActionButton navigationBarActionButton = getNavigationBarActionButton();
        if (noActionButtonAnimationNextTime) {
            noActionButtonAnimationNextTime = false;
            navigationBarHandler.setNavigationBarActionButton(navigationBarActionButton, false);
        } else {
            navigationBarHandler.setNavigationBarActionButton(navigationBarActionButton, true);
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
            resetNavigationBarActionButton();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            resetNavigationBarTitle();
            resetNavigationBarActionButton();
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
            navigationBarHandler = (FragmentBackStackManager.NavigationBarHandler) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationBarHandler interface.");
        }
    }
}
