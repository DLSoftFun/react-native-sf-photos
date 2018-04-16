package com.sf.sfphotopicker.sflib.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sf.sfphotopicker.sflib.inter.OnItemClickListener;
import com.sf.sfphotopicker.sflib.inter.OnItemLongClickListener;

/**
 * Created by Administrator on 2017-03-01.
 */

public class SFItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
    public int position;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;


    public SFItemHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }
    public void setPosition(int position){
        this.position = position;
    }
    public void setClickListener(OnItemClickListener listener){
        this.mOnItemClickListener = listener;
    }
    public void setLongClickListener(OnItemLongClickListener listener){
        this.mOnItemLongClickListener = listener;
    }


    @Override
    public void onClick(View v) {
        if (mOnItemClickListener!=null){
            mOnItemClickListener.onItemClick(v,position);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mOnItemLongClickListener!=null){
            mOnItemLongClickListener.onItemLongClick(v,position);
        }
        return true;
    }
    public final <T extends View> T find(View v, int id) {
        try {
            return (T)v.findViewById(id);
        } catch (ClassCastException ex) {
            throw ex;
        }
    }
}
