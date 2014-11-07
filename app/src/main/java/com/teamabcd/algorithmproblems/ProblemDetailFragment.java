package com.teamabcd.algorithmproblems;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.teamabcd.module.ojclient.OJAccount;
import com.teamabcd.module.ojclient.OJAccountOperator;
import com.teamabcd.module.ojclient.OJProblem;
import com.teamabcd.module.ojclient.OJProblemFetcher;

import java.util.List;
import java.util.ServiceConfigurationError;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;

public class ProblemDetailFragment extends SlidingFragment implements View.OnClickListener {

    private OJProblem problem;
    private MainActivity.FetchState fetchState = MainActivity.FetchState.Working;

    public static ProblemDetailFragment newInstance(OJProblem problem) {
        ProblemDetailFragment fragment = new ProblemDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(OJProblem.tag, problem);
        fragment.setArguments(args);
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public int getNavigationBarTitleResource() {
        return R.string.navigation_bar_title_problem_detail;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            problem = (OJProblem)args.get(OJProblem.tag);
        }
        if (problem.getOutputDescription() == null) {
            // TODO: Move account out here
            ProblemDetailAsyncLoader loader = new ProblemDetailAsyncLoader();
            loader.setAccount(new OJAccount(OJAccount.Type.HDU, "jsq2627", "jsq2627_kz"));
            loader.execute(problem);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_problem_detail, container, false);
        TextView fetchErrorTextView = (TextView) view.findViewById(R.id.fetchErrorTextView);
        fetchErrorTextView.setOnClickListener(this);
        notifyProblemDetailChange(view);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fetchErrorTextView:
                // TODO: Move account out here
                ProblemDetailAsyncLoader loader = new ProblemDetailAsyncLoader();
                loader.setAccount(new OJAccount(OJAccount.Type.HDU, "jsq2627", "jsq2627_kz"));
                loader.execute(problem);

                fetchState = MainActivity.FetchState.Working;
                notifyProblemDetailChange();

                break;
        }
    }

    private void notifyProblemDetailChange() {
        notifyProblemDetailChange(getView());
    }

    private void notifyProblemDetailChange(View view) {
        CircularProgressBar loadingProgressBar = (CircularProgressBar) view.findViewById(R.id.loadingProgressBar);
        TextView fetchErrorTextView = (TextView) view.findViewById(R.id.fetchErrorTextView);

        ScrollView problemDetailScrollView = (ScrollView) view.findViewById(R.id.problemDetailScrollView);

        LinearLayout problemDetailDescriptionLayout = (LinearLayout) view.findViewById(R.id.problemDetailDescriptionLayout);
        TextView problemDetailDescriptionTextView = (TextView) view.findViewById(R.id.problemDetailDescriptionTextView);

        LinearLayout problemDetailInputLayout = (LinearLayout) view.findViewById(R.id.problemDetailInputLayout);
        TextView problemDetailInputTextView = (TextView) view.findViewById(R.id.problemDetailInputTextView);

        TextView problemDetailOutputTextView = (TextView) view.findViewById(R.id.problemDetailOutputTextView);

        LinearLayout problemDetailSampleInputLayout = (LinearLayout) view.findViewById(R.id.problemDetailSampleInputLayout);
        TextView problemDetailSampleInputTextView = (TextView) view.findViewById(R.id.problemDetailSampleInputTextView);

        TextView problemDetailSampleOutputTextView = (TextView) view.findViewById(R.id.problemDetailSampleOutputTextView);

        switch (fetchState) {
            case Working:
                loadingProgressBar.setVisibility(View.VISIBLE);
                problemDetailScrollView.setVisibility(View.GONE);
                break;

            case Failed:
                loadingProgressBar.setVisibility(View.GONE);
                problemDetailScrollView.setVisibility(View.GONE);
                fetchErrorTextView.setVisibility(View.VISIBLE);
                break;

            case Successful:
                loadingProgressBar.setVisibility(View.GONE);
                problemDetailScrollView.setVisibility(View.VISIBLE);

                if (problem.getDescription() != null) {
                    problemDetailDescriptionLayout.setVisibility(View.VISIBLE);
                    problemDetailDescriptionTextView.setText(HtmlUtils.fromHtmlTrimTrailingLineBreak(problem.getDescription()));
                } else {
                    problemDetailDescriptionLayout.setVisibility(View.GONE);
                }

                if (problem.getInputDescription() != null) {
                    problemDetailInputLayout.setVisibility(View.VISIBLE);
                    problemDetailInputTextView.setText(HtmlUtils.fromHtmlTrimTrailingLineBreak(problem.getInputDescription()));
                } else {
                    problemDetailInputLayout.setVisibility(View.GONE);
                }

                problemDetailOutputTextView.setText(HtmlUtils.fromHtmlTrimTrailingLineBreak(problem.getOutputDescription()));

                if (problem.getInputSample() != null) {
                    problemDetailSampleInputLayout.setVisibility(View.VISIBLE);

                    problemDetailSampleInputTextView.setText(problem.getInputSample());
                } else {

                    problemDetailSampleInputLayout.setVisibility(View.GONE);
                }

                problemDetailSampleOutputTextView.setText(problem.getOutputSample());
                break;
        }
    }

    private class ProblemDetailAsyncLoader extends AsyncTask<OJProblem, Integer, OJProblem> {

        OJAccount account;

        public void setAccount(OJAccount account) {
            this.account = account;
        }

        @Override
        protected OJProblem doInBackground(OJProblem... ojProblems) {
            OJProblemFetcher problemFetcher = new OJProblemFetcher(account);
            if (problemFetcher.fillProblem(ojProblems[0])) {
                fetchState = MainActivity.FetchState.Successful;
            } else {
                fetchState = MainActivity.FetchState.Failed;
            }
            return ojProblems[0];
        }

        @Override
        protected void onPostExecute(OJProblem problem) {
            super.onPostExecute(problem);
            if (fetchState == MainActivity.FetchState.Successful) {
                ProblemDetailFragment.this.problem = problem;
            }
            notifyProblemDetailChange();
        }
    }
}
