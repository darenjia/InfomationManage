package com.bokun.bkjcb.infomationmanage.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
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
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bokun.bkjcb.infomationmanage.Adapter.AnotherSortAdapter;
import com.bokun.bkjcb.infomationmanage.Adapter.SortAdapter;
import com.bokun.bkjcb.infomationmanage.Domain.User;
import com.bokun.bkjcb.infomationmanage.R;
import com.bokun.bkjcb.infomationmanage.SQL.DBManager;
import com.bokun.bkjcb.infomationmanage.Utils.SPUtils;
import com.bokun.bkjcb.infomationmanage.View.SideBar;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import me.shaohui.bottomdialog.BottomDialog;
import zhy.com.highlight.HighLight;
import zhy.com.highlight.interfaces.HighLightInterface;
import zhy.com.highlight.position.OnBottomPosCallback;
import zhy.com.highlight.shape.RectLightShape;
import zhy.com.highlight.view.HightLightView;

public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener, View.OnClickListener {

    private SideBar sideBar;
    private ListView listView;
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
    private SortAdapter adapter;
    private SortAdapter adapterByName;
    private AnotherSortAdapter anotherAdapter;
    private Toolbar toolbar;
    private HighLight mHightLight;
    private CardView cardView;
    private LinearLayout condition_layout;

    @Override
    protected void setView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        sideBar = (SideBar) findViewById(R.id.side_bar);
        listView = (ListView) findViewById(R.id.listView);
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
        setSupportActionBar(toolbar);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
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
                    if (i1 == 1) {
                        adapter.repaceData();
                    }
                    cancelButton.setImageResource(R.mipmap.ic_close);
                    searchButton.setVisibility(View.VISIBLE);
                    adapter.getFilter().filter(keyWord);
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

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                if (firstVisibleItem >= 1) {
//                    condition_layout.setVisibility(View.GONE);
//                } else {
//                    condition_layout.setVisibility(View.VISIBLE);
//                }
            }
        });
    }

    @Override
    protected void loadData() {
        users = DBManager.newInstance(this).queryAllUser();
        Collections.sort(users);
        adapterByName = new SortAdapter(this, users);
        adapter = adapterByName;
        listView.setAdapter(adapter);
        list1 = new ArrayList<>(Arrays.asList("全部", "管理单位", "企业单位"));
        list2 = new ArrayList<>(Arrays.asList("全部", "市级", "区级"));
        sp_1.attachDataSource(list1);
        listView.setTextFilterEnabled(true);
        showHelp();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        final User user = adapter.getData().get(i);

     /*   View alertView = new AlertView().build(user, this, listener);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(alertView);
        builder.show();*/
        BottomDialog.create(getSupportFragmentManager())
                .setViewListener(new BottomDialog.ViewListener() {
                    @Override
                    public void bindView(View v) {
                        initDialogView(v, user);
                    }
                })
                .setLayoutRes(R.layout.alert_dialog)
                .setDimAmount(0.5f)
                .setTag("BottomDialog")
                .show();
    }

    private void initDialogView(View view, final User user) {

        TextView name = (TextView) view.findViewById(R.id.name);
        TextView tel = (TextView) view.findViewById(R.id.phoneNumber);
        TextView quXian = (TextView) view.findViewById(R.id.quXian);
        TextView department = (TextView) view.findViewById(R.id.department);
        TextView address = (TextView) view.findViewById(R.id.unit_address);
        TextView phone = (TextView) view.findViewById(R.id.unit_phone);
        TextView fax = (TextView) view.findViewById(R.id.unit_fax);
        TextView zipCode = (TextView) view.findViewById(R.id.unit_zipcode);
        ImageView callBtn = (ImageView) view.findViewById(R.id.call);
        name.setText(user.getUserName());
        tel.setText(user.getTel());
        quXian.setText(user.getUnit().getQuXian());
        department.setText(user.getLevel().getDepartmentName());
        address.setText(user.getUnit().getAddress());
        phone.setText(user.getUnit().getTel());
        fax.setText(user.getUnit().getFax());
        zipCode.setText(user.getUnit().getZipCode());
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + user.getTel()));
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 0);
                    }
                    return;
                }
                startActivity(intent);
            }
        };
        callBtn.setOnClickListener(listener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] paramArrayOfInt) {
        if (requestCode == 0) {
            if (!verifyPermissions(paramArrayOfInt)) {
                startActivity(intent);
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
        } else if (item.getItemId() == R.id.byName) {
            //按姓氏分组
            changePinying(0);
        } else if (item.getItemId() == R.id.byUnit) {
            //按单位分组
            changePinying(1);
        }
        return true;
    }

    private void changePinying(int flag) {
        String[] names = {"海光", "中信", "崇明", "恒申"};
        Random random = new Random();
        if (flag == 0) {
            adapter = adapterByName;
        } else {
            List<User> users = adapter.getData();
            for (User user : users) {
                user.setUnitName(names[random.nextInt(4)]);
                user.changePinying(flag);
            }
            Collections.sort(users);
            if (anotherAdapter == null) {
                anotherAdapter = new AnotherSortAdapter(this, users);
            }
            adapter = anotherAdapter;
        }
        listView.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
    }

    private void showHelp() {
// targetView 目标按钮 tipView添加的提示布局 可以直接找到'我知道了'按钮添加监听事件等处理
        mHightLight = new HighLight(MainActivity.this)//
                .autoRemove(false)//设置背景点击高亮布局自动移除为false 默认为true
//                .intercept(false)//设置拦截属性为false 高亮布局不影响后面布局的滑动效果
                .intercept(true)//拦截属性默认为true 使下方ClickCallback生效
                .enableNext()//开启next模式并通过show方法显示 然后通过调用next()方法切换到下一个提示布局，直到移除自身
                .anchor(findViewById(R.id.container))//如果是Activity上增加引导层，不需要设置anchor
                .addHighLight(R.id.spinner_one, R.layout.info_known_1, new OnBottomPosCallback(45), new RectLightShape(0, 0, 15, 0, 0))//矩形去除圆角
                .addHighLight(R.id.edit_search, R.layout.info_known, new OnBottomPosCallback(45), new RectLightShape(0, 0, 15, 0, 0))//矩形去除圆角
//                .addHighLight(R.id.help, R.layout.info_known, new OnBottomPosCallback(45), new RectLightShape(0, 0, 15, 0, 0))//矩形去除圆角
                .setOnRemoveCallback(new HighLightInterface.OnRemoveCallback() {//监听移除回调
                    @Override
                    public void onRemove() {
                        SPUtils.put(MainActivity.this, "isFirst", false);
                    }
                })
                .setOnShowCallback(new HighLightInterface.OnShowCallback() {//监听显示回调
                    @Override
                    public void onShow(HightLightView hightLightView) {
                        Toast.makeText(MainActivity.this, "The HightLight view has been shown", Toast.LENGTH_SHORT).show();
                    }
                }).setOnNextCallback(new HighLightInterface.OnNextCallback() {
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
        isShowHelp();
    }
}
