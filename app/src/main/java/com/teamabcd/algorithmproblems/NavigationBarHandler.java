package com.teamabcd.algorithmproblems;


public interface NavigationBarHandler {

    public FragmentBackStackManager getCurrentBackStack();

    public void setCurrentBackStack(FragmentBackStackManager backStack);

    public boolean isBackButtonEnabled();

    public void setBackButtonEnabled(boolean enabled, boolean animated);

    public void setNavigationBarTitle(int resId, boolean animated);

    public String getNavigationBarTitle();

    public void setNavigationBarTitle(String title, boolean animated);
}
