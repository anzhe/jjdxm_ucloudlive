package com.dou361.live.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dou361.baseutils.utils.LogUtils;
import com.dou361.baseutils.utils.UIUtils;
import com.dou361.customui.ui.AlertView;
import com.dou361.live.R;
import com.dou361.live.bean.LiveRoom;
import com.dou361.live.module.TestRoomLiveRepository;
import com.dou361.live.ui.config.LiveConfig;
import com.dou361.live.ui.config.SystemConfig;
import com.dou361.live.utils.Utils;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.widget.EaseAlertDialog;
import com.ucloud.common.util.DeviceUtils;
import com.ucloud.live.UEasyStreaming;
import com.ucloud.live.UStreamingProfile;
import com.ucloud.live.widget.UAspectFrameLayout;

import java.util.List;
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
 * 描 述：开始直播
 * <p>
 * <p>
 * 修订历史：
 * <p>
 * ========================================
 */
public class StartLiveActivity extends BaseRoomActivity
        implements UEasyStreaming.UStreamingStateListener {

    @BindView(R.id.container)
    UAspectFrameLayout mPreviewContainer;
    @BindView(R.id.start_container)
    RelativeLayout startContainer;
    @BindView(R.id.countdown_txtv)
    TextView countdownView;
    @BindView(R.id.ll_anchor)
    View ll_anchor;
    @BindView(R.id.tv_username)
    TextView usernameView;
    @BindView(R.id.tv_follow)
    TextView tv_follow;
    @BindView(R.id.btn_start)
    Button startBtn;
    @BindView(R.id.finish_frame)
    ViewStub liveEndLayout;
    @BindView(R.id.cover_image)
    ImageView coverImage;
    @BindView(R.id.img_bt_switch_camera)
    ImageButton img_bt_switch_camera;
    @BindView(R.id.img_bt_switch_light)
    ImageButton lightSwitch;
    @BindView(R.id.img_bt_switch_voice)
    ImageButton voiceSwitch;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

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
    protected void initView() {
        setContentView(R.layout.activity_start_live);

        Utils.setDrawerRightEdgeSize(this, mDrawerLayout, 0.9f);
        mDrawerLayout.openDrawer(Gravity.RIGHT);
        mDrawerLayout.setScrimColor(Color.TRANSPARENT);
        ll_anchor.setVisibility(View.INVISIBLE);
        tv_follow.setVisibility(View.GONE);
        img_bt_switch_camera.setVisibility(View.VISIBLE);
        lightSwitch.setVisibility(View.VISIBLE);
        voiceSwitch.setVisibility(View.VISIBLE);
        liveId = TestRoomLiveRepository.getLiveRoomId(EMClient.getInstance().getCurrentUser());
        roomId = TestRoomLiveRepository.getChatRoomId(EMClient.getInstance().getCurrentUser());
        anchorId = EMClient.getInstance().getCurrentUser();
        usernameView.setText(anchorId);
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

    @OnClick({R.id.img_bt_switch_camera, R.id.btn_start, R.id.img_bt_close, R.id.img_bt_switch_voice, R.id.img_bt_switch_light})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_bt_switch_camera:
                /**
                 * 切换摄像头
                 */
                mEasyStreaming.switchCamera();
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
            case R.id.img_bt_close:
                showAlertDialog();
                break;
            case R.id.img_bt_switch_voice:
                AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                if (audioManager.isMicrophoneMute()) {
                    audioManager.setMicrophoneMute(false);
                    voiceSwitch.setSelected(false);
                } else {
                    audioManager.setMicrophoneMute(true);
                    voiceSwitch.setSelected(true);
                }
                break;
            case R.id.img_bt_switch_light:
                /**
                 * 打开或关闭闪关灯
                 */
                boolean succeed = mEasyStreaming.toggleFlashMode();
                if (succeed) {
                    if (lightSwitch.isSelected()) {
                        lightSwitch.setSelected(false);
                    } else {
                        lightSwitch.setSelected(true);
                    }
                }
                break;
        }
    }

    private void showConfirmCloseLayout() {
        //显示封面
        coverImage.setVisibility(View.VISIBLE);
        List<LiveRoom> liveRoomList = TestRoomLiveRepository.getLiveRoomList();
        for (LiveRoom liveRoom : liveRoomList) {
            if (liveRoom.getId().equals(liveId)) {
                coverImage.setImageResource(liveRoom.getCover());
            }
        }
        View view = liveEndLayout.inflate();
        Button closeConfirmBtn = (Button) view.findViewById(R.id.live_close_confirm);
        TextView usernameView = (TextView) view.findViewById(R.id.tv_username);
        usernameView.setText(EMClient.getInstance().getCurrentUser());
        closeConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
                    EMClient.getInstance()
                            .chatroomManager()
                            .joinChatRoom(roomId, new EMValueCallBack<EMChatRoom>() {
                                @Override
                                public void onSuccess(EMChatRoom emChatRoom) {
                                    chatroom = emChatRoom;
                                    addChatRoomChangeListenr();
                                    onMessageListInit();
                                    LogUtils.logTagName(TAG).log("-----加入聊天室成功-----");
                                }

                                @Override
                                public void onError(int i, String s) {
                                    UIUtils.showToastCenterShort("加入聊天室失败");
                                    LogUtils.logTagName(TAG).log("-----加入聊天室失败-----" + s);
                                }
                            });

                    if (count == COUNTDOWN_END_INDEX && mEasyStreaming != null && !isShutDownCountdown) {
                        UIUtils.showToastCenterShort("直播开始！");
                        mEasyStreaming.startRecording();
                        isStarted = true;
                        ll_anchor.setVisibility(View.VISIBLE);
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
        if (isMessageListInited) messageView.refresh();
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
        mEasyStreaming.onDestroy();

        EMClient.getInstance().chatroomManager().leaveChatRoom(roomId);

        if (chatRoomChangeListener != null) {
            EMClient.getInstance().chatroomManager().removeChatRoomChangeListener(chatRoomChangeListener);
        }
    }
}
