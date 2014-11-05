package com.teamabcd.module.ojclient;

import android.nfc.Tag;

import java.io.Serializable;

/**
 * Project: Algorithm Problems
 * Created by: Stackia<jsq2627@gmail.com>
 * Date: 10/14/14
 */
public class OJProblem implements Serializable {

    public final static String tag = "OJProblem";

    private int id;
    private SolveStatus solveStatus = SolveStatus.UNTRIED;
    private int acceptedSubmission;
    private int totalSubmission;
    private String title;
    private String description;
    private String inputDescription;
    private String outputDescription;
    private String inputSample;
    private String outputSample;
    private String timeLimit;
    private String memoryLimit;

    public OJProblem() {
    }

    public OJProblem(int problemId, SolveStatus solveStatus, String title, int acceptedSubmission, int totalSubmission) {
        this.id = problemId;
        this.solveStatus = solveStatus;
        this.title = title;
        this.totalSubmission = totalSubmission;
        this.acceptedSubmission = acceptedSubmission;
    }

    public void setProblemDetails(String description,
                                  String inputDescription,
                                  String outputDescription,
                                  String inputSample,
                                  String outputSample,
                                  String timeLimit,
                                  String memoryLimit) {
        this.description = description;
        this.inputDescription = inputDescription;
        this.outputDescription = outputDescription;
        this.inputSample = inputSample;
        this.outputSample = outputSample;
        this.timeLimit = timeLimit;
        this.memoryLimit = memoryLimit;
    }

    public int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    public SolveStatus getSolveStatus() {
        return solveStatus;
    }

    void setSolveStatus(SolveStatus solveStatus) {
        this.solveStatus = solveStatus;
    }

    public int getTotalSubmission() {
        return totalSubmission;
    }

    void setTotalSubmission(int totalSubmission) {
        this.totalSubmission = totalSubmission;
    }

    public int getAcceptedSubmission() {
        return acceptedSubmission;
    }

    void setAcceptedSubmission(int acceptedSubmission) {
        this.acceptedSubmission = acceptedSubmission;
    }

    public String getTitle() {
        return title;
    }

    void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    public String getInputDescription() {
        return inputDescription;
    }

    void setInputDescription(String inputDescription) {
        this.inputDescription = inputDescription;
    }

    public String getOutputDescription() {
        return outputDescription;
    }

    void setOutputDescription(String outputDescription) {
        this.outputDescription = outputDescription;
    }

    public String getInputSample() {
        return inputSample;
    }

    void setInputSample(String inputSample) {
        this.inputSample = inputSample;
    }

    public String getOutputSample() {
        return outputSample;
    }

    void setOutputSample(String outputSample) {
        this.outputSample = outputSample;
    }

    public String getTimeLimit() {
        return timeLimit;
    }

    void setTimeLimit(String timeLimit) {
        this.timeLimit = timeLimit;
    }

    public String getMemoryLimit() {
        return memoryLimit;
    }

    void setMemoryLimit(String memoryLimit) {
        this.memoryLimit = memoryLimit;
    }

    public enum SolveStatus {
        UNTRIED,
        TRIED_BUT_WRONG,
        ACCEPT,
    }
}
