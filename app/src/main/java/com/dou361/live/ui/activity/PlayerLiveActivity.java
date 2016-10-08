package com.dou361.live.ui.activity;

import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dou361.baseutils.utils.LogUtils;
import com.dou361.customui.ui.AlertView;
import com.dou361.live.R;
import com.dou361.live.bean.LiveRoom;
import com.dou361.live.ui.config.StatusConfig;
import com.dou361.live.utils.Utils;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.widget.EaseImageView;
import com.ucloud.common.logger.L;
import com.ucloud.player.widget.v2.UVideoView;

import java.util.Random;

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
public class PlayerLiveActivity extends BaseRoomActivity implements UVideoView.Callback {

    private UVideoView mVideoView;
    @BindView(R.id.loading_layout)
    RelativeLayout loadingLayout;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.loading_text)
    TextView loadingText;
    @BindView(R.id.eiv_anchor)
    EaseImageView eiv_anchor;
    @BindView(R.id.cover_image)
    ImageView coverView;
    @BindView(R.id.tv_username)
    TextView usernameView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_player_live);
        mDrawerLayout.openDrawer(Gravity.RIGHT);
        mDrawerLayout.setScrimColor(Color.TRANSPARENT);
        Utils.setDrawerRightEdgeSize(this, mDrawerLayout, 0.9f);
        LiveRoom liveRoom = (LiveRoom) getIntent().getSerializableExtra(StatusConfig.LiveRoom);
        liveId = liveRoom.getId();
        roomId = liveRoom.getChatroomId();
        int coverRes = liveRoom.getCover();
        coverView.setImageResource(coverRes);

        anchorId = liveRoom.getAnchorId();
        usernameView.setText(anchorId);
        Glide.with(mContext)
                .load(liveRoom.getAvatar())
                .placeholder(R.color.placeholder)
                .into(eiv_anchor);
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
        if (isMessageListInited)
            messageView.refresh();
        EaseUI.getInstance().pushActivity(this);
        // register the event listener when enter the foreground
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        // unregister this event listener when this activity enters the
        // background
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);

        // 把此activity 从foreground activity 列表里移除
        EaseUI.getInstance().popActivity(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatroomManager().leaveChatRoom(roomId);

        if (chatRoomChangeListener != null) {
            EMClient.getInstance().chatroomManager().removeChatRoomChangeListener(chatRoomChangeListener);
        }
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
                EMClient.getInstance().chatroomManager().joinChatRoom(roomId, new EMValueCallBack<EMChatRoom>() {
                    @Override
                    public void onSuccess(EMChatRoom emChatRoom) {
                        chatroom = emChatRoom;
                        addChatRoomChangeListenr();
                        onMessageListInit();
                    }

                    @Override
                    public void onError(int i, String s) {

                    }
                });

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (!isFinishing()) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    periscopeLayout.addHeart();
                                }
                            });
                            try {
                                Thread.sleep(new Random().nextInt(400) + 200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
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

    @OnClick({R.id.img_bt_close})
    public void onClick(View v) {
        if (v.getId() == R.id.img_bt_close) {
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
