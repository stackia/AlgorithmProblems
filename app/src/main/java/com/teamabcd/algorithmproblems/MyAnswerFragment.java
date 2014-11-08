package com.teamabcd.algorithmproblems;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MyAnswerFragment extends Fragment {

    public final static String tag = "MY_ANSWER_FRAGMENT";

    private FragmentBackStackManager backStackManager;

    public static MyAnswerFragment newInstance() {
        MyAnswerFragment fragment = new MyAnswerFragment();
        AnswerDashboardFragment answerDashboardFragment = AnswerDashboardFragment.newInstance();
        fragment.setRetainInstance(true);
        fragment.backStackManager = new FragmentBackStackManager(R.id.myAnswerFrameContainer, answerDashboardFragment);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        backStackManager.showTopFragment();
        return inflater.inflate(R.layout.fragment_my_answer, container, false);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            backStackManager.showTopFragment();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            NavigationBarHandler navigationBarHandler = (NavigationBarHandler) activity;
            backStackManager.setNavigationBarHandler(navigationBarHandler);
            backStackManager.setFragmentManager(getFragmentManager());
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationBarHandler interface.");
        }
    }
}
