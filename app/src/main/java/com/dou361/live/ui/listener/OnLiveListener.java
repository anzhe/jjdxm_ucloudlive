package com.dou361.live.ui.listener;

import android.view.View;

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
 * 创建日期：2016/10/7 13:10
 * <p>
 * 描 述：发起直播点击监听
 * <p>
 * <p>
 * 修订历史：
 * <p>
 * ========================================
 */
public interface OnLiveListener {

    /**
     * 点击切换摄像头
     */
    void onCamreClick(View view);

    /**
     * 点击切换闪光灯
     */
    void onLightClick(View view);

    /**
     * 点击切换声音
     */
    void onVoiceClick(View view);
}