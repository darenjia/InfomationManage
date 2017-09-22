package com.bokun.bkjcb.infomationmanage.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bokun.bkjcb.infomationmanage.Http.DefaultEvent;
import com.bokun.bkjcb.infomationmanage.R;
import com.bokun.bkjcb.infomationmanage.Utils.Utils;

public class HelpActivity extends BaseActivity {


    private TextView textView;
    private TextView textView1;
    private ImageView imageView1, imageView2, imageView3, imageView4, imageView5, imageView6, imageView7;

    @Override
    protected void setView() {
        setContentView(R.layout.activity_help);
    }

    @Override
    protected void initView() {
        textView = (TextView) findViewById(R.id.text);
        textView1 = (TextView) findViewById(R.id.text1);
        imageView1 = (ImageView) findViewById(R.id.image_one);
        imageView2 = (ImageView) findViewById(R.id.image_two);
        imageView3 = (ImageView) findViewById(R.id.image_three);
        imageView4 = (ImageView) findViewById(R.id.image_four);
        imageView5 = (ImageView) findViewById(R.id.image_five);
        imageView6 = (ImageView) findViewById(R.id.image_six);
        imageView7 = (ImageView) findViewById(R.id.image_seven);
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
        imageView1.setImageDrawable(Utils.compressBitmap(getResources(), R.drawable.help1));
        imageView2.setImageDrawable(Utils.compressBitmap(getResources(), R.drawable.help2));
        imageView3.setImageDrawable(Utils.compressBitmap(getResources(), R.drawable.help3));
        imageView4.setImageDrawable(Utils.compressBitmap(getResources(), R.drawable.help4));
        imageView5.setImageDrawable(Utils.compressBitmap(getResources(), R.drawable.help5));
        imageView6.setImageDrawable(Utils.compressBitmap(getResources(), R.drawable.help6));
        imageView7.setImageDrawable(Utils.compressBitmap(getResources(), R.drawable.help7));
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
