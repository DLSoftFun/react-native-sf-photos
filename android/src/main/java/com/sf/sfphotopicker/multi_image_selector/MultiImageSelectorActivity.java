package com.sf.sfphotopicker.multi_image_selector;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sf.sfphotopicker.R;
import com.sf.sfphotopicker.multi_image_selector.adapter.FolderItemAdapter;
import com.sf.sfphotopicker.multi_image_selector.data.Folder;
import com.sf.sfphotopicker.multi_image_selector.data.Image;
import com.sf.sfphotopicker.multi_image_selector.inter.CursorCallback;
import com.sf.sfphotopicker.multi_image_selector.utils.CommonUtils;
import com.sf.sfphotopicker.multi_image_selector.utils.MediaUtils;
import com.sf.sfphotopicker.sflib.activity.SFBaseActivity;
import com.sf.sfphotopicker.sflib.inter.OnItemClickListener;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Administrator on 2017-03-02.
 */

public class MultiImageSelectorActivity extends SFBaseActivity implements CursorCallback,OnItemClickListener{
    private final int SELECT_IMAGE_CODE = 200;
    private final int SELECT_IMAGE_CLOSE_CODE = 201;
    RecyclerView recyclerView;
    LinearLayoutManager manager;
    FolderItemAdapter adapter;
    boolean isSingle = false;
    boolean isCrop = false;
    int type = 2;
    int max_number = 9;
    List<Image> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_sf_image_list);
        super.onCreate(savedInstanceState);
    }
    private String getTmpDir(Activity activity) {
        String tmpDir = activity.getCacheDir() + "/react-native-sf-photos";
        Boolean created = new File(tmpDir).mkdir();

        return tmpDir;
    }

    @Override
    public void initView() {
        super.initView();
        manager = new LinearLayoutManager(context);
        recyclerView = find(R.id.recyclerview);
        recyclerView.setLayoutManager(manager);
        adapter = new FolderItemAdapter(context);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickLitener(this);
    }

    @Override
    public void initData() {
        super.initData();
        CommonUtils.init_select();
        isSingle = getIntent().getBooleanExtra("isSingle",false);
        type = getIntent().getIntExtra("type",2);
        max_number = getIntent().getIntExtra("number",9);
        isCrop = getIntent().getBooleanExtra("isCrop",false);
        if(!isSingle){
            isCrop = false;
        }
        Serializable data = getIntent().getSerializableExtra("data");
        MediaUtils.getImageAndVideoData(context,this,type);
        if (data!=null){
            list = (List<Image>)data;
        }
        if (list == null){
            list = new ArrayList<>();
        }
        CommonUtils.clear_selcet();
        for (int i=0;i<list.size();i++){
            CommonUtils.add_select(list.get(i));
        }
    }
    public void cancel(View v){
        finish();
    }

    @Override
    public void onFinishCursor(List<?> list) {
        adapter.addData((List<Folder>) list);
    }

    @Override
    public void onItemClick(View view, int position) {
        Folder folder = adapter.getItem(position);
        Intent intent = new Intent(context,SelectImageActivity.class);
        intent.putExtra("data",(Serializable)folder.medias);
        intent.putExtra("title",folder.name);
        intent.putExtra("single",isSingle);
        intent.putExtra("number",max_number);
        intent.putExtra("isvideo",folder.isVideo);
        startActivityForResult(intent,SELECT_IMAGE_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE_CODE && resultCode == SELECT_IMAGE_CLOSE_CODE){
            finish();
        }else if (requestCode == SELECT_IMAGE_CODE && resultCode == RESULT_OK){
            if(isCrop){
                List<Image> list = CommonUtils.getSelect_images();
                Image img = list.get(0);
                startCropping(this,Uri.fromFile(new File(img.getPath())));
            }else{
                Intent intent = new Intent();
                intent.putExtra("type",0);
                intent.putExtra("data",(Serializable)CommonUtils.getSelect_images());
                setResult(RESULT_OK,intent);
                finish();
                CommonUtils.clear_selcet();
            }
        }else if (requestCode == UCrop.REQUEST_CROP) {
            if (data != null) {
                final Uri resultUri = UCrop.getOutput(data);
                if (resultUri != null) {
                    Intent intent = new Intent();
                    intent.putExtra("data",resultUri);
                    intent.putExtra("type",1);
                    setResult(RESULT_OK,intent);
                    finish();
                } else {
                    finish();
                }
            } else {
               finish();
            }
        }
    }
    private void configureCropperColors(UCrop.Options options) {
        int activeWidgetColor = Color.parseColor("#424242");
        int toolbarColor = Color.parseColor("#424242");
        int statusBarColor = Color.parseColor("#424242");
        options.setToolbarColor(toolbarColor);
        options.setStatusBarColor(statusBarColor);
        if (activeWidgetColor == Color.parseColor("#424242")) {
            /*
            Default tint is grey => use a more flashy color that stands out more as the call to action
            Here we use 'Light Blue 500' from https://material.google.com/style/color.html#color-color-palette
            */
            options.setActiveWidgetColor(Color.parseColor("#03A9F4"));
        } else {
            //If they pass a custom tint color in, we use this for everything
            options.setActiveWidgetColor(activeWidgetColor);
        }
    }
    private void startCropping(Activity activity, Uri uri) {
        UCrop.Options options = new UCrop.Options();
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setCompressionQuality(100);
        options.setCircleDimmedLayer(false);
        options.setShowCropGrid(true);
        options.setHideBottomControls(false);
//        if (enableRotationGesture) {
            // UCropActivity.ALL = enable both rotation & scaling
            options.setAllowedGestures(
                    UCropActivity.ALL, // When 'scale'-tab active
                    UCropActivity.ALL, // When 'rotate'-tab active
                    UCropActivity.ALL  // When 'aspect ratio'-tab active
            );
//        }
        configureCropperColors(options);

        UCrop.of(uri, Uri.fromFile(new File(this.getTmpDir(activity), UUID.randomUUID().toString() + ".jpg")))
                .withMaxResultSize(200, 200)
                .withAspectRatio(200, 200)
                .withOptions(options)
                .start(activity);
    }
}
