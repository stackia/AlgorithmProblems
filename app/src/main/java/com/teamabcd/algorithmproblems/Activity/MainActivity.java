package com.teamabcd.algorithmproblems.Activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.teamabcd.algorithmproblems.CustomBackStackController.FragmentBackStackManager;
import com.teamabcd.algorithmproblems.R;
import com.teamabcd.algorithmproblems.TabController.MyAnswerFragment;
import com.teamabcd.algorithmproblems.TabController.ProblemArchiveFragment;
import com.teamabcd.module.ojclient.OJAccount;
import com.teamabcd.module.ojclient.OJAccountOperator;
import com.teamabcd.module.ojclient.OJClientHolder;
import com.teamabcd.module.ojclient.OJProblemFetcher;
import com.teamabcd.module.ojclient.OJSolutionSubmitter;

public class MainActivity extends Activity implements FragmentBackStackManager.NavigationBarHandler, OJClientHolder {

    public final static String CURRENT_TAB_TAG = "CURRENT_TAB";
    public final static int REQUEST_CODE_OJ_ACCOUNT = 1;
    public final static String INTENT_EXTRA_REQUESTER_FRAGMENT_ID = "REQUESTER_FRAGMENT_ID";

    private TabEnum currentTab = TabEnum.Undefined;
    private ProblemArchiveFragment problemArchiveFragment = null;
    private MyAnswerFragment myAnswerFragment = null;
    private FragmentBackStackManager currentBackStack = null;
    private boolean doubleBackToExitPressedOnce = false;

    private OJAccount account = null;
    private OJAccountOperator accountOperator = null;
    private OJProblemFetcher problemFetcher = null;
    private OJSolutionSubmitter solutionSubmitter = null;

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

    @Override
    public void onBackPressed() {
        if (currentBackStack != null && currentBackStack.isAnimating()) {
            return;
        }

        if (currentBackStack != null && currentBackStack.isPoppable()) {
            currentBackStack.popFragment();
            return;
        }

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
        }

        doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.press_back_twice_to_exit, Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_OJ_ACCOUNT:
                if (resultCode == LoginActivity.RESULT_CODE_LOGIN_SUCCESSFUL) {
                    int fragmentId = data.getIntExtra(INTENT_EXTRA_REQUESTER_FRAGMENT_ID, -1);
                    if (fragmentId != -1) {
                        try {
                            LoginActivity.OnLoginListener onLoginListener = (LoginActivity.OnLoginListener) getFragmentManager().findFragmentById(fragmentId);
                            onLoginListener.onLogin();
                        } catch (ClassCastException ignored) {
                        }
                    }
                }
                break;
        }
    }

    public void startLoginActivity(int requesterFragmentId) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(INTENT_EXTRA_REQUESTER_FRAGMENT_ID, requesterFragmentId);
        LoginActivity.mainActivity = this;
        startActivityForResult(intent, REQUEST_CODE_OJ_ACCOUNT);
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

    public void onBackButtonClicked(View view) {
        onBackPressed();
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
    public void setBackButtonEnabled(boolean enabled, boolean animated) {
        ImageButton backButton = (ImageButton) findViewById(R.id.navigationBarBackButton);
        if (enabled) {
            if (animated) {
                Animation animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);
                backButton.setAnimation(animFadeIn);
            }
            backButton.setVisibility(View.VISIBLE);
        } else {
            if (animated) {
                Animation animFadeOut = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out);
                backButton.setAnimation(animFadeOut);
            }
            backButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void setNavigationBarTitle(int resId, boolean animated) {
        setNavigationBarTitle(getResources().getString(resId), animated);
    }

    @Override
    public String getNavigationBarTitle() {
        TextView titleView = (TextView) findViewById(R.id.navigationBarTitle);
        return titleView.getText().toString();
    }

    @Override
    public void setNavigationBarTitle(final String title, boolean animated) {
        final TextView titleView = (TextView) findViewById(R.id.navigationBarTitle);
        if (animated) {
            AlphaAnimation out = new AlphaAnimation(1.0f, 0.0f);
            out.setDuration(150);
            out.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    titleView.setText(title);
                    AlphaAnimation in = new AlphaAnimation(0.0f, 1.0f);
                    in.setDuration(150);
                    titleView.startAnimation(in);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            titleView.startAnimation(out);
        } else {
            titleView.setText(title);
        }
    }

    @Override
    public FragmentBackStackManager getCurrentBackStack() {
        return currentBackStack;
    }

    @Override
    public void setCurrentBackStack(FragmentBackStackManager backStack) {
        currentBackStack = backStack;
    }

    @Override
    public OJAccount getAccount() {
        if (account == null) {
            account = new OJAccount(OJAccount.Type.HDU);
        }
        return account;
    }

    @Override
    public void setAccount(OJAccount account) {
        this.account = account;
        getAccountOperator().setAccount(account);
        getProblemFetcher().setAccount(account);
        getSolutionSubmitter().setAccount(account);
    }

    @Override
    public OJAccountOperator getAccountOperator() {
        if (accountOperator == null) {
            accountOperator = new OJAccountOperator(getAccount());
        }
        return accountOperator;
    }

    @Override
    public OJProblemFetcher getProblemFetcher() {
        if (problemFetcher == null) {
            problemFetcher = new OJProblemFetcher(getAccount());
        }
        return problemFetcher;
    }

    @Override
    public OJSolutionSubmitter getSolutionSubmitter() {
        if (solutionSubmitter == null) {
            solutionSubmitter = new OJSolutionSubmitter(getAccount());
        }
        return solutionSubmitter;
    }

    enum TabEnum {
        Undefined,
        ProblemArchive,
        MyAnswer,
    }

    public enum FetchState {
        Working,
        Failed,
        Successful,
    }
}
