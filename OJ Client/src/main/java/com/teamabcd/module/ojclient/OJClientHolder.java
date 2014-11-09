package com.teamabcd.module.ojclient;

import com.teamabcd.module.ojclient.OJAccount;
import com.teamabcd.module.ojclient.OJAccountOperator;
import com.teamabcd.module.ojclient.OJProblemFetcher;
import com.teamabcd.module.ojclient.OJSolutionSubmitter;

/**
* Created by Stackia on 11/9/14.
*/
public interface OJClientHolder {

    public OJAccount getAccount();

    public void setAccount(OJAccount account);

    public OJAccountOperator getAccountOperator();

    public OJProblemFetcher getProblemFetcher();

    public OJSolutionSubmitter getSolutionSubmitter();

}
