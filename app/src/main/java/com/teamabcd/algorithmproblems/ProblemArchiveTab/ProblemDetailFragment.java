package com.teamabcd.algorithmproblems.ProblemArchiveTab;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.teamabcd.algorithmproblems.Activity.MainActivity;
import com.teamabcd.algorithmproblems.CustomBackStackController.FragmentBackStackManager;
import com.teamabcd.algorithmproblems.CustomView.SlidingFragment;
import com.teamabcd.algorithmproblems.R;
import com.teamabcd.algorithmproblems.Utils.HtmlUtils;
import com.teamabcd.module.ojclient.OJClientHolder;
import com.teamabcd.module.ojclient.OJProblem;
import com.teamabcd.module.ojclient.OJSolution;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;

/**
 * Project: Algorithm Problems
 * Created by: Stackia <jsq2627@gmail.com>
 * Date: 11/5/14
 */
public class ProblemDetailFragment extends SlidingFragment implements View.OnClickListener, View.OnTouchListener, AdapterView.OnItemSelectedListener {

    ProgramLanguageAdapter programLanguageAdapter;
    OJClientHolder ojClientHolder;
    // These variables are made global for performance consideration. Used in OnTouch listener.
    RelativeLayout codeAreaLayout;
    float codeAreaMeasureBaseTerm;
    ViewGroup.LayoutParams codeAreaLayoutParams;
    int codeAreaAdjustLineHeight;
    int problemDetailLayoutHeight;
    private OJProblem problem;
    private MainActivity.FetchState fetchState = MainActivity.FetchState.Working;
    private OJSolution.LanguageType solutionLanguage = OJSolution.LanguageType.ANY;

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
    public FragmentBackStackManager.NavigationBarHandler.NavigationBarActionButton getNavigationBarActionButton() {
        FragmentBackStackManager.NavigationBarHandler.NavigationBarActionButton navigationBarActionButton = new FragmentBackStackManager.NavigationBarHandler.NavigationBarActionButton();
        navigationBarActionButton.text = getString(R.string.navigation_bar_action_button_text_submit);
        navigationBarActionButton.onClickListener = this;
        return navigationBarActionButton;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            problem = (OJProblem) args.get(OJProblem.tag);
        }
        if (problem.getOutputDescription() == null) {
            startLoadDetailTask();
        } else {
            fetchState = MainActivity.FetchState.Successful;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_problem_detail, container, false);

        TextView fetchErrorTextView = (TextView) view.findViewById(R.id.fetchErrorTextView);
        fetchErrorTextView.setOnClickListener(this);
        notifyProblemDetailChange(view);

        FrameLayout codeAreaAdjustLineTouchArea = (FrameLayout) view.findViewById(R.id.problemCodeAreaAdjustLineTouchArea);
        codeAreaAdjustLineTouchArea.setOnTouchListener(this);

        Typeface typefaceSourceCodeProRegular = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SourceCodePro-Regular.ttf");
        EditText codeAreaMainEditText = (EditText) view.findViewById(R.id.problemCodeAreaMainEditText);
        codeAreaMainEditText.setTypeface(typefaceSourceCodeProRegular);

        TextView problemDetailSampleInputTextView = (TextView) view.findViewById(R.id.problemDetailSampleInputTextView);
        TextView problemDetailSampleOutputTextView = (TextView) view.findViewById(R.id.problemDetailSampleOutputTextView);
        problemDetailSampleInputTextView.setTypeface(typefaceSourceCodeProRegular);
        problemDetailSampleOutputTextView.setTypeface(typefaceSourceCodeProRegular);

        Spinner codeAreaLanguageSpinner = (Spinner) view.findViewById(R.id.problemCodeAreaLanguageSpanner);
        programLanguageAdapter = new ProgramLanguageAdapter(getActivity());
        codeAreaLanguageSpinner.setAdapter(programLanguageAdapter);
        codeAreaLanguageSpinner.setOnItemSelectedListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fetchErrorTextView:
                startLoadDetailTask();
                break;

