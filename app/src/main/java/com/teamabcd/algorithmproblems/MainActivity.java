package com.teamabcd.algorithmproblems;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.drm.DrmStore;
import android.os.Bundle;
import android.text.StaticLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;


public class MainActivity extends Activity {

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
                if (problemArchiveFragment == null) {
                    problemArchiveFragment = new ProblemArchiveFragment();
                }
                transaction.replace(R.id.tabFrameContainer, problemArchiveFragment);
                break;

            case MyAnswer:
                if (myAnswerFragment == null) {
                    myAnswerFragment = new MyAnswerFragment();
                }
                transaction.replace(R.id.tabFrameContainer, myAnswerFragment);
                break;

            case Undefined:
                throw new UnsupportedOperationException("Undefined tab.");
        }
        transaction.commit();

        currentTab = tab;
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
}
