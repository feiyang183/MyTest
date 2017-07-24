package com.example.dell.myscada.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.myscada.Base.BaseActivity;
import com.example.dell.myscada.Data.DBDefine;
import com.example.dell.myscada.Data.DBFactory;
import com.example.dell.myscada.R;
import com.example.dell.myscada.Service.AlmService;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Dell on 2017/3/10.
 */
public class LoginActivity extends BaseActivity {

    private LoginTask mAuthTask = null;
//    private User user;

    private final static String TAG ="Login--->";
    // UI references.
    private EditText mUserView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private CheckBox Cbuserinfo;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;


    TextInputLayout usernameWrapper;
    TextInputLayout passwordWrapper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.

        final TextInputLayout usernameWrapper=(TextInputLayout) findViewById(R.id.usernameWrapper);
        final TextInputLayout passwordWrapper=(TextInputLayout) findViewById(R.id.passwordWrapper);

        usernameWrapper.setHint("用户名");
        passwordWrapper.setHint("密码");

        pref = PreferenceManager.getDefaultSharedPreferences(this);

        mUserView = (EditText) findViewById(R.id.user);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    Log.d(TAG,"onEditorAction");
                    if (isNetworkConnected(getApplicationContext())){
                        attemptLogin();
                    }
                    return true;
                }
                return false;
            }
        });

        Cbuserinfo = (CheckBox) findViewById(R.id.userInfo);

        Button mUserSignInButton = (Button) findViewById(R.id.user_sign_in_button);
        mUserSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                Log.d(TAG,"Button");
                if (isNetworkConnected(getApplicationContext())){
                    attemptLogin();
                }
            }
        });

        boolean isSaveInfo = pref.getBoolean("remember_password", false);
        if (isSaveInfo){
            String account = pref.getString("account", "");
            String password = pref.getString("password", "");
            mUserView.setText(account);
            mPasswordView.setText(password);
            Cbuserinfo.setChecked(true);
        }

        mLoginFormView = findViewById(R.id.user_login_form);
        mProgressView = findViewById(R.id.login_progress);

    }


    //    隐藏键盘
    private void hideKeyboard(){
        View view = getCurrentFocus();
        if(view != null){
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(view.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUserView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String user = mUserView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            Log.d(TAG,"password isEmpty?");
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(user)) {
            Log.d(TAG,"user isEmpty?");
            mUserView.setError(getString(R.string.error_field_required));
            focusView = mUserView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            Log.d(TAG,"focusView");
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            Log.d(TAG,"showProgress-->true");
            mAuthTask = new LoginTask(user, password);
            mAuthTask.execute((Void) null);
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class LoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUser;
        private final String mPassword;
        private DBDefine dbDefine;
        private DBFactory dbFactory;

        private final static String SERVER_NAME = "jdbc:jtds:sqlserver://222.243.104.7:5555/LIZP_DB";
        private final static String LOGIN_NAME = "sa";
        private final static String LOGIN_PASSWORD = "apptech@0520*%";

        LoginTask(String user, String password) {
            mUser = user;
            mPassword = password;
            initDataBase();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                dbFactory.selectAccount();
                // Simulate network access.
                List<String> result = dbFactory.getList();
                for (String account : result){
                    String[] list = String.valueOf(account).split("@_@@");
                    if (mUser.equals(list[0]) && mPassword.equals(list[1])){
                        return true;
                    }
                }
            } catch (SQLException e){
                e.printStackTrace();
            }
            Log.d(TAG,"doInBackground");
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            if (success) {
                //TODO 页面跳转
                Log.d(TAG,"finish()");
//                Intent intent = new Intent();
//                intent.setClass(LoginActivity.this, MainActivity.class);
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivity(intent);
                editor = pref.edit();
                if (Cbuserinfo.isChecked()){
                    editor.putBoolean("remember_password", true);
                    editor.putString("account", mUser);
                    editor.putString("password", mPassword);
                }else {
                    editor.clear();
                }
                editor.apply();
                Intent startIntent = new Intent(getBaseContext(), AlmService.class);
                startService(startIntent);
                actionStart(getBaseContext(),mUser,mPassword);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "密码或帐号错误", Toast.LENGTH_LONG).show();
                Log.d(TAG,"密码错误");
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }

        private void initDataBase(){
            dbDefine = new DBDefine();
            dbDefine.setServer(SERVER_NAME);
            dbDefine.setUsername(LOGIN_NAME);
            dbDefine.setPassword(LOGIN_PASSWORD);
            dbFactory = new DBFactory(dbDefine);
        }
    }

    @Override
    protected void onDestroy(){
        if (mAuthTask != null && mAuthTask.getStatus() != AsyncTask.Status.FINISHED)
            mAuthTask.cancel(true);
        super.onDestroy();
    }

    public static void actionStart(Context context, String mUser, String mPassword){
        Intent intent = new Intent(context,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("mUser",mUser);
        intent.putExtra("mPassword",mPassword);
        context.startActivity(intent);
    }


    public boolean isNetworkConnected(Context context){
        if (context != null){
            ConnectivityManager connectivityManager = (ConnectivityManager) context.
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if(mNetworkInfo != null){
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}

