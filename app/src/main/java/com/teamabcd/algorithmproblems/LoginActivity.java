package com.teamabcd.algorithmproblems;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;


public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onBackButtonClicked(View view) {
        onBackPressed();
    }
}
