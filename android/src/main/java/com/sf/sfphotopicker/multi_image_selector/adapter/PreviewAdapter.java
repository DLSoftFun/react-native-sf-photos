package com.sf.sfphotopicker.multi_image_selector.adapter;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.sf.sfphotopicker.R;
import com.sf.sfphotopicker.multi_image_selector.data.Image;
import com.sf.sfphotopicker.multi_image_selector.utils.CommonUtils;
import com.sf.sfphotopicker.sflib.adapter.SFBaseListAdapter;
import com.sf.sfphotopicker.sflib.adapter.SFItemHolder;
import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by Administrator on 2018-04-02.
 */

public class PreviewAdapter extends SFBaseListAdapter<PreviewAdapter.ItemHoler> {
    Context context;

    public PreviewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ItemHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemHoler itemHoler = new ItemHoler(LayoutInflater.from(context).inflate(R.layout.item_photo_preview_image,null));
        itemHoler.setClickListener(mOnItemClickListener);
        return itemHoler;
    }

    @Override
    public void onBindViewHolder(ItemHoler holder, int position) {
        holder.setPosition(position);
        final Image image = CommonUtils.getSelect_images().get(position);
        holder.setImage(image);
    }

    @Override
    public int getItemCount() {
        return CommonUtils.get_select_count();
    }

    class ItemHoler extends SFItemHolder{
        ImageView imageView;
        public ItemHoler(View itemView) {
            super(itemView);
            imageView = find(itemView, R.id.imageview);
        }
        public void setImage(Image data){
            File imageFile = new File(data.getPath());
            if (imageFile.exists()) {
                // 显示图片
                Picasso.with(context)
                        .load(imageFile)
                        .placeholder(R.drawable.default_error)
                        .tag("SF")
                        .resize(50, 50)
                        .centerCrop()
                        .into(imageView);
            }else{
                imageView.setImageResource(R.drawable.default_error);
            }
        }
    }
}
