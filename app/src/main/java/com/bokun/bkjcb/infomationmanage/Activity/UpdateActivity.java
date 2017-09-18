package com.bokun.bkjcb.infomationmanage.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bokun.bkjcb.infomationmanage.Http.DefaultEvent;
import com.bokun.bkjcb.infomationmanage.R;

/**
 * Created by DengShuai on 2017/9/18.
 */

public class UpdateActivity extends BaseActivity {
    @Override
    protected void setView() {
        setContentView(R.layout.activity_update);
    }

    @Override
    protected void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle(name);
        toolbar.setNavigationIcon(R.drawable.back_aa);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void handlerEvent(DefaultEvent event) {

    }

    public static void comeIn(boolean flag, Context activity) {
        Intent intent = new Intent(activity, UpdateActivity.class);
        intent.putExtra("need", flag);
        activity.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
