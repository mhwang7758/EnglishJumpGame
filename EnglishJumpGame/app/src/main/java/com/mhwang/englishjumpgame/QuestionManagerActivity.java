package com.mhwang.englishjumpgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.mhwang.adapter.QuestionAdapter;
import com.mhwang.bean.Answer;
import com.mhwang.bean.Question;
import com.mhwang.database.DatabaseUtil;
import com.mhwang.dialog.QuestionDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Author : mhwang
 * Date : $Date$
 * Version : V1.0
 */
public class QuestionManagerActivity extends Activity {
    private final static int REQUEST_ADD_QUESTION = 1000;
    private final static int REQUEST_EDIT_QUESTION = 1001;

    @BindView(R.id.lv_questions)
    ListView lv_questions;

    @BindView(R.id.btn_addQuestion)
    Button btn_addQuestion;

    private List<Question> mQuestions;
    private QuestionAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_question_manager);
        ButterKnife.bind(this);
        mQuestions = DatabaseUtil.getInstance(this).queryAllQuestions();
        mAdapter = new QuestionAdapter(this, mQuestions);
        lv_questions.setAdapter(mAdapter);

        mAdapter.setOnItemOperationListener(new QuestionAdapter.OnItemOperationListener() {
            @Override
            public void onItemOperation(int qNum, String operationType) {
                switch (operationType){
                    case QuestionAdapter.MODIFY:
                        Intent intent = new Intent(QuestionManagerActivity.this, QuestionDialog.class);
                        intent.putExtra(QuestionDialog.REQUEST_TYPE, QuestionDialog.EDIT);
                        intent.putExtra(QuestionDialog.REQUEST_NUM, qNum);
                        startActivityForResult(intent, REQUEST_EDIT_QUESTION);
                        break;
                    case QuestionAdapter.DELETE:
                        DatabaseUtil.getInstance(QuestionManagerActivity.this).updateQuestionState(qNum, true);
                        // 删除相关问题
                        for (Question question : mQuestions){
                            if (question.getNum() == qNum){
                                mQuestions.remove(question);
                                break;
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                        break;
                }
            }
        });
    }

    @OnClick(R.id.btn_addQuestion)
    void addQuestion(){
        Intent intent = new Intent(QuestionManagerActivity.this, QuestionDialog.class);
        intent.putExtra(QuestionDialog.REQUEST_TYPE, QuestionDialog.ADD);
        startActivityForResult(intent, REQUEST_ADD_QUESTION);
    }

    @OnClick(R.id.btn_resetQuestionState)
    void resetQuestionState(){
        DatabaseUtil databaseUtil = DatabaseUtil.getInstance(this);
        for (Question question : mQuestions){
            question.setAnswered(false);
            databaseUtil.updateQuestionAnswerState(question.getNum(), false);
        }
        mAdapter.notifyDataSetChanged();
        Toast.makeText(this, R.string.hint_modify_success, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        String question = data.getStringExtra(QuestionDialog.DATA_QUESTION);
        String answer = data.getStringExtra(QuestionDialog.DATA_ANSWER);
        String qNum = data.getStringExtra(QuestionDialog.DATA_QNUM);
        String type = data.getStringExtra(QuestionDialog.DATA_TYPE);
        boolean answered = data.getBooleanExtra(QuestionDialog.DATA_ANSWERED, false);
        Question newQuestion = new Question();
        newQuestion.setType(type);
        newQuestion.setQuestion(question);
        newQuestion.setNum(Integer.parseInt(qNum));
        newQuestion.setAnswered(answered);

        Answer newAnswer = new Answer();
        newAnswer.setNum(Integer.parseInt(qNum));
        newAnswer.setCorrect(true);
        newAnswer.setAnswer(answer);

        if (requestCode == REQUEST_ADD_QUESTION) {
            mQuestions.add(newQuestion);
            DatabaseUtil.getInstance(this).insertQuestion(newQuestion);
            DatabaseUtil.getInstance(this).insertAnswer(newAnswer);
        }else{
            for (Question q : mQuestions){
                if (q.getNum() == newQuestion.getNum()){
                    q.setAnswered(newQuestion.isAnswered());
                    q.setQuestion(newQuestion.getQuestion());
                    q.setType(newQuestion.getType());
                    // 更新问题
                    DatabaseUtil.getInstance(this).updateQuestion(newQuestion);
                    // 更新答案
                    DatabaseUtil.getInstance(this).updateAnswer(newAnswer);
                    break;
                }
            }
        }
        mAdapter.notifyDataSetChanged();
    }


}
