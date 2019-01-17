package com.mhwang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mhwang.bean.Question;
import com.mhwang.englishjumpgame.R;

import java.util.List;

/**
 * Author : mhwang
 * Date : $Date$
 * Version : V1.0
 */
public class QuestionAdapter extends BaseAdapter {
    public static final String MODIFY = "modify";
    public static final String DELETE = "delete";

    private List<Question> mQuestions;
    private LayoutInflater mInflater;
    private OnItemOperationListener mListener = null;

    public QuestionAdapter(Context context, List<Question> questions){
        mQuestions = questions;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mQuestions.size();
    }

    @Override
    public Object getItem(int i) {
        return mQuestions.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Question question = mQuestions.get(i);
        ViewHolder holder;
        if (view == null){
            view = mInflater.inflate(R.layout.item_question, viewGroup, false);
            holder = new ViewHolder();
            holder.tv_text =  view.findViewById(R.id.tv_text);
            holder.tv_modify =  view.findViewById(R.id.tv_modify);
            holder.tv_delete =  view.findViewById(R.id.tv_delete);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        holder.tv_text.setText(question.getQuestion());
        holder.tv_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null){
                    mListener.onItemOperation(question.getNum(), MODIFY);
                }
            }
        });
        holder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null){
                    mListener.onItemOperation(question.getNum(), DELETE);
                }
            }
        });
        return view;
    }

    public void setOnItemOperationListener(OnItemOperationListener listener){
        mListener = listener;
    }

    private static class ViewHolder{
        TextView tv_text;
        TextView tv_modify;
        TextView tv_delete;
    }

    /**
     *  按钮操作监听
     */
    public interface OnItemOperationListener{
        void onItemOperation(int qNum, String operationType);
    }
}
