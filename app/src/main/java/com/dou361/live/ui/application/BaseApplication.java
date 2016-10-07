package com.dou361.live.ui.application;

import android.app.Application;
import android.os.Handler;

import com.dou361.baseutils.utils.LogUtils;
import com.dou361.baseutils.utils.UtilsManager;
import com.dou361.live.ui.activity.BaseActivity;
import com.hyphenate.easeui.controller.EaseUI;
import com.ucloud.live.UEasyStreaming;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by wei on 2016/5/27.
 */
public class BaseApplication extends Application {

    private static BaseApplication instance;
    /**
     * 记录所有活动的Activity
     */
    private static final List<BaseActivity> mActivities = new LinkedList<BaseActivity>();

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        /**工具类库初始化*/
        UtilsManager.init(this, "", new Handler(), Thread.currentThread());
        UtilsManager.getInstance().setDebugEnv(true);
        UtilsManager.getInstance().setLogLevel(LogUtils.LogType.LEVEL_ERROR);
        /**环信IM初始化*/
        EaseUI.getInstance().init(this, null);
        /**ucloud直播sdk初始化*/
        UEasyStreaming.initStreaming("publish3-key");

    }

    public static BaseApplication getInstance() {
        return instance;
    }


    /**
     * 关闭所有Activity
     */
    public static void finishAll() {
        List<BaseActivity> copy;
        synchronized (mActivities) {
            copy = new ArrayList<BaseActivity>(mActivities);
        }
        for (BaseActivity activity : copy) {
            activity.finish();
        }
        mActivities.clear();
    }

    /**
     * 添加页面
     */
    public static void addActivity(BaseActivity activity) {
        for (int i = 0; i < mActivities.size(); i++) {
            if (mActivities.get(i) == activity) {
                mActivities.remove(i);
                break;
            }
        }
        mActivities.add(activity);
    }

    /**
     * 添加页面
     */
    public static void finishActivity(BaseActivity activity) {
        for (int i = 0; i < mActivities.size(); i++) {
            if (mActivities.get(i) == activity) {
                activity.finish();
                mActivities.remove(i);
                break;
            }
        }

    }

    /**
     * 关闭所有Activity，除了参数传递的Activity
     */
    public static void finishAll(BaseActivity except) {
        List<BaseActivity> copy;
        synchronized (mActivities) {
            copy = new ArrayList<BaseActivity>(mActivities);
        }
        for (BaseActivity activity : copy) {
            if (activity != except)
                activity.finish();
        }
    }

    /**
     * 是否有启动的Activity
     */
    public static boolean hasActivity() {
        return mActivities.size() > 0;
    }

    /**
     * 是否有其他启动的Activity除掉当前的
     */
    public static boolean hasElseActivity(BaseActivity activity) {
        int i = 0;
        for (; i < mActivities.size(); i++) {
            if (mActivities.get(i) == activity) {
                break;
            }
        }
        return mActivities.size() != i;
    }

    /**
     * 获取当前处于栈顶的activity，无论其是否处于前台
     */
    public static BaseActivity getCurrentActivity() {
        List<BaseActivity> copy;
        synchronized (mActivities) {
            copy = new ArrayList<BaseActivity>(mActivities);
        }
        if (copy.size() > 0) {
            return copy.get(copy.size() - 1);
        }
        return null;
    }

    /**
     * 退出应用
     */
    public void exitApp() {
        finishAll();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}
