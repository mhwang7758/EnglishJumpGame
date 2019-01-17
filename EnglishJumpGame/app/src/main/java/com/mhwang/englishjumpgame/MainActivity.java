package com.mhwang.englishjumpgame;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.mhwang.dialog.AnswerQuestionDialog;
import com.mhwang.dialog.QuestionDialog;
import com.mhwang.util.SoundUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {

    private static final int REQUEST_QUESTION = 1000;

    private GameView mGameView;
    private int mCurBlockIndex = 0;

    private User mUser = null;

    @BindView(R.id.btn_restart)
    Button btn_restart;

    private SoundUtil mSoundUtil;

    private void showLog(String s){
        Log.d("MainActivity-->",s);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mGameView = (GameView) findViewById(R.id.gv_gameView);
        mSoundUtil = SoundUtil.getInstance(this);

        // 随意移动版本
//        mGameView.setOnBlockTouchListener(new GameView.OnBlockTouchListener() {
//            @Override
//            public void onBlockTouch(final int currentX, final int currentY, final BlockInfo touchBlock) {
//                // 如果横向移动，则判断是向左还是向右
//                if (currentY == touchBlock.getTopPosition()){
//                    showLog("line move currentX="+currentX+" currentY="+currentY);
//                    showLog("line move targetX="+touchBlock.getLeftPosition()+" targetY="+touchBlock.getTopPosition());
//                    mCurTopPos = currentY;
//                    if (currentX < touchBlock.getLeftPosition()){
//                        moveToRight(currentX, touchBlock.getLeftPosition());
//                    }else if (currentX > touchBlock.getLeftPosition()){
//                        moveToLeft(currentX, touchBlock.getLeftPosition());
//                    }
//                }else{                // 纵向移动，则当前位置应该选择目标位置
//                    showLog("row move currentX="+currentX+" currentY="+currentY);
//                    showLog("row move targetX="+touchBlock.getLeftPosition()+" targetY="+touchBlock.getTopPosition());
//                    mCurLeftPos = touchBlock.getLeftPosition();
//                    if (currentY > touchBlock.getTopPosition()){
//                        moveToTop(currentY, touchBlock.getTopPosition());
//                    }
//                }
//
//            }
//        });

        startGame();

    }

    @OnClick(R.id.btn_restart)
    void clickRestart(){
        btn_restart.setVisibility(View.GONE);
        startGame();
    }

    /**
     *  开始游戏
     */
    private void startGame(){
        mGameView.reset();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 24; i++){
                    BlockInfo blockInfo = new BlockInfo();
                    blockInfo.setAnswer("hello");
                    blockInfo.setQuestion("hao are you");
                    blockInfo.setColor(Color.GRAY);
                    blockInfo.setHeight(200);
                    blockInfo.setWidth(200);
                    blockInfo.setId(i);
                    addBlock(blockInfo);
                    SystemClock.sleep(300);
                }
                // 设置用户坐标
                mUser = new User();
                List<BlockInfo> blockInfos = mGameView.getBlockInfos();
                if (!blockInfos.isEmpty()) {
                    mUser.setLeftPos(blockInfos.get(mCurBlockIndex).getLeftPosition());
                    mUser.setTopPos(blockInfos.get(mCurBlockIndex).getTopPosition());
                    showLog("init user leftPos "+blockInfos.get(mCurBlockIndex).getLeftPosition());
                    showLog("init user topPos "+blockInfos.get(mCurBlockIndex).getTopPosition());
                }
                // 显示问题
                showFirstQuestion();
            }
        }).start();
    }


    private void showFirstQuestion(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showQuestion();
                    }
                },1000);
            }
        });
    }

    @OnClick(R.id.iv_setting)
    void clickSetting(){
        Intent intent = new Intent(this, QuestionManagerActivity.class);
        startActivity(intent);
    }

    private void addBlock(final BlockInfo blockInfo){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mGameView.addBlock(blockInfo);
            }
        });
    }

    /** 移动人物
     * @param touchBlock
     */
    private void move(BlockInfo touchBlock){
        int currentX = mUser.getLeftPos();
        int currentY = mUser.getTopPos();
        // 如果横向移动，则判断是向左还是向右
        if (currentY == touchBlock.getTopPosition()){
            showLog("line move currentX="+currentX+" currentY="+currentY);
            showLog("line move targetX="+touchBlock.getLeftPosition()+" targetY="+touchBlock.getTopPosition());
            if (currentX < touchBlock.getLeftPosition()){
                moveToRight(touchBlock.getLeftPosition());
            }else if (currentX > touchBlock.getLeftPosition()){
                moveToLeft(touchBlock.getLeftPosition());
            }
        }else{                // 纵向移动，则当前位置应该选择目标位置
            showLog("row move currentX="+currentX+" currentY="+currentY);
            showLog("row move targetX="+touchBlock.getLeftPosition()+" targetY="+touchBlock.getTopPosition());
            if (currentY > touchBlock.getTopPosition()){
                moveToTop(touchBlock.getTopPosition());
            }
        }
    }

    /** 向右移动
     * @param targetX
     */
    private void moveToRight(final int targetX){
        new Thread(new Runnable() {
            @Override
            public void run() {
                int curLeftPos = mUser.getLeftPos();
                while (curLeftPos < targetX){
                    curLeftPos += 10;
                    mUser.setLeftPos(curLeftPos);
                    updateGameView();
                    SystemClock.sleep(200);
                }
                if (curLeftPos > targetX){         // 复位
                    mUser.setLeftPos(targetX);
                    updateGameView();
                }
                showQuestion();
            }
        }).start();
    }

    /** 向右移动
     * @param targetX
     */
    private void moveToLeft(final int targetX){
        new Thread(new Runnable() {
            @Override
            public void run() {
                int curLeftPos = mUser.getLeftPos();
                while (curLeftPos > targetX){
                    curLeftPos -= 10;
                    mUser.setLeftPos(curLeftPos);
                    updateGameView();
                    SystemClock.sleep(200);
                }
                if (curLeftPos < targetX){
                    mUser.setLeftPos(targetX);
                    updateGameView();
                }
                showQuestion();
            }
        }).start();
    }

    /** 向上移动
     * @param targetY
     */
    private void moveToTop(final int targetY){
        new Thread(new Runnable() {
            @Override
            public void run() {
                int curTopPos = mUser.getTopPos();
                while (curTopPos > targetY){
                    curTopPos -= 10;
                    showLog("moveToTop currentY="+curTopPos+" targetY="+targetY);
                    mUser.setTopPos(curTopPos);
                    updateGameView();
                    SystemClock.sleep(200);
                }
                if (curTopPos < targetY){
                    mUser.setTopPos(targetY);
                    updateGameView();
                }
                showQuestion();
            }
        }).start();
    }

    private void updateGameView(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mGameView.moveToBlock(mUser.getLeftPos(), mUser.getTopPos());
            }
        });
    }

    /**
     *  显示问题
     */
    private void showQuestion(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, AnswerQuestionDialog.class);
                startActivityForResult(intent, REQUEST_QUESTION);
            }
        });
    }

    /**
     *  移动到下个方块
     */
    private void toNextBlock() {
        if (mCurBlockIndex + 1 >= mGameView.getBlockInfos().size()){
            showLog("no any blocks");
            return;
        }
        mCurBlockIndex++;
        BlockInfo blockInfo = mGameView.getBlockInfos().get(mCurBlockIndex);
        blockInfo.setChange(true);
        move(blockInfo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;

        boolean correctAnswer = data.getBooleanExtra(AnswerQuestionDialog.DATA_ANSWER_RESULT, false);
        if (correctAnswer) {
            // 播放成功音效
            mSoundUtil.playSound(SoundUtil.SUCCESS);
            toNextBlock();
        }else{
            // 播放失败音效
            mSoundUtil.playSound(SoundUtil.FAILED);
            btn_restart.setVisibility(View.VISIBLE);
            mCurBlockIndex = 0;
            mGameView.gameOver();
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        toNextBlock();
        return super.onTouchEvent(event);
    }
}
