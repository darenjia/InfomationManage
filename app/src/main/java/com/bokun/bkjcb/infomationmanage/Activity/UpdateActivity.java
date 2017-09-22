package com.bokun.bkjcb.infomationmanage.Activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bokun.bkjcb.infomationmanage.Adapter.UpdateAdapter;
import com.bokun.bkjcb.infomationmanage.Domain.DB_User;
import com.bokun.bkjcb.infomationmanage.Domain.Emergency;
import com.bokun.bkjcb.infomationmanage.Domain.Level;
import com.bokun.bkjcb.infomationmanage.Domain.LevelBind;
import com.bokun.bkjcb.infomationmanage.Domain.Unit;
import com.bokun.bkjcb.infomationmanage.Domain.VersionInfo;
import com.bokun.bkjcb.infomationmanage.Domain.Z_Quxian;
import com.bokun.bkjcb.infomationmanage.Http.DefaultEvent;
import com.bokun.bkjcb.infomationmanage.Http.HttpManager;
import com.bokun.bkjcb.infomationmanage.Http.HttpRequestVo;
import com.bokun.bkjcb.infomationmanage.Http.JsonParser;
import com.bokun.bkjcb.infomationmanage.Http.RequestListener;
import com.bokun.bkjcb.infomationmanage.Http.XmlParser;
import com.bokun.bkjcb.infomationmanage.R;
import com.bokun.bkjcb.infomationmanage.SQL.DBManager;
import com.bokun.bkjcb.infomationmanage.Utils.AppManager;
import com.bokun.bkjcb.infomationmanage.Utils.Constants;
import com.bokun.bkjcb.infomationmanage.Utils.L;
import com.bokun.bkjcb.infomationmanage.Utils.NetUtils;
import com.bokun.bkjcb.infomationmanage.Utils.SPUtils;
import com.bokun.bkjcb.infomationmanage.Utils.Utils;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;
import org.ksoap2.serialization.SoapObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by DengShuai on 2017/9/18.
 */

public class UpdateActivity extends BaseActivity implements RequestListener {

