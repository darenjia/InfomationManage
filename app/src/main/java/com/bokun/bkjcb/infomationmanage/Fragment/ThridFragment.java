package com.bokun.bkjcb.infomationmanage.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bokun.bkjcb.infomationmanage.R;

/**
 * Created by DengShuai on 2017/8/22.
 */

public class ThridFragment extends BaseFragment {
    public static ThridFragment fragment;

    public static ThridFragment newInstance() {
        if (fragment == null) {
            fragment = new ThridFragment();
        }
        return fragment;
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_one, null);
    }

    @Override
    protected void setListener() {

    }
}
