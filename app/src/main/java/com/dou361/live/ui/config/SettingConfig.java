package com.dou361.live.ui.config;


import com.dou361.baseutils.utils.SPUtils;
import com.dou361.baseutils.utils.UIUtils;

/**
 * ========================================
 * <p/>
 * 版 权：dou361.com 版权所有 （C） 2015
 * <p/>
 * 作 者：陈冠明
 * <p/>
 * 个人网站：http://www.dou361.com
 * <p/>
 * 版 本：1.0
 * <p/>
 * 创建日期：2016/9/12 9:55
 * <p/>
 * 描 述：常用设置配置
 * <p/>
 * <p/>
 * 修订历史：
 * <p/>
 * ========================================
 */
public class SettingConfig {


    /**
     * 是否登录
     */
    public static void putLogin(boolean flag) {
        SPUtils.putData(UIUtils.getContext(), StatusConfig.ISLOGIN, flag);
    }

    /**
     * 是否登录
     */
    public static boolean isLogin() {
        return (boolean) SPUtils.getData(UIUtils.getContext(), StatusConfig.ISLOGIN, false);
    }

    /**
     * 设置非第一次启动应用，true为非第一次启动，false为第一次启动
     */
    public static void putNotFirstBoot(boolean flag) {
        SPUtils.putData(UIUtils.getContext(), StatusConfig.ISFIRST, flag);
    }

    /**
     * 判断是否非第一次启动应用，true为非第一次启动，false为第一次启动
     */
    public static boolean isNotFirstBoot() {
        return (boolean) SPUtils.getData(UIUtils.getContext(), StatusConfig.ISFIRST, false);
    }

    /**
     * 设置版本号
     */
    public static void putVersionCode(int flag) {
        SPUtils.putData(UIUtils.getContext(), StatusConfig.PREVERSION_CODE, flag);
    }

    /**
     * 获取版本号
     */
    public static int getVersionCode() {
        return (int) SPUtils.getData(UIUtils.getContext(), StatusConfig.PREVERSION_CODE, 0);
    }

    /**
     * 设置多用户当前选择的id
     */
    public static void putUserId(String flag) {
        SPUtils.putData(UIUtils.getContext(), StatusConfig.STR_USER_ID, flag);
    }

    /**
     * 获取多用户当前选择的id
     */
    public static String getUserId() {
        return (String) SPUtils.getData(UIUtils.getContext(), StatusConfig.STR_USER_ID, "");
    }

}
