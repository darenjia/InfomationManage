package com.bokun.bkjcb.infomationmanage.Activity;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.allen.library.SuperTextView;
import com.bokun.bkjcb.infomationmanage.Adapter.SimpleFragmentAdapter;
import com.bokun.bkjcb.infomationmanage.Domain.HistoryItem;
import com.bokun.bkjcb.infomationmanage.Domain.Level;
import com.bokun.bkjcb.infomationmanage.Domain.User;
import com.bokun.bkjcb.infomationmanage.Domain.VersionInfo;
import com.bokun.bkjcb.infomationmanage.Fragment.FirstFragment;
import com.bokun.bkjcb.infomationmanage.Fragment.ForthFragment;
import com.bokun.bkjcb.infomationmanage.Fragment.MainFragment;
import com.bokun.bkjcb.infomationmanage.Http.DefaultEvent;
import com.bokun.bkjcb.infomationmanage.Http.HttpManager;
import com.bokun.bkjcb.infomationmanage.Http.HttpRequestVo;
import com.bokun.bkjcb.infomationmanage.Http.JsonParser;
import com.bokun.bkjcb.infomationmanage.Http.RequestListener;
import com.bokun.bkjcb.infomationmanage.Http.XmlParser;
import com.bokun.bkjcb.infomationmanage.R;
import com.bokun.bkjcb.infomationmanage.SQL.DBManager;
import com.bokun.bkjcb.infomationmanage.Utils.L;
import com.bokun.bkjcb.infomationmanage.Utils.NetUtils;
import com.bokun.bkjcb.infomationmanage.Utils.SPUtils;
import com.bokun.bkjcb.infomationmanage.Utils.Utils;
import com.example.zhouwei.library.CustomPopWindow;

