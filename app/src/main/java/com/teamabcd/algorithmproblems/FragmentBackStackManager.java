package com.teamabcd.algorithmproblems;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import java.util.Stack;

/**
 * Created by Stackia on 11/4/14.
 */
public class FragmentBackStackManager {

    private NavigationBarHandler navigationBarHandler = null;
    private Stack<Fragment> backStack = new Stack<Fragment>();
    private FragmentManager fragmentManager = null;
    private int containerId;

    public FragmentBackStackManager(int containerId, Fragment rootFragment) {
        this.containerId = containerId;
        backStack.push(rootFragment);
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void setNavigationBarHandler(NavigationBarHandler navigationBarHandler) {
        this.navigationBarHandler = navigationBarHandler;
    }

    public void showTopFragment() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment topFragment = backStack.peek();
        if (topFragment.isAdded()) {
            transaction.show(topFragment);
        } else {
            transaction.add(containerId, topFragment);
        }
        transaction.commit();
        updateNavigationBarBackButtonEnabled();
    }

    public void pushFragment(Fragment fragment) {
        pushFragment(fragment, true);
    }

    public void pushFragment(Fragment fragment, boolean animated) {
        if (fragment == null) {
            return;
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment backFragment = backStack.peek();
        if (backFragment != null) {
            transaction.hide(backStack.peek());
        }
        transaction.add(containerId, fragment);
        transaction.commit();
        updateNavigationBarBackButtonEnabled();
        backStack.push(fragment);
    }

    public Fragment popFragment() {
        return popFragment(true);
    }

    public Fragment popFragment(boolean animated) {
        if (backStack.size() <= 1) { // Cannot pop root fragment
            return null;
        }
        Fragment currentFragment = backStack.pop();
        Fragment backFragment = backStack.peek();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.remove(currentFragment);
        transaction.show(backFragment);
        transaction.commit();
        updateNavigationBarBackButtonEnabled();
        return currentFragment;
    }

    private void updateNavigationBarBackButtonEnabled() {
        if (backStack.size() <= 1) {
            navigationBarHandler.setBackButtonEnabled(false);
        } else {
            navigationBarHandler.setBackButtonEnabled(true);
        }
    }
}
