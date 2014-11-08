package com.teamabcd.algorithmproblems;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import java.util.Stack;

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
        SlidingFragment topFragment = (SlidingFragment) backStack.peek();
        topFragment.setNavigationBarTitleNoAnimationNextTime();
        if (topFragment.isAdded()) {
            transaction.show(topFragment);
        } else {
            transaction.add(containerId, topFragment);
        }
        transaction.commit();
        updateNavigationBarBackButtonEnabled(false);
    }

    public void pushFragment(Fragment fragment) {
        pushFragment(fragment, true);
    }

    public void pushFragment(Fragment fragment, boolean animated) {
        if (fragment == null) {
            return;
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (animated) {
            transaction.setCustomAnimations(R.animator.slide_left_in, R.animator.zoom_exit);
        }
        Fragment backFragment = backStack.peek();
        if (!isPoppable()) {
            transaction.hide(backStack.peek());
        }
        transaction.add(containerId, fragment);
        transaction.commit();
        backStack.push(fragment);
        updateNavigationBarBackButtonEnabled(true);
    }

    public Fragment popFragment() {
        return popFragment(true);
    }

    public Fragment popFragment(boolean animated) {
        if (!isPoppable()) { // Cannot pop root fragment
            return null;
        }
        Fragment currentFragment = backStack.pop();
        Fragment backFragment = backStack.peek();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (animated) {
            transaction.setCustomAnimations(R.animator.zoom_enter, R.animator.slide_right_out);
        }
        transaction.remove(currentFragment);
        transaction.show(backFragment);
        transaction.commit();
        updateNavigationBarBackButtonEnabled(true);
        return currentFragment;
    }

    public boolean isPoppable() {
        return backStack.size() > 1;
    }

    private void updateNavigationBarBackButtonEnabled(boolean animated) {
        navigationBarHandler.setCurrentBackStack(this);
        if (isPoppable()) {
            navigationBarHandler.setBackButtonEnabled(true, animated);
        } else {
            navigationBarHandler.setBackButtonEnabled(false, animated);
        }
    }
}