import org.greenrobot.eventbus.EventBus;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.carbs.android.avatarimageview.library.AvatarImageView;
import me.shaohui.bottomdialog.BottomDialog;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener, RequestListener {
    private ViewPager viewPager;
    private BottomNavigationView navigationView;
    private int currentId;
    private Intent intent;
    private BottomDialog bottomDialog;
    private CustomPopWindow popWindow;
    private Button btn_ig;
    private Button btn_con;
    private TextView info;
    private AlertDialog dialog;
    private String date;
    private TextView time;
    private HttpManager manager;
    private boolean flag = false;
    public boolean hasNew = false;

    public User user;
    private ArrayList<Fragment> fragments;
    private UpdateListener updateListener;
    private AlertDialog.Builder builder;


    public interface UpdateListener {
        void onChanged(boolean isShow);
    }

    public void setUpdateListener(UpdateListener listener) {
        this.updateListener = listener;
    }

    @Override
    protected void setView() {
        setContentView(R.layout.activity_main2);
       /* if (!flag) {
            checkUpdate();
        }*/
    }

    @Override
    protected void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        navigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void setListener() {
        navigationView.setOnNavigationItemSelectedListener(this);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    currentId = R.id.navigation_home;
                } else if (position == 1) {
                    currentId = R.id.navigation_nav;
                } else {
                    currentId = R.id.navigation_other;
                }
                navigationView.setSelectedItemId(currentId);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void loadData() {
        hasNew = getIntent().getBooleanExtra("hasNew", false);
        user = (User) getIntent().getExtras().get("User");
        L.i("是否有新版本" + hasNew);
        fragments = new ArrayList<>();
        fragments.add(FirstFragment.newInstance().setActivity(this));
        fragments.add(MainFragment.newInstance().setActivity(this));
        fragments.add(ForthFragment.newInstance().setActivity(this));
        SimpleFragmentAdapter adapter = new SimpleFragmentAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                viewPager.setCurrentItem(0);
                break;
            case R.id.navigation_nav:
                viewPager.setCurrentItem(1);
                break;
            case R.id.navigation_other:
                viewPager.setCurrentItem(2);
                break;
        }
        return true;
    }

    public void actionCall(String number, User user) {
        number = number.trim();
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher m = pattern.matcher(number);
        if (!m.matches()) {
            int end = isNumber(number);
            if (end > 0) {
                number = number.substring(0, end);
            }
        }
        if (user != null) {
            HistoryItem item = new HistoryItem();
            item.setTel(number);
            item.setUserName(user.getUserName());
            item.setUserId(user.getId());
            item.setTime(System.currentTimeMillis());
            insertHistory(item);
        }

        if (number.length() == 11) {
            number = "+86" + number;
        } else {
            number = "021" + number;
        }
        intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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
            if (verifyPermissions(paramArrayOfInt)) {
                startActivity(intent);
            } else {
                Toast.makeText(this, "没有拨号权限，无法拨打电话", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void showDetail(final User user) {
        bottomDialog = BottomDialog.create(getSupportFragmentManager())
                .setViewListener(new BottomDialog.ViewListener() {
                    @Override
                    public void bindView(View v) {
                        initDialogView(v, user);
                    }
                })
                .setLayoutRes(R.layout.alert_view)
                .setDimAmount(0.5f)
                .setTag("BottomDialog");
        bottomDialog.show();
    }

    private void initDialogView(View view, final User user) {

        TextView name = (TextView) view.findViewById(R.id.name);
        AvatarImageView imageView = (AvatarImageView) view.findViewById(R.id.item_avatar);
        name.setText(user.getUserName());
        imageView.setTextAndColor(user.getUserName().substring(0, 1), Utils.GetRandomColor(this));
        SuperTextView tel1 = (SuperTextView) view.findViewById(R.id.phoneNumber1);
        SuperTextView tel2 = (SuperTextView) view.findViewById(R.id.phoneNumber2);
        SuperTextView quxian = (SuperTextView) view.findViewById(R.id.quxian);
        SuperTextView department = (SuperTextView) view.findViewById(R.id.bumen);
        SuperTextView zhiwu = (SuperTextView) view.findViewById(R.id.zhiwu);
        SuperTextView unit_address = (SuperTextView) view.findViewById(R.id.dizhi_danwei);
        SuperTextView unit_phone = (SuperTextView) view.findViewById(R.id.dianha_danwei);
        SuperTextView unit_fax = (SuperTextView) view.findViewById(R.id.chuanzhen_danwei);
        SuperTextView unit_zipcode = (SuperTextView) view.findViewById(R.id.youbian_danwei);
        ImageView close = (ImageView) view.findViewById(R.id.close);
        String s1 = user.getTel();
        String s2 = user.getPhoneNumber();
        String s3 = user.getTel_U();
        if (!TextUtils.isEmpty(s1) && !TextUtils.isEmpty(s2)) {
            tel2.setVisibility(View.VISIBLE);
            tel1.setLeftBottomString(s1);
            tel2.setLeftBottomString(s2);
        } else {
            if (TextUtils.isEmpty(s1)) {
                tel1.setLeftBottomString(s2);
            } else {
                tel1.setLeftBottomString(s1);
            }
        }
        final String unitName = user.getDepartmentNameA();
        if (unitName.length() > 16) {
            department.setRightString(unitName.substring(16, unitName.length()));
            department.setRightTopString(unitName.substring(0, 16));
        } else {
            department.setRightString(unitName);
        }
//        department.setRightBottomString(user.getDepartmentNameA());
        String address = user.getAddress();
        if (address.length() > 16) {
            unit_address.setRightString(address.substring(16, address.length()));
            unit_address.setRightTopString(address.substring(0, 16));
        } else {
            unit_address.setRightString(user.getAddress());
        }
        if (!TextUtils.isEmpty(user.getDuty())) {
            zhiwu.setRightBottomString(user.getDuty());
            zhiwu.setVisibility(View.VISIBLE);
        }
        final String qxName = user.getQuXian();
        if (!TextUtils.isEmpty(user.getQuXian())) {
            quxian.setRightBottomString(user.getQuXian());
            quxian.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(s3)) {
            String[] numbers = s3.split("、");
            if (numbers.length > 1) {
                unit_phone.setCenterBottomString(numbers[1]);
            }
            unit_phone.setRightBottomString(numbers[0]);
            unit_phone.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(user.getFax())) {
            unit_fax.setRightBottomString(user.getFax());
            unit_fax.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(user.getZipCode())) {
            unit_zipcode.setRightBottomString(user.getZipCode());
            unit_zipcode.setVisibility(View.VISIBLE);
        }
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = ((SuperTextView) view).getLeftBottomString();
                actionCall(number, user);
            }
        };
        View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPopBottom(v);
                return true;
            }
        };
        tel1.setOnClickListener(listener);
        tel2.setOnClickListener(listener);
        tel1.setOnLongClickListener(longClickListener);
        tel2.setOnLongClickListener(longClickListener);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.dismiss();
            }
        });
        unit_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = ((SuperTextView) v).getRightBottomString();
                actionCall(number, user);
            }
        });
        department.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* viewPager.setCurrentItem(0);
                FirstFragment firstFragment = (FirstFragment) fragments.get(0);*/
                Level level = new Level();
                level.setDepartmentNameA(unitName);
                level.setLevel(user.getLevel());
                level.setKind1(user.getKind1());
                level.setKind2(user.getKind2());
                level.setKind3(user.getKind3());
                level.setQuxian(user.getQuxin());
                AboutActivity.comeIn(level, MainActivity.this, 0, qxName);
            }
        });
    }

    private int isNumber(String s) {
        for (int i = 0; i < s.length(); i++) {
            int flag = s.charAt(i);
            if (flag < 48 || flag > 57) {
                return i;
            }
        }
        return -1;
    }

    private void insertHistory(HistoryItem item) {
        DBManager.newInstance(this).insertHistory(item);
    }

    private void showPopBottom(View view) {
        final SuperTextView textView = (SuperTextView) view;
        View v = getLayoutInflater().inflate(R.layout.pop_layout, null);
        TextView tv = (TextView) v.findViewById(R.id.copy);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setPrimaryClip(ClipData.newPlainText("", textView.getLeftBottomString()));
                popWindow.dissmiss();
                Toast.makeText(MainActivity.this, "复制成功", Toast.LENGTH_SHORT).show();
            }
        });
        if (popWindow == null) {
            popWindow = new CustomPopWindow.PopupWindowBuilder(this)
                    .setView(v)
                    .setFocusable(true)
                    .setOutsideTouchable(true)
                    .create();
        }
        popWindow.showAsDropDown(view, 20, 10);

    }

    private View createRefreshView(final boolean type) {
        View view = View.inflate(this, R.layout.refresh_view, null);
        info = (TextView) view.findViewById(R.id.refresh_info);
        time = (TextView) view.findViewById(R.id.refresh_time);
        btn_ig = (Button) view.findViewById(R.id.ignore_btn);
        btn_con = (Button) view.findViewById(R.id.now_btn);
        if (type) {
            info.setText(R.string.SoftRefresh);
        }
        date = (String) SPUtils.get(this, "Time", "----.--.--");
        time.setText(date);
        btn_ig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (updateListener != null) {
                    updateListener.onChanged(true);
                }
                flag = true;
            }
        });
        btn_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                flag = true;
                if (updateListener != null) {
                    updateListener.onChanged(true);
                }
                UpdateActivity.comeIn(type ? 1 : 2, MainActivity.this);
            }
        });
        return view;
    }


    private void checkUpdate() {
        if (!NetUtils.isConnected(this)) {
            return;
        }
        if (manager != null && manager.isRunning()) {
            manager.cancelHttpRequest();
            manager.postRequest();
            return;
        }
        HttpRequestVo requestVo = new HttpRequestVo("version");
        manager = new HttpManager(this, this, requestVo);
        manager.postRequest();
    }

    @Override
    protected void handlerEvent(DefaultEvent event) {
        date = Utils.getDate();
        SPUtils.put(this, "Time", date);
        if (builder == null) {
            builder = new AlertDialog.Builder(MainActivity.this);
        }
        switch (event.getState_code()) {
            case DefaultEvent.SOFT_NEED_UPDATE:
                builder.setView(createRefreshView(true));
                dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                hasNew = true;
                break;
            case DefaultEvent.DATA_NEED_UPDATE:
                builder.setView(createRefreshView(false));
                dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                hasNew = true;
                break;
            case DefaultEvent.GET_DATA_NULL:
                hasNew = false;
                break;
            case DefaultEvent.GET_DATA_SUCCESS:
                hasNew = false;
                if (updateListener != null) {
                    updateListener.onChanged(false);
                }
                flag = true;
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void action(int i, Object object) {
        if (object != null) {
            L.i(object.toString());
            SoapObject soapObject = (SoapObject) object;
            String s = XmlParser.parseSoapObject(soapObject);
            VersionInfo version = JsonParser.getVersion(s).get(0);
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

    @Override
    protected void onStart() {
        super.onStart();
       /* if (updateListener != null && hasNew) {
            updateListener.onChanged(true);
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DBManager.newInstance(this).close();
    }
}
