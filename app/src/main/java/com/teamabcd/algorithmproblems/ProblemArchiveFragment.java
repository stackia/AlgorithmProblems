package com.teamabcd.algorithmproblems;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ProblemArchiveFragment extends Fragment {

    private FragmentBackStackManager backStackManager;

    public static ProblemArchiveFragment newInstance() {
        ProblemArchiveFragment fragment = new ProblemArchiveFragment();
        ProblemListFragment problemListFragment = ProblemListFragment.newInstance();
        fragment.backStackManager = new FragmentBackStackManager(R.id.problemArchiveFrameContainer, problemListFragment);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_problem_archive, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            NavigationBarHandler navigationBarHandler = (NavigationBarHandler)activity;
            backStackManager.setNavigationBarHandler(navigationBarHandler);
            backStackManager.setFragmentManager(getFragmentManager());
            backStackManager.showTopFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationBarHandler interface.");
        }
    }
}