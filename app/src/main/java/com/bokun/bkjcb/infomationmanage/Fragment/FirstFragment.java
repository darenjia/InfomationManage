package com.bokun.bkjcb.infomationmanage.Fragment;

import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.bokun.bkjcb.infomationmanage.R;

/**
 * Created by DengShuai on 2017/9/14.
 */

public class FirstFragment extends BaseFragment {

    private FrameLayout layout;
    private static FirstFragment fragment;

    public static FirstFragment newInstance() {
        if (fragment == null) {
            fragment = new FirstFragment();
        }
        return fragment;
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_main, null);
        layout = (FrameLayout) view.findViewById(R.id.main_layout);
        return view;
    }

    private void init() {
        PartOneFragment fragment = (PartOneFragment) PartOneFragment.newInstance().setActivity(activity);
        fragment.setLayoutAndManager(layout,activity.getSupportFragmentManager());
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.main_layout, fragment);
        transaction.commit();
    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onStart() {
        super.onStart();
        if (layout.getChildCount()==0){
            init();
        }
    }
}
