package com.teamabcd.algorithmproblems;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
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
    private ProblemListAdapter problemListAdapter;

    public static ProblemListFragment newInstance() {
        ProblemListFragment fragment = new ProblemListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        problemListAdapter = new ProblemListAdapter(getActivity(), problemList);
        navigationBarHandler.setNavigationBarTitle(R.string.navigation_bar_title_problem_list);
        ProblemListAsyncLoader loader = new ProblemListAsyncLoader();
        loader.setAccount(new OJAccount(OJAccount.Type.HDU));
        loader.execute(0);
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

    private class ProblemListAsyncLoader extends AsyncTask<Integer, Integer, List<OJProblem>> {

        OJAccount account;

        public void setAccount(OJAccount account) {
            this.account = account;
        }

        @Override
        protected List<OJProblem> doInBackground(Integer... integers) {
            OJProblemFetcher problemFetcher = new OJProblemFetcher(account);
            return problemFetcher.fetchProblemList(integers[0]);
        }

        @Override
        protected void onPostExecute(List<OJProblem> result) {
            super.onPostExecute(result);
            problemList.clear();
            problemList.addAll(result);
            problemListAdapter.notifyDataSetChanged();
        }
    }

    private static class ProblemListAdapter extends ArrayAdapter<OJProblem> {
        private ProblemListAdapter(Context context, List<OJProblem> problemList) {
            super(context, 0, problemList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.list_cell_problem, null);
            }

            OJProblem problem = getItem(position);
            TextView idTextView = (TextView)view.findViewById(R.id.problemIdTextView);
            idTextView.setText(Integer.toString(problem.getId()));
            TextView titleTextView = (TextView)view.findViewById(R.id.problemTitleTextView);
            titleTextView.setText(problem.getTitle());

            return view;
        }
    }
}
