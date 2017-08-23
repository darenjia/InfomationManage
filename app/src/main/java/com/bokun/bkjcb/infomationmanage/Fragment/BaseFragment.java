package com.bokun.bkjcb.infomationmanage.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by DengShuai on 2017/8/22.
 */

public abstract class BaseFragment extends Fragment {
    protected View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = initView(inflater, container);
            setListener();
        }
        return rootView;
    }

    protected abstract View initView(LayoutInflater inflater, ViewGroup container);

    protected abstract void setListener();
}
