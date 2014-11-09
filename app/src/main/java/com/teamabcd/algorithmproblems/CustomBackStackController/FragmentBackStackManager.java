package com.teamabcd.algorithmproblems.CustomBackStackController;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.teamabcd.algorithmproblems.CustomView.SlidingFragment;
import com.teamabcd.algorithmproblems.R;

import java.util.Stack;

/**
 * Project: Algorithm Problems
 * Created by: Stackia <jsq2627@gmail.com>
 * Date: 11/4/14
 */
public class FragmentBackStackManager {

    private NavigationBarHandler navigationBarHandler = null;
    private Stack<SlidingFragment> backStack = new Stack<SlidingFragment>();
    private FragmentManager fragmentManager = null;
    private int containerId;

    public FragmentBackStackManager(int containerId, SlidingFragment rootFragment) {
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
        SlidingFragment topFragment = backStack.peek();
        topFragment.setNavigationBarTitleNoAnimationNextTime();
        if (topFragment.isAdded()) {
            transaction.show(topFragment);
        } else {
            transaction.add(containerId, topFragment);
        }
        transaction.commit();
        topFragment.resetNavigationBarTitle();
        updateNavigationBarBackButtonEnabled(false);
    }

    public boolean isAnimating() {
        if (backStack.size() == 1) {
            return backStack.peek().isAnimating();
        } else if (backStack.size() > 1) {
            return backStack.peek().isAnimating() || backStack.get(backStack.size() - 2).isAnimating();
        }
        return false;
    }

    public void pushFragment(SlidingFragment fragment) {
        pushFragment(fragment, true);
    }

    public void pushFragment(SlidingFragment fragment, boolean animated) {
        if (fragment == null) {
            return;
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (animated) {
            transaction.setCustomAnimations(R.animator.slide_left_in, R.animator.zoom_exit);
        }
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

    public static interface NavigationBarHandler {

        public FragmentBackStackManager getCurrentBackStack();

        public void setCurrentBackStack(FragmentBackStackManager backStack);

        public boolean isBackButtonEnabled();

        public void setBackButtonEnabled(boolean enabled, boolean animated);

        public void setNavigationBarTitle(int resId, boolean animated);

        public String getNavigationBarTitle();

        public void setNavigationBarTitle(String title, boolean animated);
    }
}
