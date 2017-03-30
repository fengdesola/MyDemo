package com.sola.mydemo;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView;
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerViewAdapter;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.sola.baselib.http.RetrofitClient;
import com.sola.baselib.model.http.ResultModel;
import com.sola.baselib.util.EffectUtil;
import com.sola.baselib.util.MD5;
import com.sola.baselib.util.NetworkUtil;
import com.sola.baselib.widget.dialog.LoadingDialog;
import com.sola.mydemo.http.Parser;
import com.sola.mydemo.model.user.LoginUser;
import com.sola.mydemo.ui.base.BaseActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Button btnPhone;
    private Button btnCount;
    private TextView tvEffect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
//        mEmailSignInButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                attemptLogin();
//            }
//        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        btnPhone = (Button) findViewById(R.id.btnPhone);
        btnCount = (Button) findViewById(R.id.btnCount);
        tvEffect = (TextView) findViewById(R.id.tvEffect);


        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.setLogging(true);
        
        RxView.clicks(btnPhone).throttleFirst(500, TimeUnit.MILLISECONDS)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .compose(rxPermissions.ensure(Manifest.permission.CALL_PHONE))
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Boolean granted) throws Exception {
                        if(granted){
                            showToast("permission success");
                            System.out.println("permission success");
                            System.out.println("thread ==== " + Thread.currentThread().getName());
                            doTest();
                        }else{
                            System.out.println("permission fail");
                        }
                    }
                });

        RxView.clicks(mEmailSignInButton).throttleFirst(500, TimeUnit.MILLISECONDS)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Object o) throws Exception {
                        login();
                    }
                });

       
        RxView.clicks(btnCount).throttleFirst(20, TimeUnit.SECONDS)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Object o) throws Exception {
                        RxView.enabled(btnCount).accept(false);
                        doCount();
                    }
                });
        
        RxView.clicks(tvEffect).subscribe(new Consumer<Object>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Object o) throws Exception {
                showToast("click effect");
            }
        });
        EffectUtil.addClickEffect(tvEffect);
        
    }

    private void doCount() {
        Observable.interval(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .take(20)
                .compose(this.<Long>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        try {
                            RxTextView.text(btnCount).accept("count " + (20-aLong));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        try {
                            RxTextView.text(btnCount).accept("count click");
                            RxView.enabled(btnCount).accept(true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    private void doTest() {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                System.out.println("do test");
                System.out.println("thread ==== " + Thread.currentThread().getName());
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).subscribe();

        
        
    }

    private void login() {
        if(!NetworkUtil.isNetworkAvailable()){
            showToast("网络未打开");
            return;
        }
        loginObservable("13600001111", "123456")
                .map(new Function<String, ResultModel<LoginUser>>() {
                    @Override
                    public ResultModel<LoginUser> apply(@io.reactivex.annotations.NonNull String s) throws Exception {
                        return Parser.getInstance().parserData(s, LoginUser.class, Parser.TYPE.OBJECT);
                    }
                })
                .delay(2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Subscription>() {//subscribe()调用后而且在事件发送前执行(默认情况下subcribe发生的线程决定，可以通过最近的跟在后面的subscribeOn指定)
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Subscription subscription) throws Exception {
                        showLoadingDialog();
                        System.out.println("===========show progress");
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())//指定doOnSubscribe的线程
                .compose(RxLifecycle.bindUntilEvent(lifecycle(), ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new FlowableSubscriber<Object>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);
                        System.out.println("===========onSubscribe");
                    }

                    @Override
                    public void onNext(Object o) {
                        ResultModel<LoginUser> result = (ResultModel<LoginUser>) o;
                        showToast(result.data.displayName);
                        System.out.println("===========next");

                    }

                    @Override
                    public void onError(Throwable t) {
                        System.out.println("===========err");
                        t.printStackTrace();
                        dismissLoadingDialog();
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("===========complete");
                        dismissLoadingDialog();
                    }
                });
    }

    public Flowable<String> loginObservable(String userName, String pwd) {

        ArrayMap<String, Object> body = new ArrayMap<>();
        body.put("tenantId", "coms.xiangtan");
        body.put("loginName", userName);
        body.put("rid", "patient");
        body.put("pwd", MD5.getMD5(pwd));
        body.put("forAccessToken", true);
        return RetrofitClient.getInstance().post2("logon/login", null, body);
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
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

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

