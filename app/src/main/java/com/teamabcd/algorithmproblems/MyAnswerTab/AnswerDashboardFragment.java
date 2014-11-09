package com.teamabcd.algorithmproblems.MyAnswerTab;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.teamabcd.algorithmproblems.Activity.LoginActivity;
import com.teamabcd.algorithmproblems.Activity.MainActivity;
import com.teamabcd.algorithmproblems.CustomView.SlidingFragment;
import com.teamabcd.algorithmproblems.R;

/**
 * Project: Algorithm Problems
 * Created by: Stackia <jsq2627@gmail.com>
 * Date: 11/9/14
 */
public class AnswerDashboardFragment extends SlidingFragment implements View.OnClickListener, LoginActivity.OnLoginListener {

    public static AnswerDashboardFragment newInstance() {
        AnswerDashboardFragment fragment = new AnswerDashboardFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_answer_dashboard, container, false);
        Button loginButton = (Button) view.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginButton:
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.startLoginActivity(getId());
                break;
        }
    }

    @Override
    public int getNavigationBarTitleResource() {
        return R.string.navigation_bar_title_my_answer;
    }

    @Override
    public void onLogin() {

    }
}
