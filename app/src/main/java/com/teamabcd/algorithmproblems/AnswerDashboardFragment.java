package com.teamabcd.algorithmproblems;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class AnswerDashboardFragment extends SlidingFragment implements View.OnClickListener {

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
        Button testLoginEntrance = (Button) view.findViewById(R.id.testLoginEntrance);
        testLoginEntrance.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.testLoginEntrance:
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public int getNavigationBarTitleResource() {
        return R.string.navigation_bar_title_my_answer;
    }
}
