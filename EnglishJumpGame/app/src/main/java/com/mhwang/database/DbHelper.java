package com.mhwang.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Author : mhwang
 * Date : $Date$
 * Version : V1.0
 */
public class DbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "jumpgame.db";
    private static final int VERSION = 1;

    public static final String TABLE_QUESTION = "t_question";
    public static final String TABLE_ANSWER = "t_answer";
    public static final String QUESTION_NUM = "qNum";
    public static final String QUESTION = "question";
    public static final String QUESTION_TYPE = "type";
    public static final String ANSWER = "answer";
    public static final String CORRECTED = "corrected";
    public static final String CREATE_TIME = "createTime";
    public static final String DELETE = "qDelete";
    public static final String ANSWERED = "answered";

    private static final String CREATE_QUESTION_TABLE = "CREATE TABLE "+TABLE_QUESTION+" ( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "qNum INTEGER UNIQUE, " +
            "question TEXT, " +
            "type TEXT, " +
            "qDelete INTEGER, " +
            "answered INTEGER, " +
            "createTime TEXT)";
    private static final String CREATE_ANSWER_TABLE = "CREATE TABLE "+TABLE_ANSWER+" ( " +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "qNum INTEGER, " +
            "answer TEXT, " +
            "createTime TEXT, " +
            "corrected INTEGER)";

    private Context mContext;
    protected DbHelper(Context context){
        super(context, DB_NAME, null, VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_QUESTION_TABLE);
        sqLiteDatabase.execSQL(CREATE_ANSWER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
