package com.bokun.bkjcb.infomationmanage.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bokun.bkjcb.infomationmanage.Adapter.SimpleExpandAdapter;
import com.bokun.bkjcb.infomationmanage.Domain.User;
import com.bokun.bkjcb.infomationmanage.R;
import com.bokun.bkjcb.infomationmanage.SQL.DBManager;

import java.util.ArrayList;

import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by DengShuai on 2017/8/22.
 */

public class MainFragment extends BaseFragment implements View.OnClickListener {
    private static MainFragment fragment;
    private ImageView searchButton, cancelButton;
    private EditText editText;
    private String keyWord;
    private SimpleExpandAdapter adapter;
    private ExpandableListView listView;
    private SegmentedGroup group;
    private boolean searchFlag = false;
    private ProgressDialog dialog;
    private ArrayList<User> users;

    public static MainFragment newInstance() {
        if (fragment == null) {
            fragment = new MainFragment();
        }
        return fragment;
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_one, null);
        searchButton = (ImageView) view.findViewById(R.id.image_search);
        cancelButton = (ImageView) view.findViewById(R.id.clearSearch);
        editText = (EditText) view.findViewById(R.id.edit_search);
        listView = (ExpandableListView) view.findViewById(R.id.listView);
        group = (SegmentedGroup) view.findViewById(R.id.segmented);

        listView.setGroupIndicator(null);
        return view;
    }

    @Override
    protected void setListener() {
        listView.setOnChildClickListener(null);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                keyWord = charSequence.toString().trim();
                if (adapter == null) {
                    return;
                }
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
                    ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    return true;
                }
                return false;
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
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                activity.showDetail(adapter.getUser(groupPosition,childPosition));
                return false;
            }
        });
        if (adapter == null) {
            new LoadDataTask().execute();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_search:
                ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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

    class LoadDataTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            createDialog().show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            users = DBManager.newInstance(getContext()).queryAllUser();
//            Collections.sort(users);
            adapter = new SimpleExpandAdapter(getContext(), users);
            adapter.setListView(listView);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            listView.setAdapter(adapter);
            listView.setTextFilterEnabled(true);
//            dialog.dismiss();
        }
    }

    private ProgressDialog createDialog() {
        if (dialog == null) {
            dialog = new ProgressDialog(getContext());
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            dialog.setMessage("请稍等");
        }
        return dialog;
    }
}
