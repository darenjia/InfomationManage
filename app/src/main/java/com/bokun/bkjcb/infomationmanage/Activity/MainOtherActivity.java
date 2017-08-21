package com.bokun.bkjcb.infomationmanage.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.allen.library.SuperTextView;
import com.bokun.bkjcb.infomationmanage.Adapter.SimpleExpandAdapter;
import com.bokun.bkjcb.infomationmanage.Domain.User;
import com.bokun.bkjcb.infomationmanage.R;
import com.bokun.bkjcb.infomationmanage.SQL.DBManager;
import com.bokun.bkjcb.infomationmanage.Utils.L;
import com.bokun.bkjcb.infomationmanage.Utils.SPUtils;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.carbs.android.avatarimageview.library.AvatarImageView;
import info.hoang8f.android.segmented.SegmentedGroup;
import me.shaohui.bottomdialog.BottomDialog;
import zhy.com.highlight.HighLight;
import zhy.com.highlight.interfaces.HighLightInterface;
import zhy.com.highlight.position.OnBottomPosCallback;
import zhy.com.highlight.position.OnRightPosCallback;
import zhy.com.highlight.shape.RectLightShape;
import zhy.com.highlight.view.HightLightView;

public class MainOtherActivity extends BaseActivity implements ExpandableListView.OnChildClickListener, AdapterView.OnItemSelectedListener, View.OnClickListener {

    private ExpandableListView listView;
    private ArrayList<User> users;
    private Intent intent;
    private NiceSpinner sp_1, sp_2, sp_3, sp_4, sp_5, sp_6;
    private List<String> list;
    private ArrayList<String> list1, list2;
    private LinearLayout spinnerLayout, spLayout;
    private int position1;
    private int position2;
    private int position3;
    private int position4;
    private ImageView searchButton, cancelButton;
    private EditText editText;
    private String keyWord;
    private SimpleExpandAdapter adapter;
    private Toolbar toolbar;
    private HighLight mHightLight;
    private CardView cardView;
    private LinearLayout condition_layout;
    private CheckBox condition;
    private StringBuilder builder;
    private String str1, str2, str3, str4, str5, str6;
    private SegmentedGroup group;
    private boolean searchFlag = false;
    private BottomDialog bottomDialog;

    @Override
    protected void setView() {
        setContentView(R.layout.activity_main_other);
    }

