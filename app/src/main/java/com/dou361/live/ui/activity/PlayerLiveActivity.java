package com.dou361.live.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dou361.baseutils.utils.LogUtils;
import com.dou361.customui.ui.AlertView;
import com.dou361.live.R;
import com.dou361.live.bean.LiveRoom;
import com.dou361.live.ui.adapter.RoomPanlAdapter;
import com.dou361.live.ui.config.StatusConfig;
import com.dou361.live.ui.fragment.RoomPanlFragment;
import com.dou361.live.ui.fragment.TransparentFragment;
import com.ucloud.common.logger.L;
import com.ucloud.player.widget.v2.UVideoView;

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
 * 创建日期：2016/10/4 14:17
 * <p>
 * 描 述：直播播放页面
 * <p>
 * <p>
 * 修订历史：
 * <p>
 * ========================================
 */
public class PlayerLiveActivity extends BaseActivity implements UVideoView.Callback {

    private UVideoView mVideoView;
    @BindView(R.id.loading_layout)
    RelativeLayout loadingLayout;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.cover_image)
    ImageView coverView;
    @BindView(R.id.loading_text)
    TextView loadingText;
    @BindView(R.id.vp_panl)
    ViewPager vp_panl;
    RoomPanlFragment fragment;

    @Override
    public boolean openSliding() {
        return false;
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_player_live);
        LiveRoom liveRoom = (LiveRoom) getIntent().getSerializableExtra(StatusConfig.LiveRoom);

        int coverRes = liveRoom.getCover();
        coverView.setImageResource(coverRes);

        RoomPanlAdapter adapter = new RoomPanlAdapter(getSupportFragmentManager());
        adapter.addFragment(new TransparentFragment());
        fragment = new RoomPanlFragment();
        Bundle mBundle = new Bundle();
        mBundle.putSerializable(StatusConfig.LiveRoom, liveRoom);
        mBundle.putInt(StatusConfig.ROOM_STYLE, StatusConfig.ROOM_STYLE_PLAYER);
        fragment.setArguments(mBundle);
        adapter.addFragment(fragment);
        vp_panl.setAdapter(adapter);
        vp_panl.setCurrentItem(adapter.getCount() - 1);

        mVideoView = (UVideoView) findViewById(R.id.videoview);
        mVideoView.setPlayType(UVideoView.PlayType.LIVE);
        mVideoView.setPlayMode(UVideoView.PlayMode.NORMAL);
        mVideoView.setRatio(UVideoView.VIDEO_RATIO_FILL_PARENT);
        mVideoView.setDecoder(UVideoView.DECODER_VOD_SW);

        mVideoView.registerCallback(this);
        //真实情况下使用注释的方式
//        mVideoView.setVideoPath(SystemConfig.ucloud_player_url + liveId);
        //临时固定地址使用
        mVideoView.setVideoPath("http://23340.live-vod.cdn.aodianyun.com/m3u8/0x0/merge/02235e76590a08db99d2296f8632c5cf.m3u8");


    }


    @Override
    protected void onResume() {
        super.onResume();
        if (fragment != null) {
            fragment.onResume();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (fragment != null) {
            fragment.onStop();
        }
    }


    @Override
    protected void onDestroy() {
        if (fragment != null) {
            fragment.destroy();
        }
        super.onDestroy();
        if (mVideoView != null) {
            mVideoView.setVolume(0, 0);
            mVideoView.stopPlayback();
            mVideoView.release(true);
        }
    }

    @Override
    public void onEvent(int what, String message) {
        L.d(TAG, "what:" + what + ", message:" + message);
        LogUtils.logTagName(TAG).log("what:" + what + ", message:" + message);
        switch (what) {
            case UVideoView.Callback.EVENT_PLAY_START:
                loadingLayout.setVisibility(View.INVISIBLE);
                fragment.joinChatRoom();
                fragment.addPeriscope();
                break;
            case UVideoView.Callback.EVENT_PLAY_PAUSE:
                break;
            case UVideoView.Callback.EVENT_PLAY_STOP:
                break;
            case UVideoView.Callback.EVENT_PLAY_COMPLETION:
                Toast.makeText(this, "直播已结束", Toast.LENGTH_LONG).show();
                finish();
                break;
            case UVideoView.Callback.EVENT_PLAY_DESTORY:
                Toast.makeText(this, "DESTORY", Toast.LENGTH_SHORT).show();
                break;
            case UVideoView.Callback.EVENT_PLAY_ERROR:
                loadingText.setText("主播尚未开播");
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(this, "主播尚未开播", Toast.LENGTH_LONG).show();
                break;
            case UVideoView.Callback.EVENT_PLAY_RESUME:
                break;
            case UVideoView.Callback.EVENT_PLAY_INFO_BUFFERING_START:
//                Toast.makeText(VideoActivity.this, "unstable network", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @OnClick({R.id.btn_close})
    public void onClick(View v) {
        if (v.getId() == R.id.btn_close) {
            showAlertDialog();
        }
    }

    /**
     * 弹出提示框
     */
    private void showAlertDialog() {
        new AlertView("是否关闭直播间？", null, null, null, new String[]{"是", "否"}, mContext, AlertView.Style.Alert, new AlertView.OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                if (position == 0) {
                    onBackPressed();
                }
            }
        }).setCancelable(true)
                .show();
    }


}
