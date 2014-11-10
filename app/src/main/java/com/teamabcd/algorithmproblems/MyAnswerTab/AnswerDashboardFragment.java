package com.teamabcd.algorithmproblems.MyAnswerTab;


import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.teamabcd.algorithmproblems.Activity.LoginActivity;
import com.teamabcd.algorithmproblems.Activity.MainActivity;
import com.teamabcd.algorithmproblems.CustomBackStackController.FragmentBackStackManager;
import com.teamabcd.algorithmproblems.CustomView.SlidingFragment;
import com.teamabcd.algorithmproblems.R;
import com.teamabcd.module.ojclient.OJClientHolder;

/**
 * Project: Algorithm Problems
 * Created by: Stackia <jsq2627@gmail.com>
 * Date: 11/9/14
 */
public class AnswerDashboardFragment extends SlidingFragment implements View.OnClickListener, LoginActivity.OnLoginListener {

    public static AnswerDashboardFragment newInstance() {
        AnswerDashboardFragment fragment = new AnswerDashboardFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_answer_dashboard, container, false);
        Button loginButton = (Button) view.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);
        Button tempRefreshButton = (Button) view.findViewById(R.id.answerDashboardTempRefreshButton);
        tempRefreshButton.setOnClickListener(this);
        refreshLoginStatus(view);

        WebView webView = (WebView) view.findViewById(R.id.answerDashboardTempWebView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new TempStatusWebViewClient());

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginButton:
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.startLoginActivity(getId());
                break;

            case R.id.answerDashboardTempRefreshButton:
                refreshTempWebView();
                break;
        }
    }

    @Override
    public int getNavigationBarTitleResource() {
        return R.string.navigation_bar_title_my_answer;
    }

    @Override
    public FragmentBackStackManager.NavigationBarHandler.NavigationBarActionButton getNavigationBarActionButton() {
        return null;
    }

    @Override
    public void onLogin() {
        refreshLoginStatus();
        refreshTempWebView();
    }

    public void refreshTempWebView() {
        try {
            View view = getView();
            if (view != null) {
                OJClientHolder ojClientHolder = (OJClientHolder) getActivity();
                WebView webView = (WebView) view.findViewById(R.id.answerDashboardTempWebView);
                webView.loadUrl("http://acm.hdu.edu.cn/status.php?user=" + ojClientHolder.getAccount().getUsername());
            }
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement OJClientHolder");
        }
    }

    public void refreshLoginStatus() {
        refreshLoginStatus(getView());
    }

    public void refreshLoginStatus(View view) {
        try {
            if (view != null) {
                OJClientHolder ojClientHolder = (OJClientHolder) getActivity();
                RelativeLayout loginLayout = (RelativeLayout) view.findViewById(R.id.answerDashboardLoginLayout);
                RelativeLayout tempAnswerDashboard = (RelativeLayout) view.findViewById(R.id.tempAnswerDashboard);

                if (ojClientHolder.getAccount().isAnonymous()) {
                    loginLayout.setVisibility(View.VISIBLE);
                    tempAnswerDashboard.setVisibility(View.GONE);
                } else {
                    loginLayout.setVisibility(View.GONE);
                    tempAnswerDashboard.setVisibility(View.VISIBLE);
                }
            }
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement OJClientHolder");
        }
    }

    private class TempStatusWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            view.setVisibility(View.GONE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            view.loadUrl("javascript:var trs = document.getElementsByTagName(\"table\")[0].getElementsByTagName(\"tr\");" +
                    "trs[0].innerHTML=\"\";trs[1].innerHTML=\"\";" +
                    "trs[2].innerHTML=\"\";trs[trs.length-1].innerHTML=\"\";" +
                    "document.getElementsByTagName(\"body\")[0].style.background = \"#F5F9FC\";" +
                    "document.getElementById(\"fixed_table\").getElementsByTagName(\"h1\")[0].innerHTML=\"\";");
            view.setVisibility(View.VISIBLE);
        }
    }
}
