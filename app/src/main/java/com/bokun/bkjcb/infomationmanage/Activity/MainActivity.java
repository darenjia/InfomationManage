package com.bokun.bkjcb.infomationmanage.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bokun.bkjcb.infomationmanage.Adapter.SortAdapter;
import com.bokun.bkjcb.infomationmanage.Domain.User;
import com.bokun.bkjcb.infomationmanage.R;
import com.bokun.bkjcb.infomationmanage.SQL.DBManager;
import com.bokun.bkjcb.infomationmanage.View.SideBar;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private SideBar sideBar;
    private ListView listView;
    private ArrayList<User> users;
    private Intent intent;
    private NiceSpinner sp_1, sp_2, sp_3;

    @Override
    protected void setView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initView() {
        sideBar = (SideBar) findViewById(R.id.side_bar);
        listView = (ListView) findViewById(R.id.listView);
        sp_1 = (NiceSpinner) findViewById(R.id.spinner_one);
        sp_2 = (NiceSpinner) findViewById(R.id.spinner_two);
        sp_3 = (NiceSpinner) findViewById(R.id.spinner_three);
    }

    @Override
    protected void setListener() {
        sideBar.setOnStrSelectCallBack(new SideBar.ISideBarSelectCallBack() {
            @Override
            public void onSelectStr(int index, String selectStr) {
                for (int i = 0; i < users.size(); i++) {
                    if (selectStr.equalsIgnoreCase(users.get(i).getFirstLetter())) {
                        listView.setSelection(i); // 选择到首字母出现的位置
                        return;
                    }
                }
            }
        });
        listView.setOnItemClickListener(this);
    }

    @Override
    protected void loadData() {
        users = DBManager.newInstance(this).queryAllUser();
        Collections.sort(users);
        SortAdapter adapter = new SortAdapter(this, users);
        listView.setAdapter(adapter);
        List<String> list = new ArrayList<>(Arrays.asList("全部", "管理单位", "企业单位"));
        sp_1.attachDataSource(list);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        User user = users.get(i);
        intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + user.getTel()));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 0);
            }
            return;
        }
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] paramArrayOfInt) {
        if (requestCode == 0) {
            if (!verifyPermissions(paramArrayOfInt)) {
                startActivity(intent);
            }
        }
    }
}
