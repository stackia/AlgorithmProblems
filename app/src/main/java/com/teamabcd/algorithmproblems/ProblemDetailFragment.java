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

public class ProblemDetailFragment extends SlidingFragment {

    private OJProblem problem;
    private View view;

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
        // TODO: Move loader out here
        ProblemDetailAsyncLoader loader = new ProblemDetailAsyncLoader();
        loader.setAccount(new OJAccount(OJAccount.Type.HDU, "jsq2627", "jsq2627_kz"));
        loader.execute(problem);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_problem_detail, container, false);
        if (problem.getOutputSample() == null || this.view == null) {
            CircularProgressBar loadingProgressBar = (CircularProgressBar) view.findViewById(R.id.loadingProgressBar);
            loadingProgressBar.setVisibility(View.VISIBLE);
            LinearLayout problemDetailLayout = (LinearLayout) view.findViewById(R.id.problemDetailLayout);
            problemDetailLayout.setVisibility(View.GONE);
            this.view = view;
        } else {
            this.view = view;
            setupProblem(problem);
        }
        return view;
    }

    private void setupProblem(OJProblem problem) {
        try {
            if (problem == null) {
                throw new Exception();
            }
            if (view != null) {
                CircularProgressBar loadingProgressBar = (CircularProgressBar)view.findViewById(R.id.loadingProgressBar);
                loadingProgressBar.setVisibility(View.GONE);
                LinearLayout problemDetailLayout = (LinearLayout)view.findViewById(R.id.problemDetailLayout);
                problemDetailLayout.setVisibility(View.VISIBLE);

                if (problem.getDescription() != null) {
                    LinearLayout problemDetailDescriptionLayout = (LinearLayout)view.findViewById(R.id.problemDetailDescriptionLayout);
                    problemDetailDescriptionLayout.setVisibility(View.VISIBLE);

                    TextView problemDetailDescriptionTextView = (TextView) view.findViewById(R.id.problemDetailDescriptionTextView);
                    problemDetailDescriptionTextView.setText(HtmlUtils.fromHtmlTrimTrailingLineBreak(problem.getDescription()));
                } else {
                    LinearLayout problemDetailDescriptionLayout = (LinearLayout)view.findViewById(R.id.problemDetailDescriptionLayout);
                    problemDetailDescriptionLayout.setVisibility(View.GONE);
                }

                if (problem.getInputDescription() != null) {
                    LinearLayout problemDetailInputLayout = (LinearLayout)view.findViewById(R.id.problemDetailInputLayout);
                    problemDetailInputLayout.setVisibility(View.VISIBLE);

                    TextView problemDetailInputTextView = (TextView) view.findViewById(R.id.problemDetailInputTextView);
                    problemDetailInputTextView.setText(HtmlUtils.fromHtmlTrimTrailingLineBreak(problem.getInputDescription()));
                } else {
                    LinearLayout problemDetailInputLayout = (LinearLayout)view.findViewById(R.id.problemDetailInputLayout);
                    problemDetailInputLayout.setVisibility(View.GONE);
                }

                TextView problemDetailOutputTextView = (TextView)view.findViewById(R.id.problemDetailOutputTextView);
                problemDetailOutputTextView.setText(HtmlUtils.fromHtmlTrimTrailingLineBreak(problem.getOutputDescription()));

                if (problem.getInputSample() != null) {
                    LinearLayout problemDetailSampleInputLayout = (LinearLayout)view.findViewById(R.id.problemDetailSampleInputLayout);
                    problemDetailSampleInputLayout.setVisibility(View.VISIBLE);

                    TextView problemDetailSampleInputTextView = (TextView) view.findViewById(R.id.problemDetailSampleInputTextView);
                    problemDetailSampleInputTextView.setText(problem.getInputSample());
                } else {
                    LinearLayout problemDetailSampleInputLayout = (LinearLayout)view.findViewById(R.id.problemDetailSampleInputLayout);
                    problemDetailSampleInputLayout.setVisibility(View.GONE);
                }

                TextView problemDetailSampleOutputTextView = (TextView)view.findViewById(R.id.problemDetailSampleOutputTextView);
                problemDetailSampleOutputTextView.setText(problem.getOutputSample());
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), R.string.network_operation_timeout, Toast.LENGTH_SHORT).show();

            if (view != null) {
                CircularProgressBar loadingProgressBar = (CircularProgressBar) view.findViewById(R.id.loadingProgressBar);
                loadingProgressBar.setVisibility(View.GONE);
                LinearLayout problemDetailLayout = (LinearLayout) view.findViewById(R.id.problemDetailLayout);
                problemDetailLayout.setVisibility(View.GONE);
            }
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
                problem = ojProblems[0];
                return problem;
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(OJProblem problem) {
            super.onPostExecute(problem);
            setupProblem(problem);
        }
    }
}
