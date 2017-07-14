package com.bokun.bkjcb.infomationmanage.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bokun.bkjcb.infomationmanage.Adapter.SortAdapter;
import com.bokun.bkjcb.infomationmanage.Domain.User;
import com.bokun.bkjcb.infomationmanage.R;
import com.bokun.bkjcb.infomationmanage.SQL.DBManager;
import com.bokun.bkjcb.infomationmanage.View.AlertView;
import com.bokun.bkjcb.infomationmanage.View.SideBar;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

    @Override
    protected void setView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initView() {
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
    }

    @Override
    protected void loadData() {
        users = DBManager.newInstance(this).queryAllUser();
        Collections.sort(users);
        adapter = new SortAdapter(this, users);
        listView.setAdapter(adapter);
        list1 = new ArrayList<>(Arrays.asList("全部", "管理单位", "企业单位"));
        list2 = new ArrayList<>(Arrays.asList("全部", "市级", "区级"));
        sp_1.attachDataSource(list1);
        listView.setTextFilterEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        final User user = adapter.getData().get(i);
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
        View alertView = new AlertView().build(user, this, listener);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(alertView);
        builder.show();
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
}
