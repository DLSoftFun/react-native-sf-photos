package com.sf.sfphotopicker.sflib.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.sf.sfphotopicker.sflib.inter.OnItemClickListener;
import com.sf.sfphotopicker.sflib.inter.OnItemLongClickListener;

/**
 * Created by Administrator on 2017-01-20.
 */

public abstract class SFBaseListAdapter<I extends SFItemHolder> extends RecyclerView.Adapter<I>{
    public abstract I onCreateViewHolder(ViewGroup parent, int viewType);
    public abstract void onBindViewHolder(I holder, int position);

    public OnItemClickListener mOnItemClickListener;
    public OnItemLongClickListener mOnItemLongClickListener;
    public void setOnItemClickLitener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
    public void setOnItemLongClickLitener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }


}
