package com.dou361.live.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.dou361.baseutils.utils.LogUtils;
import com.dou361.live.R;
import com.dou361.live.bean.AnchorBean;
import com.dou361.live.ui.activity.LoginActivity;
import com.dou361.live.ui.activity.MessageActivity;
import com.dou361.live.ui.activity.PersonDataActivity;
import com.dou361.live.ui.activity.SearchActivity;
import com.dou361.live.ui.application.BaseApplication;
import com.dou361.live.ui.config.LiveConfig;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

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
 * 创建日期：2016/10/4 13:33
 * <p>
 * 描 述：我的
 * <p>
 * <p>
 * 修订历史：
 * <p>
 * ========================================
 */
public class MineFragment extends BaseFragment {
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.frame_rate)
    TextView frameRateText;
    @BindView(R.id.tv_username)
    TextView usernameView;

    LiveConfig liveConfig;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        usernameView.setText(EMClient.getInstance().getCurrentUser());
        liveConfig = new LiveConfig(getContext());
        final String[] bitrateArr = getResources().getStringArray(R.array.bitrate_types);
        String curBitrate = String.valueOf(liveConfig.getVideoEncodingBitRate());
        for (int i = 0; i < bitrateArr.length; i++) {
            if (curBitrate.equals(bitrateArr[i]))
                spinner.setSelection(i);
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                liveConfig.setVideoEncodingBitRate(Integer.parseInt(bitrateArr[position]));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @OnClick({R.id.eiv_avatar, R.id.iv_search, R.id.iv_msg, R.id.btn_logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.eiv_avatar:
                startActivity(PersonDataActivity.class);
                break;
            case R.id.iv_search:
                startActivity(SearchActivity.class);
                break;
            case R.id.iv_msg:
                AnchorBean anchorBean = new AnchorBean();
                anchorBean.setAnchorId(EMClient.getInstance().getCurrentUser());
                anchorBean.setName(EMClient.getInstance().getCurrentUser());
                startActivity(MessageActivity.class, anchorBean);
                break;
            case R.id.frame_rate_container:
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                final EditText editText = new EditText(getContext());
                editText.setText(frameRateText.getText());
                editText.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
                builder.setTitle("修改直播帧率").setView(editText)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                frameRateText.setText(editText.getText());
                                liveConfig.setVideoFrameRate(Integer.parseInt(editText.getText().toString()));
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                break;
            case R.id.btn_logout:
                EMClient.getInstance().logout(false, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        BaseApplication.getInstance().finishAll();
                        startActivity(LoginActivity.class);
                        LogUtils.logTagName(TAG).log("-----退出登录成功-----");
                    }

                    @Override
                    public void onError(int i, String s) {
                        LogUtils.logTagName(TAG).log("-----退出登录失败-----" + s);
                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
                break;
        }
    }

}
