package com.bokun.bkjcb.infomationmanage.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bokun.bkjcb.infomationmanage.R;

/**
 * Created by DengShuai on 2017/9/15.
 */

public class ForthFragment extends BaseFragment {
    private static ForthFragment fragment;

    public static ForthFragment newInstance() {
        if (fragment == null) {
            fragment = new ForthFragment();
        }
        return fragment;
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_four, null);
        return view;
    }

    @Override
    protected void setListener() {

    }
}
