package com.bokun.bkjcb.infomationmanage.Fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.bokun.bkjcb.infomationmanage.Activity.LoginActivity;
import com.bokun.bkjcb.infomationmanage.Activity.UpdateActivity;
import com.bokun.bkjcb.infomationmanage.R;

/**
 * Created by DengShuai on 2017/9/15.
 */

public class ForthFragment extends BaseFragment implements View.OnClickListener {
    private static ForthFragment fragment;
    private SuperTextView update;
    private SuperTextView help;
    private TextView exit;
    private TextView userName;
    private SuperTextView userTel;
    private SuperTextView userPhone;
    private SuperTextView unitName;
    private SuperTextView unitAddress;

    public static ForthFragment newInstance() {
        if (fragment == null) {
            fragment = new ForthFragment();
        }
        return fragment;
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_four, null);
        userName = (TextView) view.findViewById(R.id.user_name);
        userTel = (SuperTextView) view.findViewById(R.id.user_tel_1);
        userPhone = (SuperTextView) view.findViewById(R.id.user_tel_other);
        unitName = (SuperTextView) view.findViewById(R.id.unit_name);
        unitAddress = (SuperTextView) view.findViewById(R.id.unit_address);
        exit = (TextView) view.findViewById(R.id.exit);
        update = (SuperTextView) view.findViewById(R.id.update);
        help = (SuperTextView) view.findViewById(R.id.help);
        return view;
    }

    @Override
    protected void setListener() {
        update.setOnClickListener(this);
        help.setOnClickListener(this);
        exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.update) {
            UpdateActivity.comeIn(false, getContext());
        } else if (v.getId() == R.id.exit) {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            activity.finish();
        } else {

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        userName.setText(activity.user.getUserName());
        userTel.setCenterString(activity.user.getTel());
        if (!userPhone.equals("")) {
            userPhone.setVisibility(View.VISIBLE);
            userPhone.setCenterString(activity.user.getTel_U());
        }
        unitName.setCenterString(activity.user.getDepartmentNameA());
        unitAddress.setCenterString(activity.user.getAddress());
        if (activity.hasNew) {
            update.setRightIcon(R.drawable.dot);
            update.setRightString("");
        }
    }
}