    private int updateNow;
    private boolean flag = true;
    private Button btn;
    private RecyclerView recyclerView;
    private ArrayList<String> strings;
    private UpdateAdapter adapter;
    private HttpManager manager;
    private AVLoadingIndicatorView indicatorView;
    private Context context;
    private String[] strSuccess = {"区属", "联系人", "紧急联系电话", "部门", "相关部门", "单位"};
    private File file;
    private VersionInfo version;

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
                onBackPressed();
            }
        });

        btn = (Button) findViewById(R.id.update_btn);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_update);
        indicatorView = (AVLoadingIndicatorView) findViewById(R.id.update_progress);
        context = this;
    }

    @Override
    protected void setListener() {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updateNow == 0) {
                    checkUpdate();
                } else if (updateNow == 1) {
                    download();
                } else if (updateNow == 2) {
                    refresh();
                } else if (updateNow == 3) {
                    Toast.makeText(context, "请重新打开应用", Toast.LENGTH_SHORT).show();
                    AppManager.getAppManager().AppExit(context);
                } else if (updateNow == 4) {
                    Utils.installApk(context, file);
                }
            }
        });
    }

    private void download() {
        flag = false;
        adapter.addData("下载最新程序版本");
        adapter.addData("请稍等");
        adapter.addData("当前进度:0%");
        btn.setVisibility(View.GONE);
        indicatorView.setVisibility(View.VISIBLE);
        new DownloadTask().execute();
    }

    private void refresh() {
        if (!NetUtils.isConnected(this)) {
            adapter.addData("无网络连接，请检查网络");
            return;
        }
        flag = false;
        refreshQuxian();
    }

    @Override
    protected void loadData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        strings = new ArrayList<>();
        VersionInfo info = DBManager.newInstance(context).getVersionInfo();
        strings.add("当前数据版本:" + info.getDataV());
        strings.add("当前程序版本:" + info.getProgramV());
        strings.add("上次检查时间:" + SPUtils.get(this, "Time", "（无）"));
        adapter = new UpdateAdapter(R.layout.update_view, strings);
        recyclerView.setAdapter(adapter);
        updateNow = getIntent().getIntExtra("need", 0);
        if (updateNow != 0) {
            btn.setVisibility(View.GONE);
            indicatorView.setVisibility(View.VISIBLE);
            checkUpdate();
        }
        checkPermission();
    }

    @Override
    protected void handlerEvent(DefaultEvent event) {
        if (event.getType() == 0) {
            switch (event.getState_code()) {
                case DefaultEvent.GET_DATA_NULL:
                    updateNow = 0;
                    adapter.addData("未知错误！请检测网络是否可用");
                    btn.setText("检查更新");
                    break;
                case DefaultEvent.GET_DATA_SUCCESS:
                    adapter.addData("暂无新版本，请放心使用!");
                    btn.setText("检查更新");
                    updateNow = 0;
                    break;
                case DefaultEvent.SOFT_NEED_UPDATE:
                    adapter.addData("检查到软件有新版本，请立即更新!");
                    btn.setText("立即更新");
                    updateNow = 1;
                    break;
                case DefaultEvent.DATA_NEED_UPDATE:
                    adapter.addData("检查到数据有新版本，请立即更新!");
                    btn.setText("立即更新");
                    updateNow = 2;
                    break;
            }
            btn.setVisibility(View.VISIBLE);
            indicatorView.setVisibility(View.GONE);
        } else {
            switch (event.getState_code()) {
                case DefaultEvent.SUCCESS:
                    adapter.addData("-更新" + strSuccess[event.getType() - 1] + "完成");
                    switch (event.getType()) {
                        case 1:
                            refreshDB_User();
                            break;
                        case 2:
                            refreshEmergency();
                            break;
                        case 3:
                            refreshLevel();
                            break;
                        case 4:
                            refreshLevelBind();
                            break;
                        case 5:
                            refreshUnit();
                            break;
                        case 6:
                            DBManager.newInstance(context).setVersionInfo(version);
                            adapter.addData("全部更新完成，OK！");
                            btn.setText("重新打开");
                            btn.setVisibility(View.VISIBLE);
                            indicatorView.setVisibility(View.GONE);
                            flag = true;
                            updateNow = 3;
                            break;
                    }
                    break;
                case DefaultEvent.FAILED:
                    adapter.addData("更新发生错误");
                    btn.setText("立即更新");
                    btn.setVisibility(View.VISIBLE);
                    indicatorView.setVisibility(View.GONE);
                    flag = true;
                    updateNow = 0;
                    break;
            }
        }

    }

    public static void comeIn(int flag, Context activity) {
        Intent intent = new Intent(activity, UpdateActivity.class);
        intent.putExtra("need", flag);
        activity.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (flag && updateNow == 0) {
            finish();
        } else if (!flag) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("当前程序正在更新，请不要进行任何操作，以免程序发生未知错误");
            builder.setNegativeButton("知道了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else if (updateNow == 1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("程序还未更新，是否离开？");
            builder.setNegativeButton("不更新", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            }).setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    download();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else if (updateNow == 3) {
            Toast.makeText(this, "请重新打开应用", Toast.LENGTH_SHORT).show();
            AppManager.getAppManager().AppExit(this);
        } else if (updateNow == 4) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("新版本程序已下载，请立即安装！");
            builder.setPositiveButton("安装", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Utils.installApk(context, file);
                }
            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else if (updateNow == 2) {
            finish();
        }
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
        HttpRequestVo requestVo = new HttpRequestVo("version");
        manager = new HttpManager(this, this, requestVo);
        manager.postRequest();
        SPUtils.put(this, "Time", Utils.getDate());
    }

    @Override
    public void action(int i, Object object) {
        if (object != null) {
            L.i(object.toString());
            SoapObject soapObject = (SoapObject) object;
            String s = XmlParser.parseSoapObject(soapObject);
            version = JsonParser.getVersion(s).get(0);
            VersionInfo local = DBManager.newInstance(this).getVersionInfo();
            if (!version.getProgramV().equals(local.getProgramV())) {
                EventBus.getDefault().post(new DefaultEvent(DefaultEvent.SOFT_NEED_UPDATE));
            } else if (!version.getDataV().equals(local.getDataV())) {
                EventBus.getDefault().post(new DefaultEvent(DefaultEvent.DATA_NEED_UPDATE));
            } else {
                EventBus.getDefault().post(new DefaultEvent(DefaultEvent.GET_DATA_SUCCESS));
            }
        } else {
            EventBus.getDefault().post(new DefaultEvent(DefaultEvent.GET_DATA_NULL));
        }
    }

    private void refreshQuxian() {
        HttpRequestVo requestVo = new HttpRequestVo("z_quxian");
        manager = new HttpManager(this, new RequestListener() {
            @Override
            public void action(int i, Object object) {
                if (object != null) {
                    L.i(object.toString());
                    SoapObject soapObject = (SoapObject) object;
                    String s = XmlParser.parseSoapObject(soapObject);
                    ArrayList<Z_Quxian> quxians = JsonParser.getZ_Quxian(s);
                    boolean isSuccess = DBManager.newInstance(context).insertQuxian(quxians);
                    if (isSuccess) {
                        EventBus.getDefault().post(new DefaultEvent(DefaultEvent.SUCCESS, 1));
                    } else {
                        EventBus.getDefault().post(new DefaultEvent(DefaultEvent.FAILED));
                    }
                } else {
                    EventBus.getDefault().post(new DefaultEvent(DefaultEvent.GET_DATA_NULL));
                }
            }
        }, requestVo);
        manager.postRequest();
//        adapter.addData("更新区属数据中");
    }

    private void refreshDB_User() {
        HttpRequestVo requestVo = new HttpRequestVo("user");
        manager = new HttpManager(this, new RequestListener() {
            @Override
            public void action(int i, Object object) {
                if (object != null) {
                    L.i(object.toString());
                    SoapObject soapObject = (SoapObject) object;
                    String s = XmlParser.parseSoapObject(soapObject);
                    ArrayList<DB_User> users = JsonParser.getUser(s);
                    boolean isSuccess = DBManager.newInstance(context).insertUsers(users);
                    if (isSuccess) {
                        EventBus.getDefault().post(new DefaultEvent(DefaultEvent.SUCCESS, 2));
                    } else {
                        EventBus.getDefault().post(new DefaultEvent(DefaultEvent.FAILED));
                    }
                } else {
                    EventBus.getDefault().post(new DefaultEvent(DefaultEvent.GET_DATA_NULL));
                }
            }
        }, requestVo);
        manager.postRequest();
//        adapter.addData("更新联系人数据");
    }

    private void refreshEmergency() {
        HttpRequestVo requestVo = new HttpRequestVo("emergency");
        manager = new HttpManager(this, new RequestListener() {
            @Override
            public void action(int i, Object object) {
                if (object != null) {
                    L.i(object.toString());
                    SoapObject soapObject = (SoapObject) object;
                    String s = XmlParser.parseSoapObject(soapObject);
                    ArrayList<Emergency> emergencies = JsonParser.getEmergency(s);
                    boolean isSuccess = DBManager.newInstance(context).insertEmergency(emergencies);
                    if (isSuccess) {
                        EventBus.getDefault().post(new DefaultEvent(DefaultEvent.SUCCESS, 3));
                    } else {
                        EventBus.getDefault().post(new DefaultEvent(DefaultEvent.FAILED));
                    }
                } else {
                    EventBus.getDefault().post(new DefaultEvent(DefaultEvent.GET_DATA_NULL));
                }
            }
        }, requestVo);
        manager.postRequest();
//        adapter.addData("更新紧急联系人数据");
    }

    private void refreshLevel() {
        HttpRequestVo requestVo = new HttpRequestVo("z_level");
        manager = new HttpManager(this, new RequestListener() {
            @Override
            public void action(int i, Object object) {
                if (object != null) {
                    L.i(object.toString());
                    SoapObject soapObject = (SoapObject) object;
                    String s = XmlParser.parseSoapObject(soapObject);
                    ArrayList<Level> levels = JsonParser.getLevel(s);
                    boolean isSuccess = DBManager.newInstance(context).insertLevel(levels);
                    if (isSuccess) {
                        EventBus.getDefault().post(new DefaultEvent(DefaultEvent.SUCCESS, 4));
                    } else {
                        EventBus.getDefault().post(new DefaultEvent(DefaultEvent.FAILED));
                    }
                } else {
                    EventBus.getDefault().post(new DefaultEvent(DefaultEvent.GET_DATA_NULL));
                }
            }
        }, requestVo);
        manager.postRequest();
//        adapter.addData("更新部门数据");
    }

    private void refreshLevelBind() {
        HttpRequestVo requestVo = new HttpRequestVo("levelbind");
        manager = new HttpManager(this, new RequestListener() {
            @Override
            public void action(int i, Object object) {
                if (object != null) {
                    L.i(object.toString());
                    SoapObject soapObject = (SoapObject) object;
                    String s = XmlParser.parseSoapObject(soapObject);
                    ArrayList<LevelBind> levelBinds = JsonParser.getLevelBind(s);
                    boolean isSuccess = DBManager.newInstance(context).insertLevelBind(levelBinds);
                    if (isSuccess) {
                        EventBus.getDefault().post(new DefaultEvent(DefaultEvent.SUCCESS, 5));
                    } else {
                        EventBus.getDefault().post(new DefaultEvent(DefaultEvent.FAILED));
                    }
                } else {
                    EventBus.getDefault().post(new DefaultEvent(DefaultEvent.GET_DATA_NULL));
                }
            }
        }, requestVo);
        manager.postRequest();
//        adapter.addData("更新相关部门数据");
    }

    private void refreshUnit() {
        HttpRequestVo requestVo = new HttpRequestVo("unit");
        manager = new HttpManager(this, new RequestListener() {
            @Override
            public void action(int i, Object object) {
                if (object != null) {
                    L.i(object.toString());
                    SoapObject soapObject = (SoapObject) object;
                    String s = XmlParser.parseSoapObject(soapObject);
                    ArrayList<Unit> units = JsonParser.getUnit(s);
                    boolean isSuccess = DBManager.newInstance(context).insertUnits(units);
                    if (isSuccess) {
                        EventBus.getDefault().post(new DefaultEvent(DefaultEvent.SUCCESS, 6));
                    } else {
                        EventBus.getDefault().post(new DefaultEvent(DefaultEvent.FAILED));
                    }
                } else {
                    EventBus.getDefault().post(new DefaultEvent(DefaultEvent.GET_DATA_NULL));
                }
            }
        }, requestVo);
        manager.postRequest();
//        adapter.addData("更新单位数据");
    }


    class DownloadTask extends AsyncTask<Void, Long, Boolean> {
        long current = 0;

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                URL url = new URL(Constants.URL_SOFT);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                if (connection.getResponseCode() == 200) {
                    String path = Environment.getExternalStorageDirectory() + "/GasAddressBook";

                    File parentPath = new File(path);
                    if (!parentPath.exists()) {
                        parentPath.mkdir();
                    }
                    file = new File(path, "GasAddressBook.apk");
                    L.i(file.getAbsolutePath());
                    long size = connection.getContentLength();
                    InputStream is = connection.getInputStream();
                    if (file.exists()) {
                        file.delete();
                    }
                    long com = 0;
                    FileOutputStream outputStream = new FileOutputStream(file);
                    byte[] bytes = new byte[1024 * 4];
                    int length = 0;
                    while ((length = is.read(bytes)) != -1) {
                        outputStream.write(bytes, 0, length);
                        com += length;
                        publishProgress(com * 100 / size);
                    }
                    is.close();
                    outputStream.close();
                } else {
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

            return true;
        }

        @Override
        protected void onProgressUpdate(Long[] values) {

            if (values[0] > current) {
                current = values[0];
                adapter.remove(adapter.getItemCount() - 1);
                adapter.addData("当前进度：" + current + "%");
            }

        }


        @Override
        protected void onPostExecute(Boolean b) {
            if (b) {
                adapter.addData("下载已完成");
                btn.setText("安装");
                btn.setVisibility(View.VISIBLE);
                indicatorView.setVisibility(View.GONE);
                updateNow = 4;
                flag = true;
            } else {
                adapter.addData("下载出现错误");
                btn.setText("重新下载");
                btn.setVisibility(View.VISIBLE);
                indicatorView.setVisibility(View.GONE);
                updateNow = 1;
                flag = true;
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] paramArrayOfInt) {
        if (requestCode == 0) {
            if (!verifyPermissions(paramArrayOfInt)) {

                Toast.makeText(this, "没有SD卡写入权限，无法下载应用", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(UpdateActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }
            return;
        }
    }

}
