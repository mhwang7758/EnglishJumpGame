package com.mhwang.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.mhwang.bean.Answer;
import com.mhwang.bean.Question;
import com.mhwang.constant.QuestionType;
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
public class QuestionDialog extends Activity {
    @BindView(R.id.et_question)
    EditText et_question;
    @BindView(R.id.et_answer)
    EditText et_answer;
    @BindView(R.id.tv_warning)
    TextView tv_warning;
    @BindView(R.id.et_num)
    TextView et_num;
    @BindView(R.id.cb_answered)
    CheckBox cb_answered;
    @BindView(R.id.tv_hintAnswered)
    TextView tv_hintAnswered;

    public static final String DATA_QUESTION = "question";
    public static final String DATA_ANSWER = "answer";
    public static final String DATA_TYPE = "type";
    public static final String DATA_QNUM = "num";
    public static final String DATA_ANSWERED = "answered";

    public static final String REQUEST_TYPE = "requestType";
    public static final String REQUEST_NUM = "requestNum";
    public static final String EDIT = "edit";
    public static final String ADD = "add";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_question);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String requestType = intent.getStringExtra(REQUEST_TYPE);
        // 如果是编辑状态，则通过问题编号查找并显示
        if (EDIT.equals(requestType)){
            int qNum = intent.getIntExtra(REQUEST_NUM, -1);
            showQuestion(qNum);
        }else{
            tv_hintAnswered.setVisibility(View.GONE);
            cb_answered.setVisibility(View.GONE);
        }
    }

    /** 显示问题
     * @param qNum 问题编号
     */
    private void showQuestion(int qNum) {
        Question question = DatabaseUtil.getInstance(this)
                .queryQuestionById(qNum);
        if (question == null){
            tv_warning.setVisibility(View.VISIBLE);
            tv_warning.setText(R.string.hint_find_no_question);
            return;
        }
        String sNum = ""+question.getNum();
        et_num.setText(sNum);
        et_num.setEnabled(false);
        cb_answered.setChecked(question.isAnswered());
        et_question.setText(question.getQuestion());
        List<Answer> answers = DatabaseUtil.getInstance(this).queryAllAnswerById(qNum);
        if (!answers.isEmpty()){
            et_answer.setText(answers.get(0).getAnswer());
        }
    }

    @OnClick(R.id.btn_dialog_sure)
    void clickSure(){
        Intent data = new Intent();
        String question = et_question.getText().toString().trim();
        if (TextUtils.isEmpty(question)){
            tv_warning.setVisibility(View.VISIBLE);
            tv_warning.setText(R.string.hint_question);
            return;
        }
        String answer = et_answer.getText().toString().trim();
        if (TextUtils.isEmpty(answer)){
            tv_warning.setVisibility(View.VISIBLE);
            tv_warning.setText(R.string.hint_answer);
            return;
        }
        String qNum = et_num.getText().toString().trim();
        if (TextUtils.isEmpty(qNum)){
            tv_warning.setVisibility(View.VISIBLE);
            tv_warning.setText(R.string.hint_question_num);
            return;
        }
        boolean answered = cb_answered.isChecked();
        data.putExtra(DATA_QUESTION, question);
        data.putExtra(DATA_ANSWER, answer);
        data.putExtra(DATA_TYPE, QuestionType.QUESTION_ANSWER);
        data.putExtra(DATA_QNUM,qNum);
        data.putExtra(DATA_ANSWERED,answered);
        setResult(RESULT_OK,data);
        finish();
    }

    @OnClick(R.id.btn_dialog_cancel)
    void clickCancel(){
        finish();
    }
}
