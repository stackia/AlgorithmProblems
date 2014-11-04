package com.teamabcd.algorithmproblems;

/**
 * Created by Stackia on 11/4/14.
 */
public interface NavigationBarHandler {
    abstract public void setBackButtonEnabled(boolean enabled);
    abstract public boolean isBackButtonEnabled();
    abstract public void setNavigationBarTitle(int resId);
    abstract public void setNavigationBarTitle(String title);
    abstract public String getNavigationBarTitle();
}
