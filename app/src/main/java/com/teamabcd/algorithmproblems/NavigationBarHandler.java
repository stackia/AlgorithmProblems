package com.teamabcd.algorithmproblems;

/**
 * Created by Stackia on 11/4/14.
 */
public interface NavigationBarHandler {
    abstract public boolean isBackButtonEnabled();

    abstract public void setBackButtonEnabled(boolean enabled);

    abstract public void setNavigationBarTitle(int resId);

    abstract public String getNavigationBarTitle();

    abstract public void setNavigationBarTitle(String title);
}
