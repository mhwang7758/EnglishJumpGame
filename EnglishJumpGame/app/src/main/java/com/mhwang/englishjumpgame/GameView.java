package com.mhwang.englishjumpgame;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


/**
 * Description :  生成一块块可跳的
 * Author :mhwang
 * Date : 2018/3/5
 * Version : V1.0
 */

public class GameView extends View{
    private static final int START_LEFT = 100;
    private static final int BLOCK_INTERVAL = 50;  // 方块间的间隙
    private static final int NEXT_LINE_BACK = 15;  // 换行时返回的数据，为了看起来不规则

    private int mScreenWidth = 100;
    private int mScreenHeight = 100;
    private Paint mPaint;

    private List<BlockInfo> mBlockInfos;

    private BlockInfo mCurDrawingBlock = null;      // 当前显示的方块
    private BlockInfo mCurUserBlock = null;      // 当前用户所在方块
    private BlockInfo mUserTargetBlock = null;      // 当前用户所在方块
    private boolean mLeft2Right = true;             // 是否从左向右

    private int mCurLeftPos = 0;
    private int mCurRightPos = 0;
    private int mCurTopPos = 0;
    private int mCurBottomPos = 0;
    private User mUser = new User();
    private Context mContext;

    private boolean start = true;

    private OnBlockTouchListener mTouchListener = null;

