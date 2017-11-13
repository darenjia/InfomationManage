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
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bokun.bkjcb.infomationmanage.Adapter.ExpandAdapter;
import com.bokun.bkjcb.infomationmanage.Domain.User;
import com.bokun.bkjcb.infomationmanage.R;
import com.bokun.bkjcb.infomationmanage.SQL.DBManager;
import com.bokun.bkjcb.infomationmanage.Utils.L;
import com.bokun.bkjcb.infomationmanage.Utils.StringFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import info.hoang8f.android.segmented.SegmentedGroup;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by DengShuai on 2017/8/22.
 */

public class MainFragment extends BaseFragment implements View.OnClickListener {
    private static MainFragment fragment;
    private ImageView searchButton, cancelButton;
    private EditText editText;
    private String keyWord;
    private ExpandAdapter adapter;
    private ExpandableListView listView;
    private SegmentedGroup group;
    private boolean searchFlag = true;
    private ProgressDialog dialog;
    private ArrayList<User> users;
    private LinearLayout layout;
    private StringFilter filter;
    private Disposable disposable;

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
        layout = (LinearLayout) view.findViewById(R.id.search_progress);

        listView.setGroupIndicator(null);
        return view;
    }

    private void showProgress() {
        layout.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
    }

    private void hideProgress() {
        layout.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
    }


    @Override
    protected void setListener() {
        listView.setOnChildClickListener(null);
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
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                activity.showDetail(adapter.getUser(groupPosition, childPosition));
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
//                    adapter.replaceData(users);
                    cancelButton.setImageResource(R.mipmap.ic_search);
                    searchButton.setVisibility(View.GONE);
                    editText.setText("");
                }
                break;
        }
    }

    private Observable<String> createTextChangeObservable() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> e) throws Exception {
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (!s.toString().equals("")) {
                            cancelButton.setImageResource(R.mipmap.ic_close);
//                            searchButton.setVisibility(View.VISIBLE);
                        } else {
                            cancelButton.setImageResource(R.mipmap.ic_search);
                        }
                        e.onNext(s.toString().trim());
                    }
                });
            }
        }).debounce(500, TimeUnit.MILLISECONDS);
    }

    private Observable<String> createStateChangeObservable() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> e) throws Exception {
                group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if (checkedId == R.id.button1) {
                            editText.setHint("按姓名查找");
                            searchFlag = true;
                        } else {
                            editText.setHint("按单位查找");
                            searchFlag = false;
                        }
                        e.onNext(editText.getText().toString().trim());
                        L.i(editText.getText().toString().trim());
                    }
                });
            }
        }).filter(new Predicate<String>() {
            @Override
            public boolean test(String s) throws Exception {
                return !s.equals("");
            }
        }).debounce(500, TimeUnit.MILLISECONDS);
    }

    class LoadDataTask extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            users = DBManager.newInstance(getContext()).queryAllUser();
//            Collections.sort(users);
            adapter = new ExpandAdapter(getContext(), users);
            adapter.setListView(listView);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            listView.setAdapter(adapter);
            filter = new StringFilter(users);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (!disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Observable<String> textChangeObservable = createTextChangeObservable();
        Observable<String> stateChangeObservable = createStateChangeObservable();
        Observable<String> observable = io.reactivex.Observable.merge(textChangeObservable, stateChangeObservable);
        disposable = observable
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        showProgress();
                    }
                })
                .observeOn(Schedulers.io())
                .map(new Function<String, List<User>>() {
                    @Override
                    public List<User> apply(String s) throws Exception {
                        return filter.filter(searchFlag, s);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<User>>() {
                    @Override
                    public void accept(List<User> strings) throws Exception {
                        adapter.replaceData(strings);
                        hideProgress();
                    }
                });
    }

}
