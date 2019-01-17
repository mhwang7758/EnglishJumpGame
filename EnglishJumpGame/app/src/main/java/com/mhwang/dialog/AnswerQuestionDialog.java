package com.mhwang.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.mhwang.bean.Answer;
import com.mhwang.bean.Question;
import com.mhwang.database.DatabaseUtil;
import com.mhwang.englishjumpgame.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Author : mhwang
 * Date : $Date$
 * Version : V1.0
 */
public class AnswerQuestionDialog extends Activity {

    @BindView(R.id.tv_qNum)
    TextView tv_qNum;
    @BindView(R.id.tv_question)
    TextView tv_question;
    @BindView(R.id.tv_noAnswer)
    TextView tv_noAnswer;
    @BindView(R.id.et_answer)
    EditText et_answer;

    private int mRemainTimes = 3;
    private Question mQuestion;

    public static final String DATA_ANSWER_RESULT = "answerResult";

    private void showLog(String s){
        Log.d("AnswerQuestionDialog-->",s);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_answer_question);
        ButterKnife.bind(this);
        mQuestion = getRandomQuestion();
        if (mQuestion == null){
            tv_question.setText(R.string.hint_no_question);
        }else{
            tv_qNum.setText("问题编号："+Integer.toString(mQuestion.getNum()));
            tv_question.setText("题目："+mQuestion.getQuestion());
        }
    }

    @OnClick(R.id.btn_dialog_sure)
    void clickSure(){
        if (mQuestion == null){
            return;
        }
        String userAnswer = et_answer.getText().toString().trim();
        if (TextUtils.isEmpty(userAnswer)){
            tv_noAnswer.setText(R.string.hint_answer);
            tv_noAnswer.setVisibility(View.VISIBLE);
            return;
        }
        // 找到该问题答案
        List<Answer> answers = DatabaseUtil.getInstance(this)
                .queryAllAnswerById(mQuestion.getNum());
        String correctAnswer = null;
        for (Answer answer : answers){
            if (answer.isCorrect()){
                correctAnswer = answer.getAnswer();
                break;
            }
        }
        if (TextUtils.isEmpty(correctAnswer)){
            tv_noAnswer.setVisibility(View.VISIBLE);
            return;
        }
        Intent data = new Intent();
        if (!userAnswer.toUpperCase().equals(correctAnswer.toUpperCase())){
            mRemainTimes--;
            String hint = getString(R.string.hint_wrong_answer) + mRemainTimes;
            tv_noAnswer.setText(hint);
            tv_noAnswer.setVisibility(View.VISIBLE);
            // 如果没有可回答次数，则返回
            if (mRemainTimes <= 0){
                data.putExtra(DATA_ANSWER_RESULT, false);
                setResult(RESULT_OK, data);
                finish();
            }
            return;
        }
        data.putExtra(DATA_ANSWER_RESULT, true);
        DatabaseUtil.getInstance(this).updateQuestionAnswerState(mQuestion.getNum(), true);
        setResult(RESULT_OK, data);
        finish();
    }

    @OnClick(R.id.btn_dialog_cancel)
    void clickCancel(){
        finish();
    }

    /** 随机获取一个问题
     * @return
     */
    private Question getRandomQuestion(){
        List<Question> questions = DatabaseUtil.getInstance(this).queryAllUnanswerQuestions();
        if (questions.isEmpty()) return null;
        if (questions.size() == 1){
            return questions.get(0);
        }
        int i = questions.size();
        while (i >= questions.size()) {
            i = (int) (Math.random() * (questions.size() + 1));
        }
        showLog("random question index is "+i);
        return questions.get(i);
    }
}
