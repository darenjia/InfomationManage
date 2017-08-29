package com.bokun.bkjcb.infomationmanage.Activity;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.allen.library.SuperButton;
import com.allen.library.SuperTextView;
import com.allenliu.badgeview.BadgeFactory;
import com.allenliu.badgeview.BadgeView;
import com.bokun.bkjcb.infomationmanage.Adapter.SimpleExpandAdapter;
import com.bokun.bkjcb.infomationmanage.Adapter.SimpleFragmentAdapter;
import com.bokun.bkjcb.infomationmanage.Domain.User;
import com.bokun.bkjcb.infomationmanage.Fragment.MainFragment;
import com.bokun.bkjcb.infomationmanage.Fragment.SecondFragment;
import com.bokun.bkjcb.infomationmanage.Fragment.ThridFragment;
import com.bokun.bkjcb.infomationmanage.Http.DefaultEvent;
import com.bokun.bkjcb.infomationmanage.Http.HttpManager;
import com.bokun.bkjcb.infomationmanage.Http.HttpRequestVo;
import com.bokun.bkjcb.infomationmanage.Http.RequestListener;
import com.bokun.bkjcb.infomationmanage.R;
import com.example.zhouwei.library.CustomPopWindow;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
    private TextView refresh;
    private BadgeView badgeView;
    private boolean needRefresh;
    private SuperButton button;
    private ProgressBar progressBar;
    private TextView info;
    private AlertDialog dialog;
    private String date;
    private TextView time;
    private HttpManager manager;

    @Override
    protected void setView() {
        setContentView(R.layout.activity_main2);
        checkUpdate();
    }

    @Override
    protected void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        navigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        refresh = (TextView) findViewById(R.id.main_refresh);
        badgeView = BadgeFactory.create(this)
                .setWidthAndHeight(10, 10)
                .setBadgeBackground(Color.RED)
                .setBadgeGravity(Gravity.RIGHT | Gravity.TOP)
                .setShape(BadgeView.SHAPE_CIRCLE);
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
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setView(createRefreshView());
                    dialog = builder.create();
                    dialog.setCanceledOnTouchOutside(true);
                }
                dialog.show();
            }
        });
    }

    @Override
    protected void loadData() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(SecondFragment.newInstance().setActivity(this));
        fragments.add(MainFragment.newInstance().setActivity(this));
        fragments.add(ThridFragment.newInstance().setActivity(this));
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

    public void actionCall(String number) {
        number = number.trim();
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher m = pattern.matcher(number);
        if (!m.matches()) {
            int end = isNumber(number);
            if (end > 0) {
                number = number.substring(0, end);
            }
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

    private void initDialogView(View view, User user) {

        TextView name = (TextView) view.findViewById(R.id.name);
        AvatarImageView imageView = (AvatarImageView) view.findViewById(R.id.item_avatar);
        name.setText(user.getUserName());
        imageView.setTextAndColor(user.getUserName().substring(0, 1), SimpleExpandAdapter.getColor(this));
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

        department.setRightBottomString(user.getDepartmentName());
        unit_address.setRightBottomString(user.getAddress());
        if (!TextUtils.isEmpty(user.getDuty())) {
            zhiwu.setRightBottomString(user.getDuty());
            zhiwu.setVisibility(View.VISIBLE);
        }
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
                actionCall(number);
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
                actionCall(number);
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

    private View createRefreshView() {
        View view = View.inflate(this, R.layout.refresh_view, null);
        info = (TextView) view.findViewById(R.id.refresh_info);
        time = (TextView) view.findViewById(R.id.refresh_time);
        button = (SuperButton) view.findViewById(R.id.refresh_btn);
        progressBar = (ProgressBar) view.findViewById(R.id.refresh_pro);

        time.setText(date);
        if (needRefresh) {
            info.setText(R.string.needRefresh);
            button.setText("立即更新");
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (needRefresh) {
                    refreshData();
                    dialog.setCanceledOnTouchOutside(false);
                    info.setText("更新数据中。。。");
                    progressBar.setVisibility(View.VISIBLE);
                    button.setVisibility(View.GONE);
                } else {
                    checkUpdate();
                    progressBar.setVisibility(View.VISIBLE);
                    button.setVisibility(View.GONE);
                }
            }
        });
        return view;
    }

    private void refreshData() {
    }

    private void checkUpdate() {
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
                    EventBus.getDefault().post(new DefaultEvent(1));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void handlerEvent(DefaultEvent event) {
        getDate();
        if (event.getState_code() == 1) {
            needRefresh = true;
            badgeView.bind(refresh);
            if (button != null) {
                button.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                info.setText(R.string.needRefresh);
                time.setText(date);
            }
        } else {
            needRefresh = false;
            if (button != null) {
                button.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                info.setText(R.string.unknownRefresh);
                time.setText(date);
            }
        }
    }

    private void getDate() {
        Date d = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm");
        date = format.format(d);
    }

    @Override
    public void onBackPressed() {
        /*if (needRefresh) {
            Toast.makeText(this, "数据更新中，禁止操作", Toast.LENGTH_SHORT).show();
            return;
        }*/
        super.onBackPressed();
    }

    @Override
    public void action(int i, Object object) {

    }
}
