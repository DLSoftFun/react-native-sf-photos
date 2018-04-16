package com.sf.sfphotopicker.multi_image_selector;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sf.sfphotopicker.R;
import com.sf.sfphotopicker.multi_image_selector.adapter.PreviewAdapter;
import com.sf.sfphotopicker.multi_image_selector.adapter.PreviewImageItemAdapter;
import com.sf.sfphotopicker.multi_image_selector.data.Image;
import com.sf.sfphotopicker.multi_image_selector.fragment.PreviewFragment;
import com.sf.sfphotopicker.multi_image_selector.utils.CommonUtils;
import com.sf.sfphotopicker.multi_image_selector.view.MatrixImageView;
import com.sf.sfphotopicker.sflib.activity.SFBaseActivity;
import com.sf.sfphotopicker.sflib.inter.OnItemClickListener;
import com.sf.sfphotopicker.sflib.utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018-04-02.
 */

public class PhotoPreviewActivity extends SFBaseActivity implements OnItemClickListener{
    List<Image> list;
    int position;
    ViewPager viewPager;
    LinearLayout l_choose;
    TextView tv_count;
    Button btn_send;
    PreviewImageItemAdapter adapter;
    List<Fragment> fragmentList;
    RecyclerView recyclerView;
    LinearLayoutManager manager;
    PreviewAdapter previewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_sf_preview);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void back(View v) {
        super.back(v);
    }

    @Override
    public void initView() {
        super.initView();
        viewPager = find(R.id.viewpager);
        l_choose = find(R.id.l_choose);
        tv_count = find(R.id.tv_count);
        btn_send = find(R.id.btn_send);
        recyclerView = find(R.id.recyclerview);
    }

    @Override
    public void initData() {
        super.initData();
        list = (List<Image>) getIntent().getSerializableExtra("data");
        position = getIntent().getExtras().getInt("position");
        fragmentList = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            PreviewFragment fragment = new PreviewFragment();
            fragment.setData(list.get(i));
            fragmentList.add(fragment);
        }
        adapter = new PreviewImageItemAdapter(getSupportFragmentManager(),fragmentList);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                checkChoose(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        manager = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(manager);
        previewAdapter = new PreviewAdapter(context);
        recyclerView.setAdapter(previewAdapter);
        viewPager.setCurrentItem(position,false);
        checkChoose(position);
    }
    public void checkChoose(int position){
        Image image = list.get(position);
        if (CommonUtils.is_in_select(image)){
            l_choose.setBackgroundResource(R.drawable.bg_image_select_dark_check);
            tv_count.setText(""+CommonUtils.get_item_position(image));
        }else{
            l_choose.setBackgroundResource(R.drawable.bg_image_select_dark_uncheck);
            tv_count.setText("");
        }
    }

    @Override
    public void onItemClick(View view, int position) {

    }
    public void choose(View v){
        Image image = list.get(viewPager.getCurrentItem());
        if (CommonUtils.get_select_count() < 9){
            if (!CommonUtils.is_in_select(image)){
                CommonUtils.add_select(image);
                l_choose.setBackgroundResource(R.drawable.bg_image_select_dark_check);
                tv_count.setText(""+CommonUtils.get_item_position(image));
            }else{
                CommonUtils.remove_select(image);
                l_choose.setBackgroundResource(R.drawable.bg_image_select_dark_uncheck);
                tv_count.setText("");
            }
            if (CommonUtils.get_select_count() > 0) {
                btn_send.setText("发送(" + CommonUtils.get_select_count() + ")");
            } else {
                btn_send.setText("发送");
            }
            previewAdapter.notifyDataSetChanged();
        }else{
            ToastUtils.showToast(context,"最多只能选择9张照片或视频");
        }
    }
    public void send(View v){
        setResult(RESULT_OK);
        finish();
    }
}
