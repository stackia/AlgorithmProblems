package com.teamabcd.module.ojclient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Project: Algorithm Problems
 * Created by: Stackia <jsq2627@gmail.com>
 * Date: 10/14/14
 */
public class OJSolution {
    private int id = -1;
    private int problemId;
    private String code;
    private LanguageType languageType;
    private Status status;
    private String codeLength;
    private String executionTime;
    private String executionMemory;

    public OJSolution() {
    }

    public OJSolution(int problemId, String code, LanguageType languageType) {
        this.problemId = problemId;
        this.code = code;
        this.languageType = languageType;
    }

    public String getCodeLength() {
        return codeLength;
    }

    public void setCodeLength(String codeLength) {
        this.codeLength = codeLength;
    }

    public String getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(String executionTime) {
        this.executionTime = executionTime;
    }

    public String getExecutionMemory() {
        return executionMemory;
    }

    public void setExecutionMemory(String executionMemory) {
        this.executionMemory = executionMemory;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProblemId() {
        return problemId;
    }

    public void setProblemId(int problemId) {
        this.problemId = problemId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LanguageType getLanguageType() {
        return languageType;
    }

    public void setLanguageType(LanguageType languageType) {
        this.languageType = languageType;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public static List<LanguageType> getSupportedLanguageList() {
        List<LanguageType> languageTypes = Arrays.asList(LanguageType.values());
        return new ArrayList<LanguageType>(languageTypes.subList(1, languageTypes.size() - 1));
    }

    public enum LanguageType {
        ANY ("", ""),
        GCC ("GCC", "GNU C"),
        GPP ("G++", "GNU C++"),
        C ("VC", "Microsoft Visual C"),
        CPP ("VC++", "Microsoft Visual C++"),
        PASCAL ("Pascal"),
        JAVA ("Java"),
        FORTRAN ("Fortran"),
        PYTHON ("Python");

        private String literalFullName;
        private String literalShortName;

        LanguageType(String literalName) {
            this.literalFullName = literalName;
            this.literalShortName = literalName;
        }

        LanguageType(String literalShortName, String literalFullName) {
            this.literalShortName = literalShortName;
            this.literalFullName = literalFullName;
        }

        public String getLiteralFullName() {
            return literalFullName;
        }

        public String getLiteralShortName() {
            return literalShortName;
        }

        @Override
        public String toString() {
            return literalShortName;
        }
    }

    public enum Status {
        ANY,
        QUEUING,
        ACCEPTED,
        WRONG_ANSWER,
        PRESENTATION_ERROR,
        COMPILATION_ERROR,
        RUNTIME_ERROR,
        TIME_LIMIT_EXCEEDED,
        MEMORY_LIMIT_EXCEEDED,
        OUTPUT_LIMIT_EXCEEDED,
    }
}
