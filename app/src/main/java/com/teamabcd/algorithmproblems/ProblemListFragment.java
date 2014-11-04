package com.teamabcd.algorithmproblems;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.teamabcd.algorithmproblems.dummy.DummyContent;
import com.teamabcd.module.ojclient.OJAccount;
import com.teamabcd.module.ojclient.OJProblem;
import com.teamabcd.module.ojclient.OJProblemFetcher;

import org.apache.http.conn.scheme.PlainSocketFactory;

import java.util.ArrayList;
import java.util.List;

public class ProblemListFragment extends Fragment implements AbsListView.OnItemClickListener {

    private NavigationBarHandler navigationBarHandler;
    private List<OJProblem> problemList = new ArrayList<OJProblem>();
    private ListAdapter problemListAdapter;

    public static ProblemListFragment newInstance() {
        ProblemListFragment fragment = new ProblemListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        problemListAdapter = new ProblemListAdapter(getActivity(), problemList);
        navigationBarHandler.setNavigationBarTitle(R.string.navigation_bar_title_problem_list);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_problem_list, container, false);

        ListView problemListView = (ListView)view.findViewById(R.id.problemListView);
        problemListView.setAdapter(problemListAdapter);
        problemListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            navigationBarHandler = (NavigationBarHandler)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationBarHandler interface.");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

    }

    private static class ProblemListAdapter extends ArrayAdapter<OJProblem> {
        private ProblemListAdapter(Context context, List<OJProblem> problemList) {
            super(context, 0, problemList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            return super.getView(position, convertView, parent);
        }
    }
}
