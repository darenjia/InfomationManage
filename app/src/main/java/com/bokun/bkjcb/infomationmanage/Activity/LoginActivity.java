package com.bokun.bkjcb.infomationmanage.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.bokun.bkjcb.infomationmanage.Domain.User;
import com.bokun.bkjcb.infomationmanage.Domain.VersionInfo;
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

import org.greenrobot.eventbus.EventBus;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

public class LoginActivity extends BaseActivity implements RequestListener {

    private UserLoginTask mAuthTask = null;
    private User admin;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mLoginFormView;
    private ProgressDialog dialog;
    private CheckBox checkBox;
    private HttpManager manager;
    private Button btn_ig;
    private Button btn_con;
    private TextView info;
    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;
    private String date;
    private TextView time;
    private boolean hasNew;
    private Button mEmailSignInButton;
    private String password;
    private AlertDialog userDialog;
    private int userId;
    private ArrayList<User> users;
    private String userName;

    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        /*if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }*/

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        } /*else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }*/

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
            showProgress();
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mPasswordView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private boolean isEmailValid(String email) {
        return true;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }


    @Override
    protected void onStart() {
        super.onStart();
        checkUpdate();
//        EventBus.getDefault().post(new DefaultEvent(DefaultEvent.SOFT_NEED_UPDATE));
    }

    @Override
    protected void setView() {
        setContentView(R.layout.activity_login);
        setTitle(R.string.title_activity_main);
    }

    @Override
    protected void initView() {
        mEmailView = (EditText) findViewById(R.id.username);
        checkBox = (CheckBox) findViewById(R.id.rem_btn);
        mPasswordView = (EditText) findViewById(R.id.password);
        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mLoginFormView = findViewById(R.id.login_form);
    }

    @Override
    protected void setListener() {
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        mEmailView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // et.getCompoundDrawables()得到一个长度为4的数组，分别表示左右上下四张图片
                Drawable drawable = mEmailView.getCompoundDrawables()[2];
                //如果右边没有图片，不再处理
                if (drawable == null)
                    return false;
                //如果不是按下事件，不再处理
                if (event.getAction() != MotionEvent.ACTION_UP)
                    return false;
                if (event.getX() > mEmailView.getWidth()
                        - mEmailView.getPaddingRight()
                        - drawable.getIntrinsicWidth()) {
                    showMutiUserDialog(users);
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    protected void loadData() {
        userName = (String) SPUtils.get(this, "UserName", "");

        if (!TextUtils.isEmpty(userName)) {
            checkBox.setChecked(true);
            mEmailView.setText(userName);
            mPasswordView.setText((String) SPUtils.get(this, "Password", ""));
            userId = (int) SPUtils.get(this, "userId", 0);
            new LoadDataTask().execute();
        }
        mEmailSignInButton.requestFocus();
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Integer> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            if (TextUtils.isEmpty(userName) || !userName.equals(mEmail)) {
                users = DBManager.newInstance(LoginActivity.this).queryUserByLoginName(mEmail);
            }
            if (users.size() == 0) {
                return -1;
            } else if (users.size() > 1) {
                if (userId != 0) {
                    for (User u : users) {
                        if (u.getId() == userId) {
                            admin = u;
                        }
                    }
                    if (admin != null && (admin.getPassword() == null || admin.getPassword().equals(mPassword))) {
                        return 1;
                    }
                }
                return 2;
            } else {
                if (users.get(0).getPassword() == null || users.get(0).getPassword().equals(mPassword)) {
                    admin = users.get(0);
                    return 1;
                }
                return 0;
            }
           /* if (user.getPassword().equals(mPassword)) {
                return 1;
            }*/
        }

        @Override
        protected void onPostExecute(Integer success) {
            mAuthTask = null;
            if (success == 1) {
                remberInfo();
                toMainActivity();
                finish();
            } else if (success == 0) {
                hiddenProgress();
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            } else if (success == -1) {
                hiddenProgress();
                mEmailView.setError(getString(R.string.error_field_required));
                mEmailView.requestFocus();
            } else {
                hiddenProgress();
                setUserNameDrawable(true);
                showMutiUserDialog(users);
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            hiddenProgress();
        }
    }

    private class LoadDataTask extends AsyncTask<Void, Void, ArrayList<User>> {
        @Override
        protected ArrayList<User> doInBackground(Void... voids) {
            if (mEmailView.getText().toString() == null) {
                return null;
            }
            users = DBManager.newInstance(LoginActivity.this).queryUserByLoginName(mEmailView.getText().toString());
            if (users.size() > 1) {
                return users;
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<User> users) {
            super.onPostExecute(users);
            setUserNameDrawable(users != null);
        }
    }

    private void setUserNameDrawable(boolean flag) {
        if (flag) {
            Drawable drawable = getResources().getDrawable(R.drawable.more);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), (drawable.getMinimumHeight()));
            mEmailView.setCompoundDrawables(null, null, drawable, null);
        } else {
            mEmailView.setCompoundDrawables(null, null, null, null);
        }
    }

    //有多个用户是弹框
    private void showMutiUserDialog(final ArrayList<User> users) {
        int checkedItem = 0;
        if (userDialog == null) {
            AlertDialog.Builder userBuilder = new AlertDialog.Builder(this);
            View view = View.inflate(this, R.layout.user_dialog_title, null);
            userBuilder.setCustomTitle(view);
            User user;
            String s;
            ArrayList<String> strings = new ArrayList<>();
            for (int i = 0; i < users.size(); i++) {
                user = users.get(i);
                s = user.getUserName() + "(" + user.getDuty() + ")";
                strings.add(s);
                if (user.getId() == userId) {
                    checkedItem = i;
                }
            }
            String[] items = new String[strings.size()];
            items = strings.toArray(items);
            userBuilder.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    /*if (users.get(which).getPassword() == null || users.get(which).getPassword().equals(password)) {
                        admin = users.get(which);
                        remberInfo();
                        toMainActivity();
                    } else {
                        mPasswordView.setError(getString(R.string.error_incorrect_password));
                        mPasswordView.requestFocus();
                    }*/
                    admin = users.get(which);
                    userId = admin.getId();
                    dialog.dismiss();
                }
            });
            userDialog = userBuilder.create();
            userDialog.setCanceledOnTouchOutside(false);
        }
        userDialog.show();
    }

    private void showProgress() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("登录中");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
    }

    private void hiddenProgress() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private void toMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("User", admin);
        intent.putExtra("hasNew", hasNew);
        L.i("Login:" + hasNew);
        startActivity(intent);
    }

    private void remberInfo() {
        if (checkBox.isChecked()) {
            SPUtils.put(LoginActivity.this, "UserName", mEmailView.getText().toString());
            SPUtils.put(LoginActivity.this, "Password", mPasswordView.getText().toString());
            SPUtils.put(LoginActivity.this, "userId", admin.getId());
        } else {
            SPUtils.put(LoginActivity.this, "UserName", "");
            SPUtils.put(LoginActivity.this, "Password", "");
            SPUtils.put(LoginActivity.this, "userId", 0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hiddenProgress();
        DBManager.newInstance(this).close();
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
    protected void handlerEvent(DefaultEvent event) {
        date = Utils.getDate();
        SPUtils.put(this, "Time", date);
        if (builder == null) {
            builder = new AlertDialog.Builder(LoginActivity.this);
        }
        switch (event.getState_code()) {
            case DefaultEvent.SOFT_NEED_UPDATE:
                hasNew = true;
                builder.setView(createRefreshView(true));
                alertDialog = builder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();
                break;
            case DefaultEvent.DATA_NEED_UPDATE:
                hasNew = true;
                builder.setView(createRefreshView(false));
                alertDialog = builder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();
                break;
            case DefaultEvent.GET_DATA_NULL:
                hasNew = false;
                break;
            case DefaultEvent.GET_DATA_SUCCESS:
                hasNew = false;
                break;
        }
        L.i(hasNew+"??");
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
                alertDialog.dismiss();
//                hasNew = true;
            }
        });
        btn_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                UpdateActivity.comeIn(type ? 1 : 2, LoginActivity.this);
            }
        });
        return view;
    }

}

