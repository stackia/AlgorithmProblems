package com.teamabcd.algorithmproblems;

import java.security.PrivateKey;
import java.util.PriorityQueue;

public interface NavigationBarHandler {

    abstract public void setCurrentBackStack(FragmentBackStackManager backStack);

    abstract public FragmentBackStackManager getCurrentBackStack();

    abstract public boolean isBackButtonEnabled();

    abstract public void setBackButtonEnabled(boolean enabled);

    abstract public void setNavigationBarTitle(int resId);

    abstract public String getNavigationBarTitle();

    abstract public void setNavigationBarTitle(String title);
}
