package com.bokun.bkjcb.infomationmanage.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.bokun.bkjcb.infomationmanage.Adapter.UpdateAdapter;
import com.bokun.bkjcb.infomationmanage.Http.DefaultEvent;
import com.bokun.bkjcb.infomationmanage.Http.HttpManager;
import com.bokun.bkjcb.infomationmanage.Http.HttpRequestVo;
import com.bokun.bkjcb.infomationmanage.Http.RequestListener;
import com.bokun.bkjcb.infomationmanage.R;
import com.bokun.bkjcb.infomationmanage.Utils.NetUtils;
import com.bokun.bkjcb.infomationmanage.Utils.SPUtils;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by DengShuai on 2017/9/18.
 */

public class UpdateActivity extends BaseActivity implements RequestListener {

    private boolean updateNow;
    private boolean flag = false;
    private Button btn;
    private RecyclerView recyclerView;
    private ArrayList<String> strings;
    private UpdateAdapter adapter;
    private HttpManager manager;
    private AVLoadingIndicatorView indicatorView;

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

        btn = (Button) findViewById(R.id.update_btn);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_update);
        indicatorView = (AVLoadingIndicatorView) findViewById(R.id.update_progress);
    }

    @Override
    protected void setListener() {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag) {
                    refresh();
                } else {
                    checkUpdate();
                }
            }
        });
    }

    private void refresh() {
    }

    @Override
    protected void loadData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        strings = new ArrayList<>();
        strings.add("当前数据版本:1.0");
        strings.add("上次检查时间:"+ SPUtils.get(this,"Time","（无）"));
        adapter = new UpdateAdapter(R.layout.update_view, strings);
        recyclerView.setAdapter(adapter);
        updateNow = getIntent().getBooleanExtra("need", false);
        if (updateNow) {
            btn.setVisibility(View.GONE);
            indicatorView.setVisibility(View.VISIBLE);
            adapter.addData("更新中");
            adapter.addData("正在联网获取最新数据版本");
            adapter.addData("请稍等");
        }
    }

    @Override
    protected void handlerEvent(DefaultEvent event) {
        if (event.getState_code() == 0) {
            adapter.addData("检查到有新版本数据，请立即更新");
            btn.setText("立即更新");
            flag = true;
            btn.setVisibility(View.VISIBLE);
            indicatorView.setVisibility(View.GONE);
        }
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

    private String getDate() {
        Date d = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(d);
    }

    private void checkUpdate() {
        if (!NetUtils.isConnected(this)) {
            adapter.addData("无网络连接，请检查网络");
            return;
        }
        btn.setVisibility(View.GONE);
        indicatorView.setVisibility(View.VISIBLE);
        adapter.addData("正在向服务器查询最新数据");
        if (manager != null && manager.isRunning()) {
            manager.cancelHttpRequest();
            manager.postRequest();
            return;
        }
        HttpRequestVo requestVo = new HttpRequestVo(new HashMap<String, String>(), "");
        manager = new HttpManager(this, this, requestVo);
//        manager.postRequest();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    EventBus.getDefault().post(new DefaultEvent(0));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void action(int i, Object object) {

    }
}
