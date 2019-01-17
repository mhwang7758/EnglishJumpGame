package com.mhwang.bean;

/**
 * Author : mhwang
 * Date : $Date$
 * Version : V1.0
 */
public class Question {
    private int mNum;
    private boolean mDelete = false;
    private boolean mAnswered = false;
    private String mType;
    private String mQuestion;
    private String mCreateTime;

    public int getNum() {
        return mNum;
    }

    public void setNum(int num) {
        mNum = num;
    }

    public String getQuestion() {
        return mQuestion;
    }

    public void setQuestion(String question) {
        mQuestion = question;
    }

    public String getCreateTime() {
        return mCreateTime;
    }

    public void setCreateTime(String createTime) {
        mCreateTime = createTime;
    }

    public boolean isDelete() {
        return mDelete;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        this.mType = type;
    }

    public void setDelete(boolean delete) {
        mDelete = delete;
    }

    public boolean isAnswered() {
        return mAnswered;
    }

    public void setAnswered(boolean answered) {
        mAnswered = answered;
    }
}
