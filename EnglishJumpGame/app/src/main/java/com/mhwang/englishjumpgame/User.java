package com.mhwang.englishjumpgame;

/**
 * Author : mhwang   站在方格上的人或动物
 * Date : $Date$
 * Version : V1.0
 */
public class User {
    private int mPictureId = R.drawable.ic_animal;
    private int mLeftPos = 0;
    private int mTopPos = 0;

    public int getPictureId() {
        return mPictureId;
    }

    public void setPictureId(int pictureId) {
        mPictureId = pictureId;
    }

    public int getLeftPos() {
        return mLeftPos;
    }

    public void setLeftPos(int leftPos) {
        mLeftPos = leftPos;
    }

    public int getTopPos() {
        return mTopPos;
    }

    public void setTopPos(int topPos) {
        mTopPos = topPos;
    }
}
