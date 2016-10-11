package com.dou361.live.ui.activity;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dou361.baseutils.utils.UIUtils;
import com.dou361.customui.ui.AlertView;
import com.dou361.live.R;
import com.dou361.live.bean.LiveRoom;
import com.dou361.live.module.TestRoomLiveRepository;
import com.dou361.live.ui.adapter.RoomPanlAdapter;
import com.dou361.live.ui.config.LiveConfig;
import com.dou361.live.ui.config.StatusConfig;
import com.dou361.live.ui.config.SystemConfig;
import com.dou361.live.ui.fragment.RoomPanlFragment;
import com.dou361.live.ui.fragment.TransparentFragment;
import com.dou361.live.ui.listener.OnLiveListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.widget.EaseAlertDialog;
import com.hyphenate.easeui.widget.EaseImageView;
import com.ucloud.common.util.DeviceUtils;
import com.ucloud.live.UEasyStreaming;
import com.ucloud.live.UStreamingProfile;
import com.ucloud.live.widget.UAspectFrameLayout;

import java.util.List;

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
 * 描 述：开始直播
 * <p>
 * <p>
 * 修订历史：
 * <p>
 * ========================================
 */
public class StartLiveActivity extends BaseActivity
        implements UEasyStreaming.UStreamingStateListener {

    @BindView(R.id.container)
    UAspectFrameLayout mPreviewContainer;
    @BindView(R.id.start_container)
    RelativeLayout startContainer;
    @BindView(R.id.countdown_txtv)
    TextView countdownView;
    @BindView(R.id.tv_stop_username)
    TextView tv_stop_username;
    @BindView(R.id.eiv_stop_avatar)
    EaseImageView eiv_stop_avatar;
    @BindView(R.id.finish_frame)
    View liveEndLayout;
    @BindView(R.id.cover_image)
    ImageView coverImage;
    @BindView(R.id.vp_panl)
    ViewPager vp_panl;

    protected UEasyStreaming mEasyStreaming;
    public static final int MSG_UPDATE_COUNTDOWN = 1;

    public static final int COUNTDOWN_DELAY = 1000;

    public static final int COUNTDOWN_START_INDEX = 3;
    public static final int COUNTDOWN_END_INDEX = 1;
    protected boolean isShutDownCountdown = false;
    private LiveConfig mSettings;
    private UStreamingProfile mStreamingProfile;
    UEasyStreaming.UEncodingType encodingType;

    boolean isStarted;
    RoomPanlFragment fragment;

    private String liveId;
    private String roomId;
    private String anchorId;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPDATE_COUNTDOWN:
                    handleUpdateCountdown(msg.arg1);
                    break;
            }
        }
    };

    @Override
    public boolean openSliding() {
        return false;
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_start_live);

        RoomPanlAdapter adapter = new RoomPanlAdapter(getSupportFragmentManager());
        adapter.addFragment(new TransparentFragment());
        fragment = new RoomPanlFragment();
        Bundle mBundle = new Bundle();
        LiveRoom liveRoom = new LiveRoom();
        liveId = TestRoomLiveRepository.getLiveRoomId(EMClient.getInstance().getCurrentUser());
        roomId = TestRoomLiveRepository.getChatRoomId(EMClient.getInstance().getCurrentUser());
        anchorId = EMClient.getInstance().getCurrentUser();
        liveRoom.setId(liveId);
        liveRoom.setChatroomId(roomId);
        liveRoom.setAnchorId(anchorId);
        liveRoom.setAvatar(R.mipmap.live_avatar_girl09);
        mBundle.putSerializable(StatusConfig.LiveRoom, liveRoom);
        mBundle.putInt(StatusConfig.ROOM_STYLE, StatusConfig.ROOM_STYLE_LIVE);
        fragment.setArguments(mBundle);
        adapter.addFragment(fragment);
        vp_panl.setAdapter(adapter);
        vp_panl.setCurrentItem(adapter.getCount() - 1);
        fragment.setOnLiveListener(new OnLiveListener() {
            @Override
            public void onCamreClick(View view) {
                /**
                 * 切换摄像头
                 */
                mEasyStreaming.switchCamera();
            }

            @Override
            public void onLightClick(View view) {
                /**
                 * 打开或关闭闪关灯
                 */
                boolean succeed = mEasyStreaming.toggleFlashMode();
                if (succeed) {
                    view.setSelected(!view.isSelected());
                }
            }

            @Override
            public void onVoiceClick(View view) {
                /**
                 * 打开或关闭声音
                 */
                AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                if (audioManager.isMicrophoneMute()) {
                    audioManager.setMicrophoneMute(false);
                    view.setSelected(false);
                } else {
                    audioManager.setMicrophoneMute(true);
                    view.setSelected(true);
                }
            }
        });
        initEnv();
    }

    public void initEnv() {
        mSettings = new LiveConfig(this);

        //        UStreamingProfile.Stream stream = new UStreamingProfile.Stream(rtmpPushStreamDomain, "ucloud/" + mSettings.getPusblishStreamId());
        //hardcode
        UStreamingProfile.Stream stream =
                new UStreamingProfile.Stream(SystemConfig.ucloud_push_url, "ucloud/" + liveId);

        mStreamingProfile =
                new UStreamingProfile.Builder().setVideoCaptureWidth(mSettings.getVideoCaptureWidth())
                        .setVideoCaptureHeight(mSettings.getVideoCaptureHeight())
                        .setVideoEncodingBitrate(
                                mSettings.getVideoEncodingBitRate()) //UStreamingProfile.VIDEO_BITRATE_NORMAL
                        .setVideoEncodingFrameRate(mSettings.getVideoFrameRate())
                        .setStream(stream)
                        .build();

        encodingType = UEasyStreaming.UEncodingType.MEDIA_X264;
        if (DeviceUtils.hasJellyBeanMr2()) {
            encodingType = UEasyStreaming.UEncodingType.MEDIA_CODEC;
        }
        mEasyStreaming = new UEasyStreaming(this, encodingType);
        mEasyStreaming.setStreamingStateListener(this);
        mEasyStreaming.setAspectWithStreamingProfile(mPreviewContainer, mStreamingProfile);
    }

    @Override
    public void onStateChanged(int type, Object event) {
        switch (type) {
            case UEasyStreaming.State.MEDIA_INFO_SIGNATRUE_FAILED:
                Toast.makeText(this, event.toString(), Toast.LENGTH_LONG).show();
                break;
            case UEasyStreaming.State.START_RECORDING:
                if (fragment != null) {
                    fragment.addPeriscope();
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        mEasyStreaming.stopRecording();
        super.onBackPressed();
    }

    /**
     * 弹出提示框
     */
    private void showAlertDialog() {
        new AlertView("是否关闭直播间？", null, null, null, new String[]{"是", "否"}, mContext, AlertView.Style.Alert, new AlertView.OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                if (position == 0) {
                    /**
                     * 关闭直播显示直播成果
                     */
                    mEasyStreaming.stopRecording();
                    if (!isStarted) {
                        onBackPressed();
                        return;
                    }
                    showConfirmCloseLayout();
                }
            }
        }).setCancelable(true)
                .show();
    }

    @OnClick({R.id.live_close_confirm, R.id.btn_start, R.id.btn_close})
    public void onLiveClick(View v) {
        switch (v.getId()) {
            case R.id.live_close_confirm:
                onBackPressed();
                break;
            case R.id.btn_start:
                /**
                 * 开始直播
                 */
                if (liveId == null) {
                    new EaseAlertDialog(this, "只有存在的liveId才能开启直播").show();
                    return;
                }
                startContainer.setVisibility(View.INVISIBLE);
                //Utils.hideKeyboard(titleEdit);
                new Thread() {
                    public void run() {
                        int i = COUNTDOWN_START_INDEX;
                        do {
                            Message msg = Message.obtain();
                            msg.what = MSG_UPDATE_COUNTDOWN;
                            msg.arg1 = i;
                            handler.sendMessage(msg);
                            i--;
                            try {
                                Thread.sleep(COUNTDOWN_DELAY);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } while (i >= COUNTDOWN_END_INDEX);
                    }
                }.start();
                break;
            case R.id.btn_close:
                showAlertDialog();
                break;
        }
    }

    private void showConfirmCloseLayout() {
        //显示封面
        coverImage.setVisibility(View.VISIBLE);
        liveEndLayout.setVisibility(View.VISIBLE);
        List<LiveRoom> liveRoomList = TestRoomLiveRepository.getLiveRoomList();
        for (LiveRoom liveRoom : liveRoomList) {
            if (liveRoom.getId().equals(liveId)) {
                coverImage.setImageResource(liveRoom.getCover());
                Glide.with(mContext)
                        .load(liveRoom.getCover())
                        .placeholder(R.color.placeholder)
                        .into(eiv_stop_avatar);
            }
        }
        tv_stop_username.setText(EMClient.getInstance().getCurrentUser());
    }

    public void handleUpdateCountdown(final int count) {
        if (countdownView != null) {
            countdownView.setVisibility(View.VISIBLE);
            countdownView.setText(String.format("%d", count));
            ScaleAnimation scaleAnimation =
                    new ScaleAnimation(1.0f, 0f, 1.0f, 0f, Animation.RELATIVE_TO_SELF, 0.5f,
                            Animation.RELATIVE_TO_SELF, 0.5f);
            scaleAnimation.setDuration(COUNTDOWN_DELAY);
            scaleAnimation.setFillAfter(false);
            scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    countdownView.setVisibility(View.GONE);
                    fragment.joinChatRoom();
                    if (count == COUNTDOWN_END_INDEX && mEasyStreaming != null && !isShutDownCountdown) {
                        UIUtils.showToastCenterShort("直播开始！");
//                        这里没有打开只是简单测试功能没有去推流的
//                        mEasyStreaming.startRecording();
                        isStarted = true;
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            if (!isShutDownCountdown) {
                countdownView.startAnimation(scaleAnimation);
            } else {
                countdownView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mEasyStreaming.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mEasyStreaming.onResume();
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
        mEasyStreaming.onDestroy();
    }
}
