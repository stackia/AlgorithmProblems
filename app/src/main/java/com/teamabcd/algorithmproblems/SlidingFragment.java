package com.teamabcd.algorithmproblems;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

abstract public class SlidingFragment extends Fragment {

    private NavigationBarHandler navigationBarHandler;
    private boolean noTitleAnimationNextTime = false;

    abstract public int getNavigationBarTitleResource();

    public NavigationBarHandler getNavigationBarHandler() {
        return navigationBarHandler;
    }

    public void setNavigationBarTitleNoAnimationNextTime() {
        noTitleAnimationNextTime = true;
    }

    private void resetNavigationBarTitle() {
        if (noTitleAnimationNextTime) {
            noTitleAnimationNextTime = false;
            navigationBarHandler.setNavigationBarTitle(getNavigationBarTitleResource(), false);
        } else {
            navigationBarHandler.setNavigationBarTitle(getNavigationBarTitleResource(), true);
        }
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            navigationBarHandler = (NavigationBarHandler) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationBarHandler interface.");
        }
    }
}