    @Override
    protected void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        listView = (ExpandableListView) findViewById(R.id.listView);
        spinnerLayout = (LinearLayout) findViewById(R.id.spinner_layout);
        spLayout = (LinearLayout) findViewById(R.id.sp_layout);
        sp_1 = (NiceSpinner) findViewById(R.id.spinner_one);
        sp_2 = (NiceSpinner) findViewById(R.id.spinner_two);
        sp_3 = (NiceSpinner) findViewById(R.id.spinner_three);
        sp_4 = (NiceSpinner) findViewById(R.id.spinner_four);
        sp_5 = (NiceSpinner) findViewById(R.id.spinner_five);
        sp_6 = (NiceSpinner) findViewById(R.id.spinner_six);
        searchButton = (ImageView) findViewById(R.id.image_search);
        cancelButton = (ImageView) findViewById(R.id.clearSearch);
        editText = (EditText) findViewById(R.id.edit_search);
        cardView = (CardView) findViewById(R.id.card_search);
        condition_layout = (LinearLayout) findViewById(R.id.condition_layout);
        condition = (CheckBox) findViewById(R.id.condition);
        builder = new StringBuilder("筛选条件:全部");
        group = (SegmentedGroup) findViewById(R.id.segmented);
        setSupportActionBar(toolbar);
        listView.setGroupIndicator(null);
    }

    @Override
    protected void setListener() {

        listView.setOnChildClickListener(this);
        sp_1.setOnItemSelectedListener(this);
        sp_2.setOnItemSelectedListener(this);
        sp_3.setOnItemSelectedListener(this);
        sp_4.setOnItemSelectedListener(this);
        sp_5.setOnItemSelectedListener(this);
        sp_6.setOnItemSelectedListener(this);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                keyWord = charSequence.toString().trim();
//                L.i(keyWord);
                if (TextUtils.isEmpty(keyWord)) {
                    adapter.initData();
                    cancelButton.setImageResource(R.mipmap.ic_search);
                    searchButton.setVisibility(View.GONE);
                } else {
                    if (i1 == 1 && i2 == 0) {
                        adapter.repaceData();
                    }
                    cancelButton.setImageResource(R.mipmap.ic_close);
                    searchButton.setVisibility(View.VISIBLE);
                    adapter.getFilter().filter(searchFlag + "," + keyWord);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        searchButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_SEARCH || id == EditorInfo.IME_NULL) {
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    return true;
                }
                return false;
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL && condition_layout.getVisibility() != View.GONE) {
//                    condition_layout.setVisibility(View.GONE);
                    condition_layout.startAnimation(hide());
                    condition.setText(getTxt());
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
        condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (condition_layout.getVisibility() == View.GONE) {
//                    condition_layout.setVisibility(View.VISIBLE);
                    condition_layout.startAnimation(show());
                } else {
//                    condition_layout.setVisibility(View.GONE);
                    condition_layout.startAnimation(hide());
                    condition.setText(getTxt());
                }
            }
        });
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.button1) {
                    editText.setHint("按姓名查找");
                    searchFlag = false;
                } else {
                    editText.setHint("按单位查找");
                    searchFlag = true;
                }
                keyWord = editText.getText().toString().trim();
                if (!keyWord.equals("")) {
                    adapter.repaceData();
                    adapter.getFilter().filter(searchFlag + "," + keyWord);
                }
            }
        });
    }

    @Override
    protected void loadData() {
        new LoadDataTask().execute();

//        showHelp();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        tel1.setOnClickListener(listener);
        tel2.setOnClickListener(listener);
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

    private void actionCall(String number) {
        if (number.trim().length() == 11) {
            number = "+86" + number;
        } else {
            number = "021" + number;
        }
        intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
        if (ActivityCompat.checkSelfPermission(MainOtherActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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
                showMissingPermissionDialog();
            }
        }
    }

    private ArrayList<String> getSpinnerData(String leval) {
        return leval != null ? DBManager.newInstance(this).queryAllUnitName(leval) : DBManager.newInstance(this).queryAllQuName();
    }

    private ArrayList<String> getSpinnerData(int quxian) {
        return DBManager.newInstance(this).queryNameByQu(quxian);
    }

    private ArrayList<String> getSpinnerData(int quxian, int kind1) {
        return DBManager.newInstance(this).queryNameByQu(quxian, kind1);
    }

    private ArrayList<String> getSpinnerData(int quxian, int kind1, int kind2) {
        return DBManager.newInstance(this).queryNameByQu(quxian, kind1, kind2);
    }

    private ArrayList<String> getSpinnerDataByUnitId(int unitId) {
        return DBManager.newInstance(this).queryNameByUnitId(unitId);
    }

    private ArrayList<String> getSpinnerDataByKind1(int kind1) {
        return DBManager.newInstance(this).queryNameByKind1(kind1);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.spinner_one:
                if (i == 0) {
                    sp_2.setVisibility(View.GONE);
                    sp_3.setVisibility(View.GONE);
                    spinnerLayout.setVisibility(View.GONE);
                } else {
                    sp_2.setVisibility(View.VISIBLE);
                    sp_3.setVisibility(View.GONE);
                    spinnerLayout.setVisibility(View.GONE);
                    sp_2.dismissDropDown();
                    if (i == 1) {
                        sp_2.attachDataSource(list2);
                    } else {
                        sp_2.attachDataSource(getSpinnerData("2"));
                    }
                    sp_2.requestLayout();
                }
                adapter.filtUser(0, i);
                break;
            case R.id.spinner_two:
                position1 = sp_1.getSelectedIndex();
                if (position1 == 1) {
                    if (i != 0) {
                        sp_3.setVisibility(View.VISIBLE);
                        if (i == 1) {
                            sp_3.attachDataSource(getSpinnerData("0"));
                        } else {
                            sp_3.attachDataSource(getSpinnerData(null));
                        }
                        sp_3.requestLayout();
                    } else {
                        sp_3.setVisibility(View.GONE);
                    }
                } else if (position1 == 2) {
                    if (i != 0) {
                        sp_3.setVisibility(View.VISIBLE);
                        sp_3.attachDataSource(getSpinnerDataByUnitId(i));
                        sp_3.requestLayout();
                    } else {
                        sp_3.setVisibility(View.GONE);
                    }
                }
                spinnerLayout.setVisibility(View.GONE);
                adapter.filtUser(1, i);
                break;
            case R.id.spinner_three:
                position2 = sp_2.getSelectedIndex();
                if (position1 == 1) {
                    if (position2 == 0) {
                        spinnerLayout.setVisibility(View.GONE);
                        sp_3.setVisibility(View.GONE);
                    } else if (position2 == 1) {
                        //选择市级之后，做list筛选
                        if (i == 0) {
                            spinnerLayout.setVisibility(View.GONE);
                        } else {
                            spinnerLayout.setVisibility(View.VISIBLE);
                            sp_5.setVisibility(View.GONE);
                            sp_6.setVisibility(View.GONE);
                            sp_4.attachDataSource(getSpinnerDataByKind1(i));
                            sp_4.requestLayout();
                        }
                    } else {
                        if (i == 0) {
                            spinnerLayout.setVisibility(View.GONE);
                        } else {
                            spinnerLayout.setVisibility(View.VISIBLE);
                            sp_5.setVisibility(View.GONE);
                            sp_6.setVisibility(View.GONE);
                            sp_4.attachDataSource(getSpinnerData(i));
                            sp_4.requestLayout();
                        }
                    }
                } else {
                    //选择企业单位之后做筛选
                    spinnerLayout.setVisibility(View.GONE);
                }
                adapter.filtUser(2, i);
                break;
            case R.id.spinner_four:
                position3 = sp_3.getSelectedIndex();
                if (position2 == 2) {
                    if (position3 != 0) {
                        if (i == 0) {
                            sp_5.setVisibility(View.GONE);
                            sp_6.setVisibility(View.GONE);
                        } else {
                            sp_5.setVisibility(View.VISIBLE);
                            sp_6.setVisibility(View.GONE);
                            sp_5.attachDataSource(getSpinnerData(position3, i));
                            sp_5.requestLayout();
                        }
                    }
                } else {
                    //市级单位筛选
                }
                adapter.filtUser(3, i);
                break;
            case R.id.spinner_five:
                position4 = sp_4.getSelectedIndex();
                if (position4 == 3) {
                    if (i == 0) {
                        sp_6.setVisibility(View.GONE);
                    } else {
                        sp_6.setVisibility(View.VISIBLE);
                        sp_6.attachDataSource(getSpinnerData(position3, position4, i));
                        sp_6.requestLayout();
                    }
                } else {
                }
                adapter.filtUser(4, i);
                break;
            case R.id.spinner_six:
                if (i == 0) {

                } else {
                    //做筛选
                }
                adapter.filtUser(5, i);
                break;
        }
    }

    private String getTxt() {
        builder.delete(5, builder.length());
        str1 = sp_1.getText().toString();

        if (sp_2.getVisibility() != View.GONE) {
            str2 = "/" + sp_2.getText().toString();
        } else {
            str2 = "";
        }
        if (sp_3.getVisibility() != View.GONE) {
            str3 = "/" + sp_3.getText().toString();
        } else {
            str3 = "";
        }
        if (spinnerLayout.getVisibility() != View.GONE) {
            if (sp_4.getVisibility() != View.GONE) {
                str4 = "/" + sp_4.getText().toString();
            } else {
                str4 = "";
            }
            if (sp_5.getVisibility() != View.GONE) {
                str5 = "/" + sp_5.getText().toString();
            } else {
                str5 = "";
            }
            if (sp_6.getVisibility() != View.GONE) {
                str6 = "/" + sp_6.getText().toString();
            } else {
                str6 = "";
            }
        } else {
            return builder.append(str1).append(str2).append(str3).toString();
        }
        return (builder.append(str1).append(str2).append(str3).append(str4).append(str5).append(str6).toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_search:
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                break;
            case R.id.clearSearch:
                if (!TextUtils.isEmpty(editText.getText().toString().trim())) {
                    adapter.initData();
                    cancelButton.setImageResource(R.mipmap.ic_search);
                    searchButton.setVisibility(View.GONE);
                    editText.setText("");
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.help) {
            showHelp();
        }
//        else if (item.getItemId() == R.id.byName) {
//            //按姓氏分组
//            changePinying(0);
//        } else if (item.getItemId() == R.id.byUnit) {
//            //按单位分组
//            changePinying(1);
//        }
        return true;
    }

    private void showHelp() {
// targetView 目标按钮 tipView添加的提示布局 可以直接找到'我知道了'按钮添加监听事件等处理
        mHightLight = new HighLight(MainOtherActivity.this)//
                .autoRemove(false)//设置背景点击高亮布局自动移除为false 默认为true
//                .intercept(false)//设置拦截属性为false 高亮布局不影响后面布局的滑动效果
                .intercept(true)//拦截属性默认为true 使下方ClickCallback生效
                .enableNext()//开启next模式并通过show方法显示 然后通过调用next()方法切换到下一个提示布局，直到移除自身
                .anchor(findViewById(R.id.container))//如果是Activity上增加引导层，不需要设置anchor
                .addHighLight(R.id.condition, R.layout.info_known_1, new OnBottomPosCallback(45), new RectLightShape(0, 0, 15, 0, 0))//矩形去除圆角
                .addHighLight(R.id.edit_search, R.layout.info_known, new OnBottomPosCallback(45), new RectLightShape(0, 0, 15, 0, 0))//矩形去除圆角
                .addHighLight(R.id.segmented, R.layout.info_known2, new OnRightPosCallback(0), new RectLightShape(0, 0, 15, 0, 0))//矩形去除圆角
//                .addHighLight(R.id.help, R.layout.info_known, new OnBottomPosCallback(45), new RectLightShape(0, 0, 15, 0, 0))//矩形去除圆角
                .setOnRemoveCallback(new HighLightInterface.OnRemoveCallback() {//监听移除回调
                    @Override
                    public void onRemove() {
                        SPUtils.put(MainOtherActivity.this, "isFirst", false);
                    }
                })
                .setOnNextCallback(new HighLightInterface.OnNextCallback() {
                    @Override
                    public void onNext(HightLightView hightLightView, View targetView, View tipView) {
                        // targetView 目标按钮 tipView添加的提示布局 可以直接找到'我知道了'按钮添加监听事件等处理
                        tipView.findViewById(R.id.iv_known).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mHightLight.isShowing() && mHightLight.isNext())//如果开启next模式
                                {
                                    mHightLight.next();
                                } else {
                                    mHightLight.remove();
                                }
                            }
                        });
                    }
                });
        mHightLight.show();
    }

    private void isShowHelp() {
        boolean isFirst = (boolean) SPUtils.get(this, "isFirst", true);
        if (isFirst) {
            showHelp();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private Animation show() {
        Animation animation = new AlphaAnimation(0, 1f);
        animation.setDuration(300);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                condition_layout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return animation;
    }

    private Animation hide() {
        Animation animation = new AlphaAnimation(1, 0);
        animation.setDuration(300);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                condition_layout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return animation;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        final User user = adapter.getUser(groupPosition, childPosition);
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
        return true;
    }

    class LoadDataTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            users = DBManager.newInstance(MainOtherActivity.this).queryAllUser();
//            Collections.sort(users);
            adapter = new SimpleExpandAdapter(MainOtherActivity.this, users);
            adapter.setListView(listView);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            listView.setAdapter(adapter);
            list1 = new ArrayList<>(Arrays.asList("全部", "管理单位", "企业单位"));
            list2 = new ArrayList<>(Arrays.asList("全部", "市级", "区级"));
            sp_1.attachDataSource(list1);
            listView.setTextFilterEnabled(true);
            isShowHelp();
        }
    }

    /**
     * 显示提示信息
     *
     * @since 2.5.0
     */
    private void showMissingPermissionDialog() {
       /* AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("当前应用缺少拨号权限。\\n\\n请点击\\\"设置\\\"-\\\"权限\\\"-打开所需权限。");

        // 拒绝, 退出应用
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        builder.setPositiveButton("设置",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                    }
                });

        builder.setCancelable(false);

        builder.show();*/
        L.i("没有权限");
       /* bottomDialog.dismiss();
        Snackbar.make(listView, "没有拨号权限，无法拨打电话", 3).setAction("设置", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAppSettings();
            }
        }).show();*/
        Toast.makeText(this,"没有拨号权限，无法拨打电话",Toast.LENGTH_SHORT).show();
    }

    /**
     * 启动应用的设置
     *
     * @since 2.5.0
     */
    private void startAppSettings() {
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

}
