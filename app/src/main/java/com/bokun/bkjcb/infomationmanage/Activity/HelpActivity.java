package com.bokun.bkjcb.infomationmanage.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.bokun.bkjcb.infomationmanage.Http.DefaultEvent;
import com.bokun.bkjcb.infomationmanage.R;

public class HelpActivity extends BaseActivity {


    private TextView textView;
    private TextView textView1;

    @Override
    protected void setView() {
        setContentView(R.layout.activity_help);
    }

    @Override
    protected void initView() {
        textView = (TextView) findViewById(R.id.text);
        textView1 = (TextView) findViewById(R.id.text1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAppSettings();
            }
        });
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAppSettings();
            }
        });

    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void handlerEvent(DefaultEvent event) {

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public static void comeIn(Context activity) {
        Intent intent = new Intent(activity, HelpActivity.class);
        activity.startActivity(intent);
    }
}
