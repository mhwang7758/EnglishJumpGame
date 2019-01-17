package com.mhwang.englishjumpgame;

import android.graphics.Color;

/**
 * Description : 方块信息类
 * Author :mhwang
 * Date : 2018/3/15
 * Version : V1.0
 */

public class BlockInfo {
    private int mId;
    private String mQuestion;
    private String mAnswer;
    private int mLeftPosition;
    private int mTopPosition;
    private int mRightPosition;
    private int mBottomPosition;
    private int mWidth;
    private int mHeight;
    private int mColor = Color.BLACK;
    private boolean mChange = false;

    public boolean isChange() {
        return mChange;
    }

    public int getRightPosition() {
        return mRightPosition;
    }

    public void setRightPosition(int rightPosition) {
        mRightPosition = rightPosition;
    }

    public int getBottomPosition() {
        return mBottomPosition;
    }

    public void setBottomPosition(int bottomPosition) {
        mBottomPosition = bottomPosition;
    }

    public void setChange(boolean change) {
        mChange = change;
        mColor = Color.RED;
    }

    public String getQuestion() {
        return mQuestion;
    }

    public void setQuestion(String question) {
        mQuestion = question;
    }

    public String getAnswer() {
        return mAnswer;
    }

    public void setAnswer(String answer) {
        mAnswer = answer;
    }

    public int getLeftPosition() {
        return mLeftPosition;
    }

    public void setLeftPosition(int leftPosition) {
        mLeftPosition = leftPosition;
    }

    public int getTopPosition() {
        return mTopPosition;
    }

    public void setTopPosition(int topPosition) {
        mTopPosition = topPosition;
    }

    public int getWidth() {
        return mWidth;
    }

    public void setWidth(int width) {
        mWidth = width;
    }

    public int getHeight() {
        return mHeight;
    }

    public void setHeight(int height) {
        mHeight = height;
    }

    public int getId() {

        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        mColor = color;
    }
}
