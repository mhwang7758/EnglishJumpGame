package com.mhwang.bean;

/**
 * Author : mhwang
 * Date : $Date$
 * Version : V1.0
 */
public class Answer {
    private int mNum;
    private String mAnswer;
    private boolean mCorrect;
    private String mCreateTime;

    public int getNum() {
        return mNum;
    }

    public void setNum(int num) {
        mNum = num;
    }

    public String getAnswer() {
        return mAnswer;
    }

    public void setAnswer(String answer) {
        mAnswer = answer;
    }

    public boolean isCorrect() {
        return mCorrect;
    }

    public void setCorrect(boolean correct) {
        mCorrect = correct;
    }

    public String getCreateTime() {
        return mCreateTime;
    }

    public void setCreateTime(String createTime) {
        mCreateTime = createTime;
    }
}
