package com.bokun.bkjcb.infomationmanage.Activity;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.allen.library.SuperTextView;
import com.bokun.bkjcb.infomationmanage.Adapter.MultiTypeAdapter;
import com.bokun.bkjcb.infomationmanage.Adapter.SimpleExpandAdapter;
import com.bokun.bkjcb.infomationmanage.Adapter.TypeItemCallback;
import com.bokun.bkjcb.infomationmanage.Domain.HistoryItem;
import com.bokun.bkjcb.infomationmanage.Domain.Level;
import com.bokun.bkjcb.infomationmanage.Domain.User;
import com.bokun.bkjcb.infomationmanage.Http.DefaultEvent;
import com.bokun.bkjcb.infomationmanage.R;
import com.bokun.bkjcb.infomationmanage.SQL.DBManager;
import com.bokun.bkjcb.infomationmanage.View.DividerItemDecoration;
import com.example.zhouwei.library.CustomPopWindow;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.carbs.android.avatarimageview.library.AvatarImageView;
import me.shaohui.bottomdialog.BottomDialog;

public class AboutActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private MultiTypeAdapter typeAdapter;
    private BottomDialog bottomDialog;
    private CustomPopWindow popWindow;
    private Intent intent;
    private Level level;
    private int type;
    private ExpandableListView listView;
    private SimpleExpandAdapter adapter;

    @Override
    protected void setView() {
        setContentView(R.layout.activity_about);
        level = (Level) getIntent().getExtras().get("Level");
        type = getIntent().getIntExtra("type", 0);
    }

    @Override
    protected void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (type == 1) {
            toolbar.setTitle(level.getDepartmentNameA() + "-相关企业");
        } else {
            toolbar.setTitle(level.getDepartmentNameA());
        }

        toolbar.setNavigationIcon(R.drawable.back_aa);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recycler_about);
        listView = (ExpandableListView) findViewById(R.id.expand_about);
    }

    @Override
    protected void setListener() {
        if (typeAdapter != null) {
            typeAdapter.setItemClick(new TypeItemCallback() {
                @Override
                public void onItemClick(int position, User model, int tag) {
                    super.onItemClick(position, model, tag);
                    showDetail(model);
                }
            });
        } else {
            listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                    showDetail(adapter.getUser(groupPosition, childPosition));
                    return false;
                }
            });
        }
    }

    @Override
    protected void loadData() {
        level = DBManager.newInstance(this).queryLevelId(level);
        if (type == 0) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            typeAdapter = new MultiTypeAdapter(this);
            typeAdapter.setData(DBManager.newInstance(this).queryUser(level));
            recyclerView.setAdapter(typeAdapter);
            recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            ArrayList<User> users = new ArrayList<>();
            ArrayList<Level> levels = DBManager.newInstance(this).queryLevel(level);
            for (Level l : levels) {
                users.addAll(DBManager.newInstance(this).queryUser(l));
            }
            adapter = new SimpleExpandAdapter(this, users);
            adapter.setListView(listView);
            listView.setAdapter(adapter);
            listView.setGroupIndicator(null);
            listView.setVisibility(View.VISIBLE);
            if (adapter.getGroupCount() == 1) {
                listView.expandGroup(0);
            }
        }
    }

    @Override
    protected void handlerEvent(DefaultEvent event) {

    }

    public static void comeIn(Level level, Context activity, int type) {
        Intent intent = new Intent(activity, AboutActivity.class);
        intent.putExtra("Level", level);
        intent.putExtra("type", type);
        activity.startActivity(intent);
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
        imageView.setTextAndColor(user.getUserName().substring(0, 1), SimpleExpandAdapter.getColor(this));
        SuperTextView tel1 = (SuperTextView) view.findViewById(R.id.phoneNumber1);
        SuperTextView tel2 = (SuperTextView) view.findViewById(R.id.phoneNumber2);
        SuperTextView quxian = (SuperTextView) view.findViewById(R.id.quxian);
        final SuperTextView department = (SuperTextView) view.findViewById(R.id.bumen);
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

        department.setRightBottomString(user.getDepartmentNameA());
        unit_address.setRightString(user.getAddress());
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
               /* Level level = new Level();
                level.setDepartmentNameA(department.getRightBottomString());
                level.setLevel(user.getLevel());
                level.setKind1(user.getKind1());
                level.setKind2(user.getKind2());
                level.setKind3(user.getKind3());
                level.setQuxin(user.getQuxin());*/
            }
        });
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
                Toast.makeText(AboutActivity.this, "复制成功", Toast.LENGTH_SHORT).show();
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
        if (ActivityCompat.checkSelfPermission(AboutActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 0);
            }
            return;
        }
        startActivity(intent);
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

    @Override
    public void onBackPressed() {
        finish();
    }
}
