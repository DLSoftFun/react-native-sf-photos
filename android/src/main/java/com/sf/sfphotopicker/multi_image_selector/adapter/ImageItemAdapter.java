package com.sf.sfphotopicker.multi_image_selector.adapter;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sf.sfphotopicker.R;
import com.sf.sfphotopicker.multi_image_selector.SelectImageActivity;
import com.sf.sfphotopicker.multi_image_selector.data.Image;
import com.sf.sfphotopicker.multi_image_selector.utils.CommonUtils;
import com.sf.sfphotopicker.multi_image_selector.utils.MediaUtils;
import com.sf.sfphotopicker.multi_image_selector.view.SquaredImageView;
import com.sf.sfphotopicker.sflib.adapter.SFBaseListAdapter;
import com.sf.sfphotopicker.sflib.adapter.SFItemHolder;
import com.sf.sfphotopicker.sflib.utils.ToastUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-03-02.
 */

public class ImageItemAdapter extends SFBaseListAdapter<ImageItemAdapter.ItemHolder>{
    Context context;
    List<Image> list;
    final int mGridWidth;
    boolean isSingle;
    boolean isVideo;
    int max_number = 9;

    public ImageItemAdapter(Context context,boolean isSingle,boolean isVideo,int max_number) {
        this.context = context;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point size = new Point();
            wm.getDefaultDisplay().getSize(size);
            width = size.x;
        }else{
            width = wm.getDefaultDisplay().getWidth();
        }
        mGridWidth = width / 3;
        list = new ArrayList<>();
        this.isSingle = isSingle;
        this.isVideo = isVideo;
        this.max_number = max_number;
    }
    public void addData(List<Image> data){
        this.list.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemHolder holder = new ItemHolder(LayoutInflater.from(context).inflate(R.layout.item_photo_image,null));
        holder.setClickListener(mOnItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ItemHolder holder, final int position) {
        holder.setPosition(position);
        final Image image = list.get(position);
        if (isVideo){
            holder.setVideoImage(image);
        }else{
            holder.setImage(image);
        }
        if (CommonUtils.is_in_select(image)){
            holder.l_choose.setBackgroundResource(R.drawable.bg_image_select_check);
            holder.tv_count.setText(""+CommonUtils.get_item_position(image));
        }else{
            holder.l_choose.setBackgroundResource(R.drawable.bg_image_select_uncheck);
            holder.tv_count.setText("");
        }
        if (image.getPath().contains(".gif")){
            holder.tv_type.setVisibility(View.VISIBLE);
        }else{
            holder.tv_type.setVisibility(View.GONE);
        }

        holder.l_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonUtils.get_select_count() < max_number){
                    if (!CommonUtils.is_in_select(image)){
                        CommonUtils.add_select(image);
                    }else{
                        CommonUtils.remove_select(image);
                    }
                    notifyItemChanged(position);
                    ((SelectImageActivity)context).updateBtn();
                }else{
                    ToastUtils.showToast(context,"最多只能选择"+max_number+"张照片或视频");
                }
            }
        });
        if (isSingle){
            holder.l_choose.setVisibility(View.GONE);
        }else{
            holder.l_choose.setVisibility(View.VISIBLE);
        }
    }
    public Image getItem(int position){
        return list.get(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemHolder extends SFItemHolder{
        SquaredImageView iv_image;
        LinearLayout l_choose;
        TextView tv_count;
        TextView tv_type;
        public ItemHolder(View itemView) {
            super(itemView);
            iv_image = find(itemView,R.id.image);
            l_choose = find(itemView,R.id.l_choose);
            tv_count = find(itemView,R.id.tv_count);
            tv_type = find(itemView,R.id.tv_type);

        }
        public void setVideoImage(Image data){
            iv_image.setImageBitmap(MediaUtils.getVideoThumbnail(data.getPath()));
        }
        public void setImage(Image data){
            File imageFile = new File(data.getPath());
            if (imageFile.exists()) {
                // 显示图片
                Picasso.with(context)
                        .load(imageFile)
                        .placeholder(R.drawable.default_error)
                        .tag("SF")
                        .resize(mGridWidth, mGridWidth)
                        .centerCrop()
                        .into(iv_image);
            }else{
                iv_image.setImageResource(R.drawable.default_error);
            }
        }
    }
}
