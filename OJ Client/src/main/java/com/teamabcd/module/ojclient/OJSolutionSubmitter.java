package com.teamabcd.module.ojclient;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Project: Algorithm Problems
 * Created by: Stackia <jsq2627@gmail.com>
 * Date: 10/14/14
 */
public class OJSolutionSubmitter {
    private OJAccount account;
    private OkHttpClient httpClient = new OkHttpClient();

    public OJSolutionSubmitter(OJAccount account) {
        setAccount(account);
    }

    public List<OJSolution.LanguageType> getAvailableLanguageTypes() {
        return getAvailableLanguageTypes(account.getType());
    }

    public List<OJSolution.LanguageType> getAvailableLanguageTypes(OJAccount.Type accountType) {
        ArrayList<OJSolution.LanguageType> languageTypes = new ArrayList<OJSolution.LanguageType>();
        switch (accountType) {
            case HDU:
                languageTypes.add(OJSolution.LanguageType.C);
                languageTypes.add(OJSolution.LanguageType.CPP);
                languageTypes.add(OJSolution.LanguageType.GCC);
                languageTypes.add(OJSolution.LanguageType.GPP);
                languageTypes.add(OJSolution.LanguageType.PASCAL);
                languageTypes.add(OJSolution.LanguageType.JAVA);
                break;
        }
        return languageTypes;
    }

    public boolean submit(OJSolution solution) {
        boolean success = false;
        try {
            switch (account.getType()) {
                case HDU:
                    if (getAvailableLanguageTypes(OJAccount.Type.HDU).contains(solution.getLanguageType())) {
                        RequestBody requestBody = RequestBody.create(
                                OJConstants.URLEncodedForm,
                                OJUtils.generateURLForm(
                                        "check", "0",
                                        "problemid", Integer.toString(solution.getProblemId()),
                                        "language", Integer.toString(OJConstants.getHDULanguageTypeId(solution.getLanguageType())),
                                        "usercode", solution.getCode()
                                )
                        );
                        Request request = new Request.Builder().url(OJConstants.getHDUSolutionSubmitURL()).post(requestBody).build();
                        Response response = httpClient.newCall(request).execute();
                        String responseContent = OJUtils.decodeGB2312(response.body().bytes());
                        Pattern pattern = Pattern.compile("<a href=\"/viewcode.php\\?rid=(\\d+)\"  target=_blank>\\d+\\s*?B</td>");
                        Matcher matcher = pattern.matcher(responseContent);
                        if (matcher.find()) {
                            solution.setId(Integer.parseInt(matcher.group(1)));
                            success = true;
                        } else {
                            success = false;
                        }
                    } else {
                        success = false;
                    }
                    break;
            }
        } catch (IOException e) {
            success = false;
        }
        return success;
    }

    public boolean fillSolution(OJSolution solution) {
        boolean success = false;
        try {
            switch (account.getType()) {
                case HDU:
                    Request request = new Request.Builder().url(OJConstants.getHDUSolutionStatusURL(solution.getId(), 0, "", OJSolution.LanguageType.ANY, OJSolution.Status.ANY)).build();
                    Response response = httpClient.newCall(request).execute();
                    String responseBody = OJUtils.decodeGB2312(response.body().bytes());
                    Pattern pattern = Pattern.compile("<tr (?:bgcolor=#D7EBFF )?align=center ><td height=22px>" + solution.getId() + "</td><td>[0-9: -]+?</td><td>.*?(Compilation Error|Wrong Answer|Accepted|Queuing|Presentation Error|Runtime Error<br>\\([A-Z_]+?\\)|Time Limit Exceeded|Memory Limit Exceeded|Output Limit Exceeded).*?</td><td><a href=\"/showproblem.php\\?pid=\\d+\">(\\d+)</a></td><td>(\\d+MS)</td><td>(\\d+K)</td><td>.*?(\\d+)\\s*?B.*?</td>.*?</tr>", Pattern.DOTALL);
                    Matcher matcher = pattern.matcher(responseBody);
                    if (matcher.find()) {
                        String statusString = matcher.group(1);
                        int problemId = Integer.parseInt(matcher.group(2));
                        String executionTime = matcher.group(3);
                        String executionMemory = matcher.group(4);
                        String codeLength = matcher.group(5) + "B";
                        OJSolution.Status status = OJSolution.Status.ANY;
                        if (statusString.equals("Compilation Error")) {
                            status = OJSolution.Status.COMPILATION_ERROR;
                        } else if (statusString.equals("Wrong Answer")) {
                            status = OJSolution.Status.WRONG_ANSWER;
                        } else if (statusString.equals("Accepted")) {
                            status = OJSolution.Status.ACCEPTED;
                        } else if (statusString.equals("Queuing")) {
                            status = OJSolution.Status.QUEUING;
                        } else if (statusString.equals("Presentation Error")) {
                            status = OJSolution.Status.PRESENTATION_ERROR;
                        } else if (statusString.contains("Runtime Error")) {
                            status = OJSolution.Status.RUNTIME_ERROR;
                        } else if (statusString.equals("Time Limit Exceeded")) {
                            status = OJSolution.Status.TIME_LIMIT_EXCEEDED;
                        } else if (statusString.equals("Memory Limit Exceeded")) {
                            status = OJSolution.Status.MEMORY_LIMIT_EXCEEDED;
                        } else if (statusString.equals("Output Limit Exceeded")) {
                            status = OJSolution.Status.OUTPUT_LIMIT_EXCEEDED;
                        }
                        solution.setStatus(status);
                        solution.setProblemId(problemId);
                        solution.setExecutionTime(executionTime);
                        solution.setExecutionMemory(executionMemory);
                        solution.setCodeLength(codeLength);
                        success = true;
                    } else {
                        success = false;
                    }
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
