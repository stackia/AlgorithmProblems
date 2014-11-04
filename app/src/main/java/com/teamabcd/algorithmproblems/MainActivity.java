package com.teamabcd.algorithmproblems;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.appwidget.AppWidgetHostView;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


public class MainActivity extends Activity implements NavigationBarHandler {

    private TabEnum currentTab = TabEnum.Undefined;
    private ProblemArchiveFragment problemArchiveFragment = null;
    private MyAnswerFragment myAnswerFragment = null;

    enum TabEnum {
        Undefined,
        ProblemArchive,
        MyAnswer,
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setCurrentTab(TabEnum.ProblemArchive); // Default tab
    }

    public void setCurrentTab(TabEnum tab) {
        if (currentTab == tab) {
            return;
        }

        // Switch fragment (Lazy load)
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        switch (tab) {
            case ProblemArchive:
                if (myAnswerFragment != null) {
                    transaction.hide(myAnswerFragment);
                }
                if (problemArchiveFragment == null) {
                    problemArchiveFragment = ProblemArchiveFragment.newInstance();
                    transaction.add(R.id.tabFrameContainer, problemArchiveFragment);
                } else {
                    transaction.show(problemArchiveFragment);
                }
                break;

            case MyAnswer:
                if (problemArchiveFragment != null) {
                    transaction.hide(problemArchiveFragment);
                }
                if (myAnswerFragment == null) {
                    myAnswerFragment = MyAnswerFragment.newInstance();
                    transaction.add(R.id.tabFrameContainer, myAnswerFragment);
                } else {
                    transaction.show(myAnswerFragment);
                }
                break;

            case Undefined:
                throw new UnsupportedOperationException("Undefined tab.");
        }
        transaction.commit();

        setSelectedTabButton(tab);
        currentTab = tab;
    }

    private void setSelectedTabButton(TabEnum tab) {
        TextView tabButtonProblemArchive = (TextView)findViewById(R.id.tabBarProblemArchiveButton);
        if (tab == TabEnum.ProblemArchive) {
            tabButtonProblemArchive.setTextColor(getResources().getColor(R.color.tab_bar_button_text_highlighted));
            tabButtonProblemArchive.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.tab_bar_button_problem_archive_highlight), null, null);
        } else {
            tabButtonProblemArchive.setTextColor(getResources().getColor(R.color.tab_bar_button_text_normal));
            tabButtonProblemArchive.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.tab_bar_button_problem_archive), null, null);
        }

        TextView tabButtonMyAnswer = (TextView)findViewById(R.id.tabBarMyAnswersButton);
        if (tab == TabEnum.MyAnswer) {
            tabButtonMyAnswer.setTextColor(getResources().getColor(R.color.tab_bar_button_text_highlighted));
            tabButtonMyAnswer.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.tab_bar_button_my_answers_highlight), null, null);
        } else {
            tabButtonMyAnswer.setTextColor(getResources().getColor(R.color.tab_bar_button_text_normal));
            tabButtonMyAnswer.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.tab_bar_button_my_answers), null, null);
        }
    }

    public TabEnum getCurrentTab() {
        return currentTab;
    }

    public void onProblemArchiveTabButtonClicked(View view) {
        setCurrentTab(TabEnum.ProblemArchive);
    }

    public void onMyAnswerTabButtonClicked(View view) {
        setCurrentTab(TabEnum.MyAnswer);
    }

    @Override
    public void setBackButtonEnabled(boolean enabled) {
        ImageButton backButton = (ImageButton)findViewById(R.id.navigationBarBackButton);
        if (enabled) {
            backButton.setVisibility(View.VISIBLE);
        } else {
            backButton.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean isBackButtonEnabled() {
        ImageButton backButton = (ImageButton)findViewById(R.id.navigationBarBackButton);
        if (backButton.getVisibility() == View.VISIBLE) {
            return true;
        }
        return false;
    }

    @Override
    public void setNavigationBarTitle(int resId) {
        TextView titleView = (TextView)findViewById(R.id.navigationBarTitle);
        titleView.setText(resId);
    }

    @Override
    public void setNavigationBarTitle(String title) {
        TextView titleView = (TextView)findViewById(R.id.navigationBarTitle);
        titleView.setText(title);
    }

    @Override
    public String getNavigationBarTitle() {
        TextView titleView = (TextView)findViewById(R.id.navigationBarTitle);
        return titleView.getText().toString();
    }
}
