package com.teamabcd.module.ojclient;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Project: Algorithm Problems
 * Created by: Stackia <jsq2627@gmail.com>
 * Date: 10/14/14
 */
public class OJAccountOperator {
    private OJAccount account;
    private OkHttpClient httpClient = new OkHttpClient();

    public OJAccountOperator(OJAccount account) {
        setAccount(account);
    }

    public boolean prepareSession() {
        boolean success = false;
        try {
            switch (account.getType()) {
                case HDU:
                    if (account.isAnonymous()) {
                        Request request = new Request.Builder().url(OJConstants.getHDUPrepareURL()).build();
                        Response response = httpClient.newCall(request).execute();
                        success = (response.code() == 200);
                    } else {
                        success = true;
                    }
                    break;
            }
        } catch (Exception e) {
            success = false;
        }
        return success;
    }

    public boolean login() {
        if (account.isAnonymous()) {
            return true;
        }
        boolean success = false;
        try {
            switch (account.getType()) {
                case HDU:
                    RequestBody requestBody = RequestBody.create(
                            OJConstants.URLEncodedForm,
                            OJUtils.generateURLForm(
                                    "username", account.getUsername(),
                                    "userpass", account.getPassword(),
                                    "login", "Sign In"
                            )
                    );
                    Request request = new Request.Builder().url(OJConstants.getHDULoginURL()).post(requestBody).build();
                    Response response = httpClient.newCall(request).execute();
                    String responseBody = OJUtils.decodeGB2312(response.body().bytes());
                    success = responseBody.contains("<a href=\"/userloginex.php?action=logout\" style=\"text-decoration: none\">");
                    break;
            }
        } catch (IOException e) {
            success = false;
        }
        return success;
    }

    public boolean logout() {
        if (account.isAnonymous()) {
            return true;
        }
        boolean success = false;
        try {
            switch (account.getType()) {
                case HDU:
                    Request request = new Request.Builder().url(OJConstants.getHDULogoutURL()).build();
                    Response response = httpClient.newCall(request).execute();
                    success = (response.code() == 200);
                    break;
            }
        } catch (IOException e) {
            success = false;
        }
        return success;
    }

    public OJAccount getAccount() {
        return account;
    }

    public void setAccount(OJAccount account) {
        this.account = account;
        httpClient.setCookieHandler(account.getCookieManager());
    }
}
