package com.teamabcd.algorithmproblems;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.teamabcd.module.ojclient.OJAccount;
import com.teamabcd.module.ojclient.OJAccountOperator;
import com.teamabcd.module.ojclient.OJProblem;
import com.teamabcd.module.ojclient.OJProblemFetcher;

import java.util.ArrayList;
import java.util.List;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;

public class ProblemListFragment extends SlidingFragment implements ListView.OnItemClickListener, View.OnClickListener {

    private List<OJProblem> problemList = new ArrayList<OJProblem>();
    private ProblemListAdapter problemListAdapter;
    private MainActivity.FetchState fetchState = MainActivity.FetchState.Working;

    public static ProblemListFragment newInstance() {
        ProblemListFragment fragment = new ProblemListFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        problemListAdapter = new ProblemListAdapter(getActivity(), problemList);

        // TODO: Move loader out here
        ProblemListAsyncLoader loader = new ProblemListAsyncLoader();
        loader.setAccount(new OJAccount(OJAccount.Type.HDU, "jsq2627", "jsq2627_kz"));
        loader.execute(0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_problem_list, container, false);

        TextView fetchErrorTextView = (TextView) view.findViewById(R.id.fetchErrorTextView);
        fetchErrorTextView.setOnClickListener(this);

        ListView problemListView = (ListView) view.findViewById(R.id.problemListView);
        problemListView.setAdapter(problemListAdapter);
        problemListView.setOnItemClickListener(this);

        problemListAdapter.notifyDataSetChanged(view);

        return view;
    }

    @Override
    public int getNavigationBarTitleResource() {
        return R.string.navigation_bar_title_problem_list;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        getNavigationBarHandler().getCurrentBackStack().pushFragment(ProblemDetailFragment.newInstance(problemList.get(position)));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fetchErrorTextView:
                fetchState = MainActivity.FetchState.Working;
                problemListAdapter.notifyDataSetChanged();

                // TODO: Move loader out here
                ProblemListAsyncLoader loader = new ProblemListAsyncLoader();
                loader.setAccount(new OJAccount(OJAccount.Type.HDU, "jsq2627", "jsq2627_kz"));
                loader.execute(0);
                break;
        }
    }

    private class ProblemListAdapter extends ArrayAdapter<OJProblem> {
        private ProblemListAdapter(Context context, List<OJProblem> problemList) {
            super(context, 0, problemList);
        }

        public void notifyDataSetChanged(View view) {
            CircularProgressBar loadingProgressBar = (CircularProgressBar) view.findViewById(R.id.loadingProgressBar);
            TextView fetchErrorTextView = (TextView) view.findViewById(R.id.fetchErrorTextView);
            ListView problemListView = (ListView) view.findViewById(R.id.problemListView);

            switch (fetchState) {
                case Working:
                    loadingProgressBar.setVisibility(View.VISIBLE);
                    fetchErrorTextView.setVisibility(View.GONE);
                    problemListView.setVisibility(View.GONE);
                    break;

                case Failed:
                    loadingProgressBar.setVisibility(View.GONE);
                    fetchErrorTextView.setVisibility(View.VISIBLE);
                    problemListView.setVisibility(View.GONE);
                    break;

                case Successful:
                    loadingProgressBar.setVisibility(View.GONE);
                    fetchErrorTextView.setVisibility(View.GONE);
                    problemListView.setVisibility(View.VISIBLE);
                    super.notifyDataSetChanged();
                    break;
            }
        }

        @Override
        public void notifyDataSetChanged() {
            View view = ProblemListFragment.this.getView();
            if (view != null) {
                notifyDataSetChanged(view);
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.list_cell_problem, null);
            }

            Resources resources = view.getResources();
            OJProblem problem = getItem(position);

            TextView idTextView = (TextView) view.findViewById(R.id.problemIdTextView);
            idTextView.setText(Integer.toString(problem.getId()));

            TextView titleTextView = (TextView) view.findViewById(R.id.problemTitleTextView);
            titleTextView.setText(Html.fromHtml(problem.getTitle()));

            TextView statisticsTextView = (TextView) view.findViewById(R.id.problemStatisticsTextView);
            statisticsTextView.setText(
                    String.format(
                            resources.getString(R.string.problem_statistics_text_format),
                            (double) problem.getAcceptedSubmission() * 100 / problem.getTotalSubmission(),
                            problem.getAcceptedSubmission(),
                            problem.getTotalSubmission()
                    )
            );

            ImageView stateIconView = (ImageView) view.findViewById(R.id.problemStateIconView);
            TextView stateTextView = (TextView) view.findViewById(R.id.problemStateTextView);
            switch (problem.getSolveStatus()) {
                case ACCEPT:
                    stateIconView.setImageResource(R.drawable.problem_state_accept_icon);
                    stateTextView.setText(resources.getString(R.string.problem_state_accept));
                    break;

                case TRIED_BUT_WRONG:
                    stateIconView.setImageResource(R.drawable.problem_state_wrong_icon);
                    stateTextView.setText(resources.getString(R.string.problem_state_wrong));
                    break;

                case UNTRIED:
                    stateIconView.setImageResource(R.drawable.problem_state_untried_icon);
                    stateTextView.setText(resources.getString(R.string.problem_state_untried));
                    break;
            }

            return view;
        }
    }

    private class ProblemListAsyncLoader extends AsyncTask<Integer, Integer, List<OJProblem>> {

        OJAccount account;

        public void setAccount(OJAccount account) {
            this.account = account;
        }

        @Override
        protected List<OJProblem> doInBackground(Integer... integers) {
            OJAccountOperator accountOperator = new OJAccountOperator(account);
            accountOperator.login();

            OJProblemFetcher problemFetcher = new OJProblemFetcher(account);
            return problemFetcher.fetchProblemList(integers[0]);
        }

        @Override
        protected void onPostExecute(List<OJProblem> result) {
            super.onPostExecute(result);

            if (result == null) {
                fetchState = MainActivity.FetchState.Failed;
            } else {
                fetchState = MainActivity.FetchState.Successful;
            }

            if (fetchState == MainActivity.FetchState.Successful) {
                problemList.clear();
                problemList.addAll(result);
            }
            problemListAdapter.notifyDataSetChanged();
        }
    }
}
