package com.sf.sfphotopicker.sflib.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by LC on 2016/6/27.
 */
public class SFBaseActivity extends AppCompatActivity implements ActivityLoad{
    public Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        context = this;
        initView();
        initData();
    }

    public void back(View v){
        finish();
    }
    public final <T extends View> T find(int id) {
        try {
            return (T)findViewById(id);
        } catch (ClassCastException ex) {
            throw ex;
        }
    }
    public final <T extends View> T find(View v,int id) {
        try {
            return (T)v.findViewById(id);
        } catch (ClassCastException ex) {
            throw ex;
        }
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }
}
