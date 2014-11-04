package com.teamabcd.module.ojclient;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Project: Algorithm Problems
 * Created by: Stackia<jsq2627@gmail.com>
 * Date: 10/14/14
 */
public class OJProblemFetcher {
    private OJAccount account;
    private OkHttpClient httpClient = new OkHttpClient();

    private String sharedFirstPageContent;

    public OJProblemFetcher(OJAccount account) {
        setAccount(account);
    }

    public int fetchProblemListPageCount() {
        int pageCount = 0;
        try {
            switch (account.getType()) {
                case HDU:
                    Request request = new Request.Builder().url(OJConstants.getHDUProblemListPageURL(1)).build();
                    Response response = httpClient.newCall(request).execute();
                    sharedFirstPageContent = OJUtils.decodeGB2312(response.body().bytes());
                    Pattern pattern = Pattern.compile("<a (style=\"color:red;\" )?href=listproblem.php\\?vol=(\\d+) style=\"margin:5px\">");
                    Matcher matcher = pattern.matcher(sharedFirstPageContent);
                    int maxPage = 0;
                    while (matcher.find()) {
                        int page = Integer.parseInt(matcher.group(2));
                        if (page > maxPage) {
                            maxPage = page;
                        }
                    }
                    pageCount = maxPage;
                    break;
            }
        } catch (IOException e) {
            pageCount = 0;
        }
        return pageCount;
    }

    public List<OJProblem> fetchProblemList(int page) {
        List<OJProblem> problemList = null;
        try {
            switch (account.getType()) {
                case HDU:
                    String pageContent;
                    if (page == 1 && !sharedFirstPageContent.isEmpty()) {
                        pageContent = sharedFirstPageContent;
                        sharedFirstPageContent = "";
                    } else {
                        Request request = new Request.Builder().url(OJConstants.getHDUProblemListPageURL(page)).build();
                        Response response = httpClient.newCall(request).execute();
                        pageContent = OJUtils.decodeGB2312(response.body().bytes());
                    }
                    problemList = new ArrayList<OJProblem>();
                    Pattern pattern = Pattern.compile("p\\(\\d+,(\\d+),(-?\\d+),\"((?:\\\\.|[^\"])*?)\",(\\d+),(\\d+)\\);");
                    Matcher matcher = pattern.matcher(pageContent);
                    while (matcher.find()) {
                        OJProblem.SolveStatus solveStatus;
                        int problemId = Integer.parseInt(matcher.group(1));
                        switch (Integer.parseInt(matcher.group(2))) {
                            case 0:
                                solveStatus = OJProblem.SolveStatus.UNTRIED;
                                break;
                            case 5:
                                solveStatus = OJProblem.SolveStatus.ACCEPT;
                                break;
                            case 6:
                                solveStatus = OJProblem.SolveStatus.TRIED_BUT_WRONG;
                                break;
                            default:
                                solveStatus = OJProblem.SolveStatus.UNTRIED;
                        }
                        String title = matcher.group(3);
                        int accepted = Integer.parseInt(matcher.group(4));
                        int submitted = Integer.parseInt(matcher.group(5));
                        problemList.add(new OJProblem(problemId, solveStatus, title, accepted, submitted));
                    }
                    break;
            }
        } catch (IOException e) {
            problemList = null;
        }

        return problemList;
    }

    public OJProblem fetchProblem(int problemId) {
        OJProblem problem = null;
        try {
            switch (account.getType()) {
                case HDU:
                    Request request = new Request.Builder().url(OJConstants.getHDUProblemURL(problemId)).build();
                    Response response = httpClient.newCall(request).execute();
                    String problemContent = OJUtils.decodeGB2312(response.body().bytes());
                    Pattern metaPattern = Pattern.compile("<tr><td align=center><h1 style='color:#1A5CC8'>(.*?)</h1><font><b><span style='font-family:Arial;font-size:12px;font-weight:bold;color:green'>Time Limit: (\\d+/\\d+ MS) \\(Java/Others\\)&nbsp;&nbsp;&nbsp;&nbsp;Memory Limit: (\\d+/\\d+ K) \\(Java/Others\\)<br>Total Submission\\(s\\): (\\d+)&nbsp;&nbsp;&nbsp;&nbsp;Accepted Submission\\(s\\): (\\d+)<br></span>", Pattern.DOTALL);
                    Matcher metaMatcher = metaPattern.matcher(problemContent);
                    boolean noMatch = true;
                    problem = new OJProblem();
                    problem.setId(problemId);
                    if (metaMatcher.find()) {
                        noMatch = false;
                        problem.setTitle(metaMatcher.group(1));
                        problem.setTimeLimit(metaMatcher.group(2));
                        problem.setMemoryLimit(metaMatcher.group(3));
                        problem.setTotalSubmission(Integer.parseInt(metaMatcher.group(4)));
                        problem.setAcceptedSubmission(Integer.parseInt(metaMatcher.group(5)));
                    }
                    Pattern bodyPattern = Pattern.compile("<div class=panel_title align=left>Problem Description</div> <div class=panel_content>(.*?)</div><div class=panel_bottom>&nbsp;</div><br><div class=panel_title align=left>Input</div> <div class=panel_content>(.*?)</div><div class=panel_bottom>&nbsp;</div><br><div class=panel_title align=left>Output</div> <div class=panel_content>(.*?)</div><div class=panel_bottom>&nbsp;</div><br><div class=panel_title align=left>Sample Input</div><div class=panel_content><pre><div style=\"font-family:Courier New,Courier,monospace;\">(.*?)</div></pre></div><div class=panel_bottom>&nbsp;</div><br><div class=panel_title align=left>Sample Output</div><div class=panel_content><pre><div style=\"font-family:Courier New,Courier,monospace;\">(.*?)</div></pre></div><div class=panel_bottom>&nbsp;</div>", Pattern.DOTALL);
                    Matcher bodyMatcher = bodyPattern.matcher(problemContent);
                    if (bodyMatcher.find()) {
                        noMatch = false;
                        problem.setDescription(bodyMatcher.group(1));
                        problem.setInputDescription(bodyMatcher.group(2));
                        problem.setOutputDescription(bodyMatcher.group(3));
                        problem.setInputSample(bodyMatcher.group(4));
                        problem.setOutputSample(bodyMatcher.group(5));
                    }
                    if (noMatch) {
                        problem = null;
                    }
                    break;
            }
        } catch (IOException e) {
            problem = null;
        }
        return problem;
    }

    public boolean fillProblem(OJProblem baseProblem) {
        if (baseProblem.getId() == 0) {
            return false;
        }
        OJProblem problem = fetchProblem(baseProblem.getId());
        if (problem == null) {
            return false;
        }
        baseProblem.setTitle(problem.getTitle());
        baseProblem.setAcceptedSubmission(problem.getAcceptedSubmission());
        baseProblem.setTotalSubmission(problem.getTotalSubmission());
        baseProblem.setProblemDetails(problem.getDescription(), problem.getInputDescription(), problem.getOutputDescription(), problem.getInputSample(), problem.getOutputSample(), problem.getTimeLimit(), problem.getMemoryLimit());
        return true;
    }

    public OJAccount getAccount() {
        return account;
    }

    public void setAccount(OJAccount account) {
        this.account = account;
        httpClient.setCookieHandler(account.getCookieManager());
    }
}
