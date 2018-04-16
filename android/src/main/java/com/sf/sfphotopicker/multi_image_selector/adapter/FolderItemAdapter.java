package com.sf.sfphotopicker.multi_image_selector.adapter;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.sf.sfphotopicker.R;
import com.sf.sfphotopicker.multi_image_selector.data.Folder;
import com.sf.sfphotopicker.multi_image_selector.data.Image;
import com.sf.sfphotopicker.multi_image_selector.utils.MediaUtils;
import com.sf.sfphotopicker.sflib.adapter.SFBaseListAdapter;
import com.sf.sfphotopicker.sflib.adapter.SFItemHolder;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-03-02.
 */

public class FolderItemAdapter extends SFBaseListAdapter<FolderItemAdapter.ItemHolder>{
    Context context;
    List<Folder> list;
    final int mGridWidth;

    public FolderItemAdapter(Context context) {
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
    }
    public void addData(List<Folder> data){
        this.list.addAll(data);
        notifyDataSetChanged();
    }
    public Folder getItem(int position){
        return list.get(position);
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemHolder holder = new ItemHolder(LayoutInflater.from(context).inflate(R.layout.item_folder,null));
        holder.setClickListener(mOnItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.setPosition(position);
        Folder folder = list.get(position);
        if (folder.isVideo){
            holder.setVideoImage(folder.cover);
            holder.tv_name.setText(list.get(position).name+"("+list.get(position).medias.size()+")");
        }else{
            holder.setImage(folder.cover);
            holder.tv_name.setText(list.get(position).name+"("+list.get(position).medias.size()+")");
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemHolder extends SFItemHolder{
        ImageView iv_image;
        TextView tv_name;
        public ItemHolder(View itemView) {
            super(itemView);
            iv_image = (ImageView) itemView.findViewById(R.id.iv_image);
            tv_name = (TextView)itemView.findViewById(R.id.tv_name);
        }
        public void setVideoImage(Image data){
            if(data!=null && data.getPath() !=null){
                iv_image.setImageBitmap(MediaUtils.getVideoThumbnail(data.getPath()));
            }
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