    private void showLog(String msg){
        Log.d("GameView-->",msg);
    }
    public GameView(Context context, int width, int height) {
        super(context);
        mContext = context;
        mBlockInfos = new ArrayList<>();
        mPaint = new Paint();
        mPaint.setColor(Color.GREEN);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(1);
    }

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;
        showLog("screen width is "+mScreenWidth+" height is "+mScreenHeight);
        mBlockInfos = new ArrayList<>();
        mPaint = new Paint();
        mPaint.setColor(Color.GREEN);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(1);
        start = true;
    }

    /** 添加方块
     * @param blockInfo
     */
    public void addBlock(BlockInfo blockInfo){
        mBlockInfos.add(blockInfo);
        this.invalidate();
    }

    public List<BlockInfo> getBlockInfos() {
        return mBlockInfos;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        showLog("onDraw");

        initBlockData(canvas);

        drawUserBlock(canvas);
        start = false;

    }

    private void jumpAnimation(int endLeftPos, int endTopPos){

    }

    public void reset(){
        start = true;
    }

    public void gameOver(){
        mBlockInfos.clear();
        invalidate();
    }

    private void initBlockData(Canvas canvas){
        for (int i = 0; i < mBlockInfos.size(); i++) {
            BlockInfo blockInfo = mBlockInfos.get(i);
            mPaint.setColor(blockInfo.getColor());
            // 如果第一个方块，往前移一点
            if (i == 0){
                mCurLeftPos = 100 + blockInfo.getWidth();
                mCurRightPos =  mCurLeftPos + blockInfo.getWidth();
                mCurTopPos = mScreenHeight - blockInfo.getHeight() * 2;
                mCurBottomPos = mCurTopPos + blockInfo.getHeight();
            }else{
                // 如果是从左往右添加
                if (mLeft2Right){
                    addBlockFromLeft2Right(blockInfo);
                }else{
                    addBlockFromRight2Left(blockInfo);
                }
            }
            mCurDrawingBlock = blockInfo;
            showLog("当前绘制方块位置"+i+" 左上角坐标: ("+mCurLeftPos+","+mCurTopPos+")");
            blockInfo.setLeftPosition(mCurLeftPos);
            blockInfo.setTopPosition(mCurTopPos);
            blockInfo.setRightPosition(mCurRightPos);
            blockInfo.setBottomPosition(mCurBottomPos);
            canvas.drawRect(mCurLeftPos, mCurTopPos, mCurRightPos, mCurBottomPos, mPaint);
            // 如果未发生改变，则加上问号标志
            if (!blockInfo.isChange()){
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_no_answer);
                canvas.drawBitmap(bitmap, mCurLeftPos, mCurTopPos, mPaint);
            }
            // 如果是第一个方块，把人物画上去，需要在方块后画，不然会被覆盖
            if (i == 0 && start){         // 如果刚开始，把第一个方块的坐标赋值给人物
                showLog("init user block");
                mUser.setLeftPos(blockInfo.getLeftPosition());
                mUser.setTopPos(blockInfo.getTopPosition());
            }

        }
    }

    private void drawUserBlock(Canvas canvas){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),mUser.getPictureId());
        if (bitmap == null){
            showLog("bitmap is null");
        }else {
            canvas.drawBitmap(bitmap, (int)mUser.getLeftPos(), (int)mUser.getTopPos(), mPaint);
        }
    }

    public void moveToBlock(int x, int y){
        showLog("moveTo x="+x+" y="+y);
        mUser.setLeftPos(x);
        mUser.setTopPos(y);
        invalidate();
    }

    /**
     *  从左往右添加方块
     */
    private void addBlockFromLeft2Right(BlockInfo blockInfo){
        int nextBlockLeftPos = mCurLeftPos + mCurDrawingBlock.getWidth() + BLOCK_INTERVAL;
        // 如果下个方块的左上角位置没有超出边界，则将其置为当前方块位置，否则向上一行
        if (nextBlockLeftPos + blockInfo.getWidth() <= mScreenWidth) {
            mCurLeftPos = nextBlockLeftPos;
            mCurRightPos = mCurLeftPos + blockInfo.getWidth();
        }else{
            int nextBlockTopPos = mCurTopPos - mCurDrawingBlock.getHeight() - BLOCK_INTERVAL;
            // 如果高度超出了，提示游戏结束
            if (nextBlockTopPos < 0){
                showLog("game over");
                return;
            }
            // 往左移回一点点，看起来不规则点
            mCurLeftPos = mCurLeftPos - NEXT_LINE_BACK;
            mCurRightPos = mCurLeftPos + blockInfo.getWidth();
            mCurTopPos = nextBlockTopPos;
            mCurBottomPos = mCurTopPos + blockInfo.getHeight();
            mLeft2Right = false;
        }
    }

    /**
     *  从左往右添加方块
     */
    private void addBlockFromRight2Left(BlockInfo blockInfo){
        int nextBlockLeftPos = mCurLeftPos - blockInfo.getWidth() - BLOCK_INTERVAL;
        // 如果下个方块的左上角位置没有超出边界，则将其置为当前方块位置，否则向上一行
        if (nextBlockLeftPos >= 0) {
            mCurLeftPos = nextBlockLeftPos;
            mCurRightPos = mCurLeftPos + blockInfo.getWidth();
        }else{
            int nextBlockTopPos = mCurTopPos - mCurDrawingBlock.getHeight() - BLOCK_INTERVAL;
            // 如果高度超出了，提示游戏结束
            if (nextBlockTopPos > mScreenHeight){
                showLog("game over");
                return;
            }
            // 往左移回一点点，看起来不规则点
            mCurLeftPos = mCurLeftPos + NEXT_LINE_BACK;
            mCurRightPos = mCurLeftPos + blockInfo.getWidth();

            mCurTopPos = nextBlockTopPos;
            mCurBottomPos = mCurTopPos + blockInfo.getHeight();
            mLeft2Right = true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
//                float x = event.getX();
//                float y = event.getY();
//                showLog("action down touch x is "+event.getX());
//                showLog("action down touch y is "+event.getY());
//                for(BlockInfo blockInfo : mBlockInfos){
//                    if (blockInfo.getLeftPosition() < x && blockInfo.getRightPosition() > x
//                            && blockInfo.getTopPosition() < y && blockInfo.getBottomPosition() > y){
//                        blockInfo.setChange(true);
//                        showLog("touch blockInfo "+blockInfo.getId());
//                        showLog("touch blockInfo x="+blockInfo.getLeftPosition());
//                        showLog("touch blockInfo y="+blockInfo.getTopPosition());
////                        mUser.setLeftPos(blockInfo.getLeftPosition());
////                        mUser.setTopPos(blockInfo.getTopPosition());
//                        mUserTargetBlock = blockInfo;
//                        break;
//                    }
//                }
//                mTouchListener.onBlockTouch(mUser.getLeftPos(), mUser.getTopPos(), mUserTargetBlock);
                break;
            case MotionEvent.ACTION_UP:
                showLog("action up touch x is "+event.getX());
                showLog("action up touch y is "+event.getY());
                break;
        }
        return super.onTouchEvent(event);
    }

    public void setOnBlockTouchListener(OnBlockTouchListener listener){
        mTouchListener = listener;
    }

    public interface OnBlockTouchListener{
        void onBlockTouch(int currentX, int currentY, BlockInfo touchBlock);
    }
}