            case R.id.navigationBarActionButton:
                try {
                    OJClientHolder ojClientHolder = (OJClientHolder) getActivity();
                    if (ojClientHolder.getAccount().isAnonymous()) {
                        Toast.makeText(getActivity(), R.string.problem_submit_failed_login_first, Toast.LENGTH_SHORT).show();
                    } else {
                        startSolutionSubmitTask();
                    }
                } catch (ClassCastException e) {
                    throw new ClassCastException("Activity must implement OJClientHolder");
                }
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        solutionLanguage = programLanguageAdapter.getItem(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.problemCodeAreaAdjustLineTouchArea) {
            if (event.getAction() == MotionEvent.ACTION_MOVE) {

                if (codeAreaLayout == null) {
                    View view = getView();
                    if (view != null) {
                        codeAreaLayout = (RelativeLayout) view.findViewById(R.id.codeAreaLayout);
                        RelativeLayout codeAreaAdjustLine = (RelativeLayout) view.findViewById(R.id.problemCodeAreaAdjustLine);
                        RelativeLayout problemDetailLayout = (RelativeLayout) view.findViewById(R.id.problemDetailLayout);
                        int[] problemDetailLayoutLocation = new int[2];
                        problemDetailLayout.getLocationOnScreen(problemDetailLayoutLocation);
                        codeAreaAdjustLineHeight = codeAreaAdjustLine.getHeight();
                        codeAreaMeasureBaseTerm = problemDetailLayoutLocation[1] + problemDetailLayout.getHeight() + codeAreaAdjustLineHeight / 2.0f;
                        problemDetailLayoutHeight = problemDetailLayout.getHeight();
                    } else {
                        return true;
                    }
                }

                codeAreaLayoutParams = codeAreaLayout.getLayoutParams();
                codeAreaLayoutParams.height = (int) (codeAreaMeasureBaseTerm - event.getRawY());
                if (codeAreaLayoutParams.height < codeAreaAdjustLineHeight) {
                    codeAreaLayoutParams.height = codeAreaAdjustLineHeight;
                } else if (codeAreaLayoutParams.height > problemDetailLayoutHeight) {
                    codeAreaLayoutParams.height = problemDetailLayoutHeight;
                }
                codeAreaLayout.setLayoutParams(codeAreaLayoutParams);
            }
            return true;
        }
        return false;
    }

    private void startLoadDetailTask() {
        ProblemDetailAsyncLoader loader = new ProblemDetailAsyncLoader();
        loader.execute(problem);
    }

    private void startSolutionSubmitTask() {
        View view = getView();
        if (view != null) {
            EditText codeAreaMainEditText = (EditText) view.findViewById(R.id.problemCodeAreaMainEditText);
            String code = codeAreaMainEditText.getText().toString();
            if (code == null || code.isEmpty()) {
                Toast.makeText(getActivity(), R.string.problem_submit_failed_empty_code_body, Toast.LENGTH_SHORT).show();
                return;
            }
            OJSolution solution = new OJSolution(problem.getId(), codeAreaMainEditText.getText().toString(), solutionLanguage);
            SolutionSubmitAsyncTask task = new SolutionSubmitAsyncTask();
            task.execute(solution);
        }
    }

    private void notifyProblemDetailChange() {
        notifyProblemDetailChange(getView());
    }

    private void notifyProblemDetailChange(View view) {
        if (view == null) {
            return;
        }

        CircularProgressBar loadingProgressBar = (CircularProgressBar) view.findViewById(R.id.loadingProgressBar);
        TextView fetchErrorTextView = (TextView) view.findViewById(R.id.fetchErrorTextView);

        RelativeLayout problemDetailLayout = (RelativeLayout) view.findViewById(R.id.problemDetailLayout);

        LinearLayout problemDetailDescriptionLayout = (LinearLayout) view.findViewById(R.id.problemDetailDescriptionLayout);
        TextView problemDetailDescriptionTextView = (TextView) view.findViewById(R.id.problemDetailDescriptionTextView);

        LinearLayout problemDetailInputLayout = (LinearLayout) view.findViewById(R.id.problemDetailInputLayout);
        TextView problemDetailInputTextView = (TextView) view.findViewById(R.id.problemDetailInputTextView);

        LinearLayout problemDetailOutputLayout = (LinearLayout) view.findViewById(R.id.problemDetailOutputLayout);
        TextView problemDetailOutputTextView = (TextView) view.findViewById(R.id.problemDetailOutputTextView);

        LinearLayout problemDetailSampleInputLayout = (LinearLayout) view.findViewById(R.id.problemDetailSampleInputLayout);
        TextView problemDetailSampleInputTextView = (TextView) view.findViewById(R.id.problemDetailSampleInputTextView);

        TextView problemDetailSampleOutputTextView = (TextView) view.findViewById(R.id.problemDetailSampleOutputTextView);

        switch (fetchState) {
            case Working:
                loadingProgressBar.setVisibility(View.VISIBLE);
                problemDetailLayout.setVisibility(View.GONE);
                break;

            case Failed:
                loadingProgressBar.setVisibility(View.GONE);
                problemDetailLayout.setVisibility(View.GONE);
                fetchErrorTextView.setVisibility(View.VISIBLE);
                break;

            case Successful:
                loadingProgressBar.setVisibility(View.GONE);
                problemDetailLayout.setVisibility(View.VISIBLE);

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

                if (problem.getOutputDescription() != null) {
                    problemDetailOutputLayout.setVisibility(View.VISIBLE);
                    problemDetailOutputTextView.setText(HtmlUtils.fromHtmlTrimTrailingLineBreak(problem.getOutputDescription()));
                } else {
                    problemDetailOutputLayout.setVisibility(View.GONE);
                }

                if (problem.getInputSample() != null) {
                    problemDetailSampleInputLayout.setVisibility(View.VISIBLE);

                    problemDetailSampleInputTextView.setText(Html.fromHtml(problem.getInputSample().replaceAll("\\n", "<br>")));
                } else {
                    problemDetailSampleInputLayout.setVisibility(View.GONE);
                }

                problemDetailSampleOutputTextView.setText(Html.fromHtml(problem.getOutputSample().replaceAll("\\n", "<br>")));
                break;
        }
    }

    private class ProgramLanguageAdapter extends ArrayAdapter<OJSolution.LanguageType> {

        private ProgramLanguageAdapter(Context context) {
            super(context, R.layout.program_language_spinner_item, OJSolution.getSupportedLanguageList());
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = (TextView) super.getView(position, convertView, parent);
            textView.setTextColor(Color.WHITE);
            textView.setPadding(0, 0, 0, 0);
            textView.setGravity(Gravity.CENTER);
            return textView;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
            textView.setText(getItem(position).getLiteralFullName());
            return textView;
        }
    }

    private class ProblemDetailAsyncLoader extends AsyncTask<OJProblem, Integer, OJProblem> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            fetchState = MainActivity.FetchState.Working;
            notifyProblemDetailChange();
        }

        @Override
        protected OJProblem doInBackground(OJProblem... ojProblems) {
            try {
                OJClientHolder ojClientHolder = (OJClientHolder) getActivity();
                if (ojClientHolder.getProblemFetcher().fillProblem(ojProblems[0])) {
                    fetchState = MainActivity.FetchState.Successful;
                } else {
                    fetchState = MainActivity.FetchState.Failed;
                }
                return ojProblems[0];
            } catch (ClassCastException e) {
                throw new ClassCastException("Activity must implement OJClientHolder");
            }
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

    private class SolutionSubmitAsyncTask extends AsyncTask<OJSolution, Integer, OJSolution> {

        private Toast submitProgressToast = Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            submitProgressToast.setText(R.string.problem_submit_working);
            submitProgressToast.show();
        }

        @Override
        protected OJSolution doInBackground(OJSolution... ojSolutions) {
            try {
                OJClientHolder ojClientHolder = (OJClientHolder) getActivity();
                if (ojClientHolder.getSolutionSubmitter().submit(ojSolutions[0])) {
                    return ojSolutions[0];
                } else {
                    return null;
                }
            } catch (ClassCastException e) {
                throw new ClassCastException("Activity must implement OJClientHolder");
            }
        }

        @Override
        protected void onPostExecute(OJSolution ojSolution) {
            super.onPostExecute(ojSolution);
            if (ojSolution == null) {
                submitProgressToast.setText(R.string.problem_submit_failed_unknown_reason);
            } else {
                submitProgressToast.setText(R.string.problem_submit_successful);
            }
            submitProgressToast.show();

        }
    }
}
