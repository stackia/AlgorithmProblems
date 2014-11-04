package com.teamabcd.module.ojclient;

import java.net.CookieManager;
import java.net.CookiePolicy;

/**
 * Project: Algorithm Problems
 * Created by: Stackia<jsq2627@gmail.com>
 * Date: 10/14/14
 */
public class OJAccount {
    private Type type;
    private String username;
    private String password;
    private boolean anonymous = true;
    private CookieManager cookieManager = new CookieManager();

    private OJAccount() {
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
    }

    public OJAccount(Type type, String username, String password) {
        this();
        this.type = type;
        this.username = username;
        this.password = password;
        this.anonymous = false;
    }

    public OJAccount(Type type) {
        this();
        this.type = type;
        this.anonymous = true;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }

    public CookieManager getCookieManager() {
        return cookieManager;
    }

    public enum Type {
        HDU,
    }
}
