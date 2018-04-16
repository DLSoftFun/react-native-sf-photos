package com.sf.sfphotopicker.multi_image_selector.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sf.sfphotopicker.R;
import com.sf.sfphotopicker.multi_image_selector.data.Image;
import com.sf.sfphotopicker.multi_image_selector.view.MatrixImageView;
import com.sf.sfphotopicker.sflib.adapter.SFBaseListAdapter;
import com.sf.sfphotopicker.sflib.adapter.SFItemHolder;

import java.util.List;

/**
 * Created by Administrator on 2018-04-02.
 */

public class PreviewImageItemAdapter extends FragmentPagerAdapter{
    List<Fragment> list;
    public PreviewImageItemAdapter(FragmentManager fm,List<Fragment> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
