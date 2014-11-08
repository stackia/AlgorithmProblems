package com.teamabcd.algorithmproblems;

import com.teamabcd.module.ojclient.OJAccount;
import com.teamabcd.module.ojclient.OJAccountOperator;
import com.teamabcd.module.ojclient.OJProblemFetcher;
import com.teamabcd.module.ojclient.OJSolutionSubmitter;

public interface OJClientHolder {

    public OJAccount getAccount();

    public void setAccount(OJAccount account);

    public OJAccountOperator getAccountOperator();

    public OJProblemFetcher getProblemFetcher();

    public OJSolutionSubmitter getSolutionSubmitter();

}
