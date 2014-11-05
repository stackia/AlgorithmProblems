package com.teamabcd.algorithmproblems;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamabcd.module.ojclient.OJProblem;

public class ProblemDetailFragment extends Fragment {

    private OJProblem problem;

    public static ProblemDetailFragment newInstance(OJProblem problem) {
        ProblemDetailFragment fragment = new ProblemDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(OJProblem.tag, problem);
        fragment.setArguments(args);
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            problem = (OJProblem)args.get(OJProblem.tag);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_problem_detail, container, false);
        return view;
    }
}
