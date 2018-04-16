package com.sf.sfphotopicker.multi_image_selector.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sf.sfphotopicker.R;
import com.sf.sfphotopicker.multi_image_selector.data.Image;
import com.sf.sfphotopicker.multi_image_selector.view.MatrixImageView;

import java.io.File;

/**
 * Created by Administrator on 2018-04-02.
 */

public class PreviewFragment extends Fragment {
    MatrixImageView imageView;
    Image image;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.item_photo_preview,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView = (MatrixImageView)view.findViewById(R.id.imageview);
        imageView.setImageURI(Uri.fromFile(new File(image.getPath())));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    public void setData(Image image){
        this.image = image;
    }
}
