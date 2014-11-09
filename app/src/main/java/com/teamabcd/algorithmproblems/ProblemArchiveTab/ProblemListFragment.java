package com.teamabcd.algorithmproblems.ProblemArchiveTab;

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
import android.widget.Toast;

import com.teamabcd.algorithmproblems.Activity.MainActivity;
import com.teamabcd.algorithmproblems.CustomBackStackController.FragmentBackStackManager;
import com.teamabcd.algorithmproblems.CustomView.LoadMoreListView;
import com.teamabcd.algorithmproblems.CustomView.SlidingFragment;
import com.teamabcd.algorithmproblems.R;
import com.teamabcd.module.ojclient.OJClientHolder;
import com.teamabcd.module.ojclient.OJProblem;

import java.util.ArrayList;
import java.util.List;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;

/**
 * Project: Algorithm Problems
 * Created by: Stackia <jsq2627@gmail.com>
 * Date: 11/4/14
 */
public class ProblemListFragment extends SlidingFragment implements ListView.OnItemClickListener, View.OnClickListener, LoadMoreListView.OnLoadMoreListener {

    private List<OJProblem> problemList = new ArrayList<OJProblem>();
    private ProblemListAdapter problemListAdapter;
    private MainActivity.FetchState fetchState = MainActivity.FetchState.Working;
    private int currentLoadedPage = 0;

    public static ProblemListFragment newInstance() {
        ProblemListFragment fragment = new ProblemListFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        problemListAdapter = new ProblemListAdapter(getActivity(), problemList);
        startLoadPageTask(currentLoadedPage + 1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_problem_list, container, false);

        TextView fetchErrorTextView = (TextView) view.findViewById(R.id.fetchErrorTextView);
        fetchErrorTextView.setOnClickListener(this);

        LoadMoreListView problemListView = (LoadMoreListView) view.findViewById(R.id.problemListView);
        problemListView.setOnLoadMoreListener(this);
        problemListView.setOnItemClickListener(this);
        problemListView.setAdapter(problemListAdapter);

        problemListAdapter.notifyDataSetChanged(view);

        return view;
    }

    @Override
    public int getNavigationBarTitleResource() {
        return R.string.navigation_bar_title_problem_list;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        FragmentBackStackManager backStackManager = getNavigationBarHandler().getCurrentBackStack();
        if (!backStackManager.isAnimating()) {
            backStackManager.pushFragment(ProblemDetailFragment.newInstance(problemList.get(position)));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fetchErrorTextView:
                currentLoadedPage = 0;
                startLoadPageTask(currentLoadedPage + 1);
                break;
        }
    }

    @Override
    public void onLoadMore() {
        startLoadPageTask(currentLoadedPage + 1);
    }

    private void startLoadPageTask(int page) {
        ProblemListAsyncLoader loader = new ProblemListAsyncLoader();
        loader.execute(page);
    }

    private class ProblemListAdapter extends ArrayAdapter<OJProblem> {
        private ProblemListAdapter(Context context, List<OJProblem> problemList) {
            super(context, 0, problemList);
        }

        public void notifyDataSetChanged(View view) {
            CircularProgressBar loadingProgressBar = (CircularProgressBar) view.findViewById(R.id.loadingProgressBar);
            TextView fetchErrorTextView = (TextView) view.findViewById(R.id.fetchErrorTextView);
            LoadMoreListView problemListView = (LoadMoreListView) view.findViewById(R.id.problemListView);

            switch (fetchState) {
                case Working:
                    if (currentLoadedPage == 0) {
                        loadingProgressBar.setVisibility(View.VISIBLE);
                        fetchErrorTextView.setVisibility(View.GONE);
                        problemListView.setVisibility(View.GONE);
                    } else {
                        problemListView.forceLoadingViewVisible();
                    }
                    break;

                case Failed:
                    if (currentLoadedPage == 0) {
                        loadingProgressBar.setVisibility(View.GONE);
                        fetchErrorTextView.setVisibility(View.VISIBLE);
                        problemListView.setVisibility(View.GONE);
                    } else {
                        problemListView.notifyLoadMoreFinished();
                    }
                    break;

                case Successful:
                    if (currentLoadedPage == 1) {
                        loadingProgressBar.setVisibility(View.GONE);
                        fetchErrorTextView.setVisibility(View.GONE);
                        problemListView.setVisibility(View.VISIBLE);
                        super.notifyDataSetChanged();
                    } else {
                        problemListView.notifyLoadMoreFinished();
                        super.notifyDataSetChanged();
                    }
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

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            fetchState = MainActivity.FetchState.Working;
            problemListAdapter.notifyDataSetChanged();
        }

        @Override
        protected List<OJProblem> doInBackground(Integer... integers) {
            try {
                OJClientHolder ojClientHolder = (OJClientHolder) getActivity();
                return ojClientHolder.getProblemFetcher().fetchProblemList(integers[0]);
            } catch (ClassCastException e) {
                throw new ClassCastException("Activity must implement OJClientHolder");
            }
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
                ++currentLoadedPage;
                problemList.addAll(result);
            } else if (currentLoadedPage > 0) {
                Toast.makeText(getActivity(), R.string.network_operation_timeout_pull_down, Toast.LENGTH_SHORT).show();
            }
            problemListAdapter.notifyDataSetChanged();
        }
    }
}
