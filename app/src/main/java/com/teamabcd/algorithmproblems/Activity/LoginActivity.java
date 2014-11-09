package com.teamabcd.algorithmproblems.Activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.teamabcd.algorithmproblems.R;
import com.teamabcd.module.ojclient.OJAccount;
import com.teamabcd.module.ojclient.OJAccountOperator;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;

public class LoginActivity extends Activity {

    public static final String INTENT_EXTRA_OJ_ACCOUNT = "OJ_ACCOUNT";
    public static final int RESULT_CODE_LOGIN_SUCCESSFUL = 1;
    public static final String FETCH_STATE_TAG = "FETCH_STATE";

    public static MainActivity mainActivity;

    private MainActivity.FetchState fetchState = MainActivity.FetchState.Failed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (savedInstanceState != null) {
            setFetchState((MainActivity.FetchState) savedInstanceState.getSerializable(FETCH_STATE_TAG));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(FETCH_STATE_TAG, fetchState);
    }

    public void setFetchState(MainActivity.FetchState fetchState) {
        this.fetchState = fetchState;
        Button loginButton = (Button) findViewById(R.id.loginButton);
        CircularProgressBar progressBar = (CircularProgressBar) findViewById(R.id.loadingProgressBar);

        switch (fetchState) {
            case Failed:
                loginButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                break;

            case Working:
                loginButton.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void onBackButtonClicked(View view) {
        onBackPressed();
    }

    public void onLoginButtonClicked(View view) {
        EditText usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        EditText passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if (username == null || username.isEmpty()) {
            Toast.makeText(this, R.string.login_username_cannot_be_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        if (password == null || password.isEmpty()) {
            Toast.makeText(this, R.string.login_password_cannot_be_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        LoginAsyncTask loginAsyncTask = new LoginAsyncTask();
        loginAsyncTask.execute(username, password);
    }

    public interface OnLoginListener {
        public void onLogin();
    }

    private class LoginAsyncTask extends AsyncTask<String, Integer, OJAccount> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setFetchState(MainActivity.FetchState.Working);
        }

        @Override
        protected OJAccount doInBackground(String... params) {
            OJAccount account = new OJAccount(OJAccount.Type.HDU, params[0], params[1]);
            OJAccountOperator accountOperator = new OJAccountOperator(account);
            if (accountOperator.login()) {
                return account;
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(OJAccount account) {
            super.onPostExecute(account);
            if (account == null) {
                setFetchState(MainActivity.FetchState.Failed);
                Toast.makeText(LoginActivity.this, R.string.login_failed, Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                if (LoginActivity.mainActivity != null) {
                    LoginActivity.mainActivity.setAccount(account);
                }
                setResult(RESULT_CODE_LOGIN_SUCCESSFUL, getIntent());
                finish();
            } catch (Exception ignored) {
            }
        }
    }
}
