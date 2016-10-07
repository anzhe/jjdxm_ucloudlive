package com.dou361.live.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.dou361.baseutils.utils.LogUtils;
import com.dou361.baseutils.utils.UIUtils;
import com.dou361.live.R;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * ========================================
 * <p>
 * 版 权：dou361.com 版权所有 （C） 2015
 * <p>
 * 作 者：陈冠明
 * <p>
 * 个人网站：http://www.dou361.com
 * <p>
 * 版 本：1.0
 * <p>
 * 创建日期：2016/10/5 15:01
 * <p>
 * 描 述：注册
 * <p>
 * <p>
 * 修订历史：
 * <p>
 * ========================================
 */
public class RegisterActivity extends BaseActivity {

    @BindView(R.id.et_account)
    EditText username;
    @BindView(R.id.et_password)
    EditText password;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_register);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @OnClick({R.id.btn_register})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                if (TextUtils.isEmpty(username.getText()) || TextUtils.isEmpty(password.getText())) {
                    UIUtils.showToastCenterShort("用户名和密码不能为空");
                    return;
                }
                final ProgressDialog pd = new ProgressDialog(RegisterActivity.this);
                pd.setMessage("正在注册...");
                pd.setCanceledOnTouchOutside(false);
                pd.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            EMClient.getInstance().createAccount(username.getText().toString(), password.getText().toString());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pd.dismiss();
                                    UIUtils.showToastCenterShort("注册成功");
                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                    LogUtils.logTagName(TAG).log("-----注册成功-----");
                                    onBackPressed();
                                }
                            });
                        } catch (final HyphenateException e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pd.dismiss();
                                    UIUtils.showToastCenterShort("注册失败：" + e.getMessage());
                                    LogUtils.logTagName(TAG).log("-----注册失败-----" + e.getMessage());
                                }
                            });
                        }
                    }
                }).start();
                break;
        }
    }
}
