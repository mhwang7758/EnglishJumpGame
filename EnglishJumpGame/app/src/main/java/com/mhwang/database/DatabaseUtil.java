package com.mhwang.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mhwang.bean.Answer;
import com.mhwang.bean.Question;
import com.mhwang.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : mhwang
 * Date : $Date$
 * Version : V1.0
 */
public class DatabaseUtil {
    private static DatabaseUtil sDatabaseUtil = null;
    private SQLiteDatabase mDatabase;

    private static void showLog(String msg) {
        Log.d("DatabaseUtil-->", msg);
    }


    private DatabaseUtil(Context context) {
        mDatabase = new DbHelper(context).getReadableDatabase();
    }

    /**
     * 获取实例
     * @param context 上下文对象
     * @return DatabaseUtil对象
     */
    public static DatabaseUtil getInstance(Context context) {
        if (sDatabaseUtil == null) {
            synchronized (DatabaseUtil.class) {
                if (sDatabaseUtil == null) {
                    sDatabaseUtil = new DatabaseUtil(context.getApplicationContext());
                }
            }
        }
        return sDatabaseUtil;
    }

    /** 插入问题
     * @param question 要插入的问题
     * @return
     */
    public long insertQuestion(Question question){
        ContentValues values = new ContentValues();
        String date = DateUtil.getTime();
        values.put(DbHelper.QUESTION, question.getQuestion());
        values.put(DbHelper.QUESTION_NUM, question.getNum());
        values.put(DbHelper.CREATE_TIME, date);
        values.put(DbHelper.QUESTION_TYPE, question.getType());
        values.put(DbHelper.DELETE, question.isDelete() ? 1 : 0);
        values.put(DbHelper.ANSWERED, question.isAnswered() ? 1 : 0);
        return mDatabase.insert(DbHelper.TABLE_QUESTION, null, values);
    }

    /** 更新问题
     * @param question 要更新的问题
     * @return
     */
    public long updateQuestion(Question question){
        ContentValues values = new ContentValues();
        String date = DateUtil.getTime();
        values.put(DbHelper.QUESTION, question.getQuestion());
        values.put(DbHelper.CREATE_TIME, date);
        values.put(DbHelper.QUESTION_TYPE, question.getType());
        values.put(DbHelper.DELETE, question.isDelete() ? 1 : 0);
        values.put(DbHelper.ANSWERED, question.isAnswered() ? 1 : 0);
        return mDatabase.update(DbHelper.TABLE_QUESTION, values,
                DbHelper.QUESTION_NUM+"=?", new String[]{""+question.getNum()});
    }

    /** 更新问题状态
     * @param id
     * @param delete true，删除
     * @return
     */
    public long updateQuestionState(int id, boolean delete){
        ContentValues values = new ContentValues();
        values.put(DbHelper.DELETE, delete ? 1 : 0);
        return mDatabase.update(DbHelper.TABLE_QUESTION, values, DbHelper.QUESTION_NUM+"=?", new String[]{""+id});
    }

    /** 更新问题回答状态
     * @param id
     * @param answered true，删除
     * @return
     */
    public long updateQuestionAnswerState(int id, boolean answered){
        ContentValues values = new ContentValues();
        values.put(DbHelper.ANSWERED, answered ? 1 : 0);
        return mDatabase.update(DbHelper.TABLE_QUESTION, values, DbHelper.QUESTION_NUM+"=?", new String[]{""+id});
    }

    /** 查找所有问题
     * @return
     */
    public List<Question> queryAllQuestions(){
        List<Question> questions = new ArrayList<>();
        Cursor cursor = mDatabase.query(DbHelper.TABLE_QUESTION, null, DbHelper.DELETE+"=?", new String[]{"0"},
                null, null,null);
        while (cursor.moveToNext()){
            Question question = new Question();
            int qNum = cursor.getInt(cursor.getColumnIndex(DbHelper.QUESTION_NUM));
            int answered = cursor.getInt(cursor.getColumnIndex(DbHelper.ANSWERED));
            String text = cursor.getString(cursor.getColumnIndex(DbHelper.QUESTION));
            String date = cursor.getString(cursor.getColumnIndex(DbHelper.CREATE_TIME));
            String type = cursor.getString(cursor.getColumnIndex(DbHelper.QUESTION_TYPE));
            question.setNum(qNum);
            question.setQuestion(text);
            question.setCreateTime(date);
            question.setType(type);
            question.setAnswered(answered == 1);
            questions.add(question);
        }
        cursor.close();
        return questions;
    }

