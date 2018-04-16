package com.sf.sfphotopicker.multi_image_selector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sf.sfphotopicker.R;
import com.sf.sfphotopicker.multi_image_selector.adapter.ImageItemAdapter;
import com.sf.sfphotopicker.multi_image_selector.data.Image;
import com.sf.sfphotopicker.multi_image_selector.utils.CommonUtils;
import com.sf.sfphotopicker.sflib.activity.SFBaseActivity;
import com.sf.sfphotopicker.sflib.inter.OnItemClickListener;

import java.util.List;

/**
 * Created by Administrator on 2017-03-03.
 */

public class SelectImageActivity extends SFBaseActivity implements OnItemClickListener {
    private final int SELECT_IMAGE_CLOSE_CODE = 201;
    RecyclerView recyclerView;
    GridLayoutManager manager;
    ImageItemAdapter adapter;
    TextView tv_title;
    Button btn_send;
    boolean isSingle;
    LinearLayout l_send;
    boolean isVideo;
    int max_number = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_sf_selectimage);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        super.initView();
        isSingle = getIntent().getBooleanExtra("single", false);
        isVideo = getIntent().getBooleanExtra("isvideo", false);
        max_number = getIntent().getIntExtra("number",9);
//        if (isVideo){
//            isSingle = true;
//        }
        manager = new GridLayoutManager(context, 3);
        recyclerView = find(R.id.recyclerview);
        l_send = find(R.id.l_send);
        recyclerView.setLayoutManager(manager);
        adapter = new ImageItemAdapter(context, isSingle, isVideo,max_number);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickLitener(this);
        tv_title = find(R.id.tv_title);
        btn_send = find(R.id.btn_send);
        if (isSingle) {
            l_send.setVisibility(View.GONE);
        }
    }

    @Override
    public void initData() {
        super.initData();
        List<Image> list = (List<Image>) getIntent().getSerializableExtra("data");
        adapter.addData(list);
        tv_title.setText(getIntent().getExtras().getString("title"));
        if (CommonUtils.get_select_count() > 0) {
            btn_send.setText("发送(" + CommonUtils.get_select_count() + ")");
        }
    }

    public void cancel(View v) {
        setResult(SELECT_IMAGE_CLOSE_CODE);
        finish();
    }

    public void send(View v) {
        setResult(RESULT_OK);
        finish();
    }

    public void updateBtn() {
        if (CommonUtils.get_select_count() > 0) {
            btn_send.setText("发送(" + CommonUtils.get_select_count() + ")");
        } else {
            btn_send.setText("发送");
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        if (isSingle) {
            CommonUtils.add_select(adapter.getItem(position));
            setResult(RESULT_OK);
            finish();
        }else{
            Intent intent = new Intent(context,PhotoPreviewActivity.class);
            intent.putExtra("data",getIntent().getSerializableExtra("data"));
            intent.putExtra("position",position);
            startActivityForResult(intent,200);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 200){
            this.updateBtn();
            adapter.notifyDataSetChanged();
            if(resultCode == RESULT_OK){
                setResult(RESULT_OK);
                finish();
            }
        }
    }
}
