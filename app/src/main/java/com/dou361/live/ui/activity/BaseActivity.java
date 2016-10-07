package com.dou361.live.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dou361.baseutils.utils.LogUtils;
import com.dou361.live.R;
import com.dou361.live.ui.application.BaseApplication;
import com.dou361.statusbar.StatusBarUtil;
import com.dou361.statusbar.SwipeBackLayout;

import butterknife.ButterKnife;

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
 * 创建日期：2016/10/4 11:41
 * <p>
 * 描 述：activity基类
 * <p>
 * <p>
 * 修订历史：
 * <p>
 * ========================================
 */
public abstract class BaseActivity extends AppCompatActivity {

    /**
     * 当前Activity
     */
    protected Activity activity;
    /**
     * 当前上下文
     */
    protected Context mContext;
    /**
     * 当前调用类的类标识
     */
    protected String TAG = this.getClass().getSimpleName();
    /**
     * 判断当前Activity是否是显示false为onResume()之后 true为onPause()之后
     */
    protected boolean mFgState;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * 只有继承了BaseActivity，Activity才会加入集合管理中
         */
        BaseApplication.getInstance().addActivity(this);
        if (openSliding()) {
            SwipeBackLayout layout = new SwipeBackLayout(this, null);
            layout.attachToActivity(this, SwipeBackLayout.CloseMode.PRESSBACK);
        }
        init();
        initView();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        if (openStatus()) {
            setStatuBar();
        }
    }

    /**
     * true 右滑关闭activity false无操作
     */
    protected boolean openSliding() {
        return true;
    }

    /**
     * 是否使用沉浸式状态栏
     */
    protected boolean openStatus() {
        return true;
    }

    /**
     * 设置状态栏背景色
     */
    protected void setStatuBar() {
        StatusBarUtil.setColorNoTranslucent(this, getResources().getColor(R.color.colorPrimaryDark));
    }

    @Override
    public void onBackPressed() {
        /** 点击返回键时关闭当前Activity */
        BaseApplication.getInstance().finishActivity(this);
        try {
            super.onBackPressed();
        } catch (Exception e) {

        }
    }

    @Override
    protected void onResume() {
        activity = this;
        mContext = this;
        mFgState = false;
        super.onResume();
        LogUtils.logTagName(TAG).log(TAG+"-----onResume-----");
    }

    @Override
    protected void onPause() {
        mFgState = true;
        super.onPause();
        LogUtils.logTagName(TAG).log(TAG+"-----onPause-----");
    }

    @Override
    protected void onDestroy() {
        /** 主动调用gc回收 */
        System.gc();
        super.onDestroy();
    }

    /**
     * 初始化对象
     */
    protected void init() {
        activity = this;
        mContext = this;
    }

    /**
     * 初始化View
     */
    protected abstract void initView();

    /**
     * 启动activity
     */
    protected void startActivity(Class<?> clazz) {
        Intent intent = new Intent(mContext, clazz);
        startActivity(intent);
    }
}