    /** 查找所有未回答过的问题
     * @return
     */
    public List<Question> queryAllUnanswerQuestions(){
        List<Question> questions = new ArrayList<>();
        Cursor cursor = mDatabase.query(DbHelper.TABLE_QUESTION, null,
                DbHelper.DELETE+"=? and "+DbHelper.ANSWERED+"=?", new String[]{"0","0"},
                null, null,null);
        while (cursor.moveToNext()){
            Question question = new Question();
            int qNum = cursor.getInt(cursor.getColumnIndex(DbHelper.QUESTION_NUM));
            String text = cursor.getString(cursor.getColumnIndex(DbHelper.QUESTION));
            String date = cursor.getString(cursor.getColumnIndex(DbHelper.CREATE_TIME));
            String type = cursor.getString(cursor.getColumnIndex(DbHelper.QUESTION_TYPE));
            question.setNum(qNum);
            question.setQuestion(text);
            question.setCreateTime(date);
            question.setType(type);
            question.setAnswered(false);
            questions.add(question);
        }
        cursor.close();
        return questions;
    }

    /** 查找所有删除的问题
     * @return
     */
    public List<Question> queryAllDeleteQuestions(){
        List<Question> questions = new ArrayList<>();
        Cursor cursor = mDatabase.query(DbHelper.TABLE_QUESTION, null, DbHelper.DELETE+"=?", new String[]{"1"},
                null, null,null);
        while (cursor.moveToNext()){
            Question question = new Question();
            int qNum = cursor.getInt(cursor.getColumnIndex(DbHelper.QUESTION_NUM));
            int answered = cursor.getInt(cursor.getColumnIndex(DbHelper.ANSWERED));
            String text = cursor.getString(cursor.getColumnIndex(DbHelper.QUESTION));
            String date = cursor.getString(cursor.getColumnIndex(DbHelper.CREATE_TIME));
            String type = cursor.getString(cursor.getColumnIndex(DbHelper.QUESTION_TYPE));
            question.setNum(qNum);
            question.setQuestion(text);
            question.setCreateTime(date);
            question.setDelete(true);
            question.setType(type);
            question.setAnswered(answered == 1);
            questions.add(question);

        }
        cursor.close();
        return questions;
    }

    /** 根据id查找问题
     * @param qNum
     * @return
     */
    public Question queryQuestionById(int qNum){
        Cursor cursor = mDatabase.query(DbHelper.TABLE_QUESTION, null, DbHelper.QUESTION_NUM+"=?",
                new String[]{""+qNum},null,null,null);
        Question question = null;
        while (cursor.moveToNext()){
            question = new Question();
            int answered = cursor.getInt(cursor.getColumnIndex(DbHelper.ANSWERED));
            String text = cursor.getString(cursor.getColumnIndex(DbHelper.QUESTION));
            String date = cursor.getString(cursor.getColumnIndex(DbHelper.CREATE_TIME));
            String type = cursor.getString(cursor.getColumnIndex(DbHelper.QUESTION_TYPE));
            question.setType(type);
            question.setNum(qNum);
            question.setQuestion(text);
            question.setAnswered(answered == 1);
            question.setCreateTime(date);
        }
        cursor.close();
        return question;
    }

    /** 插入答案
     * @param answer
     * @return
     */
    public long insertAnswer(Answer answer){
        ContentValues values = new ContentValues();
        String date = DateUtil.getTime();
        values.put(DbHelper.CREATE_TIME, date);
        values.put(DbHelper.QUESTION_NUM, answer.getNum());
        values.put(DbHelper.ANSWER, answer.getAnswer());
        values.put(DbHelper.CORRECTED, answer.isCorrect() ? 1 : 0);
        return mDatabase.insert(DbHelper.TABLE_ANSWER, null, values);
    }

    /** 更新答案
     * @param answer
     * @return
     */
    public long updateAnswer(Answer answer){
        ContentValues values = new ContentValues();
        String date = DateUtil.getTime();
        values.put(DbHelper.CREATE_TIME, date);
        values.put(DbHelper.QUESTION_NUM, answer.getNum());
        values.put(DbHelper.ANSWER, answer.getAnswer());
        values.put(DbHelper.CORRECTED, answer.isCorrect() ? 1 : 0);
        return mDatabase.update(DbHelper.TABLE_ANSWER, values,
                DbHelper.QUESTION_NUM+"=?", new String[]{""+answer.getNum()});
    }

    /** 根据问题编号查找答案
     * @param qNum
     * @return
     */
    public List<Answer> queryAllAnswerById(int qNum){
        List<Answer> answers = new ArrayList<>();
        Cursor cursor = mDatabase.query(DbHelper.TABLE_ANSWER, null, DbHelper.QUESTION_NUM+"=?",
                new String[]{""+qNum},null,null,null);
        while (cursor.moveToNext()){
            Answer answer = new Answer();
            String text = cursor.getString(cursor.getColumnIndex(DbHelper.ANSWER));
            String createTime = cursor.getString(cursor.getColumnIndex(DbHelper.CREATE_TIME));
            int correct = cursor.getInt(cursor.getColumnIndex(DbHelper.CORRECTED));
            answer.setAnswer(text);
            answer.setCorrect(correct == 1);
            answer.setCreateTime(createTime);
            answer.setNum(qNum);
            answers.add(answer);
        }
        cursor.close();
        return answers;
    }
}
