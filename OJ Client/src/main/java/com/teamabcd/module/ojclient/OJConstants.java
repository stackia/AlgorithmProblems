package com.teamabcd.module.ojclient;

import com.squareup.okhttp.MediaType;

/**
 * Project: Algorithm Problems
 * Created by: Stackia <jsq2627@gmail.com>
 * Date: 10/14/14
 */
class OJConstants {
    final static String HDUBaseURL = "http://acm.hdu.edu.cn/";
    final static MediaType URLEncodedForm = MediaType.parse("application/x-www-form-urlencoded");

    static String getHDULoginURL() {
        return HDUBaseURL + "userloginex.php?action=login";
    }

    static String getHDUPrepareURL() {
        return HDUBaseURL;
    }

    static String getHDULogoutURL() {
        return HDUBaseURL + "userloginex.php?action=logout";
    }

    static String getHDUProblemListPageURL(int page) {
        return HDUBaseURL + "listproblem.php?vol=" + page;
    }

    static String getHDUProblemURL(int problemId) {
        return HDUBaseURL + "showproblem.php?pid=" + problemId;
    }

    static int getHDULanguageTypeId(OJSolution.LanguageType languageType) {
        int languageTypeId = -1;
        switch (languageType) {
            case GPP:
                languageTypeId = 0;
                break;
            case GCC:
                languageTypeId = 1;
                break;
            case CPP:
                languageTypeId = 2;
                break;
            case C:
                languageTypeId = 3;
                break;
            case PASCAL:
                languageTypeId = 4;
                break;
            case JAVA:
                languageTypeId = 5;
                break;
            default:
                languageTypeId = -1;
        }
        return languageTypeId;
    }

    static String getHDUSolutionSubmitURL() {
        return HDUBaseURL + "submit.php?action=submit";
    }

    static String getHDUSolutionStatusURL(int from, int problemId, String author, OJSolution.LanguageType languageType, OJSolution.Status solutionStatus) {
        int languageTypeId = -1;
        int solutionStatusId = -1;
        switch (languageType) {
            case ANY:
                languageTypeId = 0;
                break;
            case GPP:
                languageTypeId = 1;
                break;
            case GCC:
                languageTypeId = 2;
                break;
            case CPP:
                languageTypeId = 3;
                break;
            case C:
                languageTypeId = 4;
                break;
            case PASCAL:
                languageTypeId = 5;
                break;
            case JAVA:
                languageTypeId = 6;
                break;
            default:
                languageTypeId = -1;
        }
        switch (solutionStatus) {
            case ANY:
                solutionStatusId = 0;
                break;
            case ACCEPTED:
                solutionStatusId = 5;
                break;
            case WRONG_ANSWER:
                solutionStatusId = 6;
                break;
            case PRESENTATION_ERROR:
                solutionStatusId = 8;
                break;
            case COMPILATION_ERROR:
                solutionStatusId = 12;
                break;
            case RUNTIME_ERROR:
                solutionStatusId = 7;
                break;
            case TIME_LIMIT_EXCEEDED:
                solutionStatusId = 9;
                break;
            case MEMORY_LIMIT_EXCEEDED:
                solutionStatusId = 10;
                break;
            case OUTPUT_LIMIT_EXCEEDED:
                solutionStatusId = 11;
                break;
            default:
                solutionStatusId = -1;
        }
        return HDUBaseURL + "status.php?first=" + from + "&user=" + author + "&pid=" + problemId + "&lang=" + languageTypeId + "&status=" + solutionStatusId;
    }
}
