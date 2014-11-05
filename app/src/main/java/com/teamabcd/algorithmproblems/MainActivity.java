package com.teamabcd.algorithmproblems;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


public class MainActivity extends Activity implements NavigationBarHandler {

    public final static String CURRENT_TAB_TAG = "CURRENT_TAB";

    private TabEnum currentTab = TabEnum.Undefined;
    private ProblemArchiveFragment problemArchiveFragment = null;
    private MyAnswerFragment myAnswerFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            setCurrentTab(TabEnum.ProblemArchive); // Default tab selection
        } else {
            FragmentManager fragmentManager = getFragmentManager();
            problemArchiveFragment = (ProblemArchiveFragment) fragmentManager.findFragmentByTag(ProblemArchiveFragment.tag);
            myAnswerFragment = (MyAnswerFragment) fragmentManager.findFragmentByTag(MyAnswerFragment.tag);
            setCurrentTab((TabEnum) savedInstanceState.get(CURRENT_TAB_TAG)); // Restore tab selection
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(CURRENT_TAB_TAG, currentTab);
    }

    private void setSelectedTabButton(TabEnum tab) {
        TextView tabButtonProblemArchive = (TextView) findViewById(R.id.tabBarProblemArchiveButton);
        if (tab == TabEnum.ProblemArchive) {
            tabButtonProblemArchive.setTextColor(getResources().getColor(R.color.tab_bar_button_text_highlighted));
            tabButtonProblemArchive.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.tab_bar_button_problem_archive_highlight), null, null);
        } else {
            tabButtonProblemArchive.setTextColor(getResources().getColor(R.color.tab_bar_button_text_normal));
            tabButtonProblemArchive.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.tab_bar_button_problem_archive), null, null);
        }

        TextView tabButtonMyAnswer = (TextView) findViewById(R.id.tabBarMyAnswersButton);
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
                    transaction.add(R.id.tabFrameContainer, problemArchiveFragment, ProblemArchiveFragment.tag);
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
                    transaction.add(R.id.tabFrameContainer, myAnswerFragment, MyAnswerFragment.tag);
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

    public void onProblemArchiveTabButtonClicked(View view) {
        setCurrentTab(TabEnum.ProblemArchive);
    }

    public void onMyAnswerTabButtonClicked(View view) {
        setCurrentTab(TabEnum.MyAnswer);
    }

    @Override
    public boolean isBackButtonEnabled() {
        ImageButton backButton = (ImageButton) findViewById(R.id.navigationBarBackButton);
        if (backButton.getVisibility() == View.VISIBLE) {
            return true;
        }
        return false;
    }

    @Override
    public void setBackButtonEnabled(boolean enabled) {
        ImageButton backButton = (ImageButton) findViewById(R.id.navigationBarBackButton);
        if (enabled) {
            backButton.setVisibility(View.VISIBLE);
        } else {
            backButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void setNavigationBarTitle(int resId) {
        TextView titleView = (TextView) findViewById(R.id.navigationBarTitle);
        titleView.setText(resId);
    }

    @Override
    public String getNavigationBarTitle() {
        TextView titleView = (TextView) findViewById(R.id.navigationBarTitle);
        return titleView.getText().toString();
    }

    @Override
    public void setNavigationBarTitle(String title) {
        TextView titleView = (TextView) findViewById(R.id.navigationBarTitle);
        titleView.setText(title);
    }

    enum TabEnum {
        Undefined,
        ProblemArchive,
        MyAnswer,
    }
}
