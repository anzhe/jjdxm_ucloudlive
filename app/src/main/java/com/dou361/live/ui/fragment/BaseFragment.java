package com.dou361.live.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dou361.baseutils.utils.LogUtils;
import com.dou361.live.bean.AnchorBean;
import com.dou361.live.bean.LiveRoom;
import com.dou361.live.ui.config.StatusConfig;

import butterknife.ButterKnife;
import butterknife.Unbinder;

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
 * 创建日期：2016/10/4 12:57
 * <p>
 * 描 述：fragment基类
 * <p>
 * <p>
 * 修订历史：
 * <p>
 * ========================================
 */
public abstract class BaseFragment extends Fragment {
    private Unbinder unbinder;
    /**
     * 当前调用类的类标识
     */
    protected String TAG = this.getClass().getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = initView(inflater, container);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    public abstract View initView(LayoutInflater inflater, ViewGroup container);

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.logTagName(TAG).log(TAG + "-----onResume-----");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtils.logTagName(TAG).log(TAG + "-----onPause-----");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * 启动activity
     */
    protected void startActivity(Class<?> clazz) {
        Intent intent = new Intent(getActivity(), clazz);
        startActivity(intent);
    }

    /**
     * 启动activity
     */
    protected void startActivity(Class<?> clazz, AnchorBean anchorBean) {
        Intent intent = new Intent(getActivity(), clazz);
        intent.putExtra(StatusConfig.ARG_ANCHOR, anchorBean);
        startActivity(intent);
    }

    /**
     * 启动activity
     */
    protected void startActivity(Class<?> clazz, LiveRoom liveRoom) {
        Intent intent = new Intent(getActivity(), clazz);
        intent.putExtra(StatusConfig.LiveRoom, liveRoom);
        startActivity(intent);
    }
}
