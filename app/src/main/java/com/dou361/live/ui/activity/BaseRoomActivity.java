package com.dou361.live.ui.activity;

import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dou361.baseutils.utils.LogUtils;
import com.dou361.baseutils.utils.UIUtils;
import com.dou361.live.R;
import com.dou361.live.bean.AnchorBean;
import com.dou361.live.module.TestAnchorRepository;
import com.dou361.live.ui.adapter.AnchorAdapter;
import com.dou361.live.ui.config.StatusConfig;
import com.dou361.live.ui.dialog.RoomUserDetailsDialog;
import com.dou361.live.ui.dialog.ScreenshotDialog;
import com.dou361.live.ui.fragment.ConversationListFragment;
import com.dou361.live.ui.listener.MessageViewListener;
import com.dou361.live.ui.listener.OnATUserListener;
import com.dou361.live.ui.listener.OnItemClickRecyclerListener;
import com.dou361.live.ui.widget.BarrageLayout;
import com.dou361.live.ui.widget.GiftLayout;
import com.dou361.live.ui.widget.PeriscopeLayout;
import com.dou361.live.ui.widget.RoomMessagesView;
import com.dou361.live.utils.Utils;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMChatRoomChangeListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
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
 * 描 述：房间基类
 * <p>
 * <p>
 * 修订历史：
 * <p>
 * ========================================
 */
public abstract class BaseRoomActivity extends BaseActivity {

    /**
     * 抽屉布局的根布局
     */
    @BindView(R.id.root_layout)
    View root_layout;
    /**
     * 成员总数
     */
    @BindView(R.id.audience_num)
    TextView audienceNumView;
    /**
     * 底部bar中的成员集合布局
     */
    @BindView(R.id.horizontal_recycle_view)
    RecyclerView horizontalRecyclerView;
    /**
     * 弹幕
     */
    @BindView(R.id.barrage_layout)
    BarrageLayout barrageLayout;
    /**
     * 礼物
     */
    @BindView(R.id.gift_layout)
    GiftLayout giftLayout;
    /**
     * 聊天室布局
     */
    @BindView(R.id.message_view)
    RoomMessagesView messageView;
    /**
     * 视频点赞布局
     */
    @BindView(R.id.periscope_layout)
    PeriscopeLayout periscopeLayout;
    /**
     * 底部bar
     */
    @BindView(R.id.bottom_bar)
    View bottomBar;
    /**
     * 私信消息提示
     */
    @BindView(R.id.new_messages_warn)
    ImageView newMsgNotifyImage;

    /**
     * 主播id
     */
    protected String anchorId;
    /**
     * 环信聊天室id
     */
    protected String roomId = "";
    /**
     * ucloud直播id
     */
    protected String liveId = "";
    protected boolean isMessageListInited;
    protected EMChatRoomChangeListener chatRoomChangeListener;
    protected EMChatRoom chatroom;
    List<AnchorBean> memberList = new ArrayList<AnchorBean>();

    protected Handler handler = new Handler();
    private TestAnchorRepository avatarRepository = new TestAnchorRepository();

    @Override
    public boolean openStatus() {
        return false;
    }

    @Override
    public boolean openSliding() {
        return false;
    }

    /**
     * 聊天室监听事件
     */
    protected void addChatRoomChangeListenr() {
        chatRoomChangeListener = new EMChatRoomChangeListener() {
            /**
             * 聊天室被解散。
             *
             * @param roomId
             *            聊天室id
             * @param roomName
             *            聊天室名称
             */
            @Override
            public void onChatRoomDestroyed(String roomId, String roomName) {
                if (roomId.equals(BaseRoomActivity.this.roomId)) {
                    LogUtils.logTagName(TAG).log(" room : " + roomId + " with room name : " + roomName + " was destroyed");
                }
            }

            /**
             * 聊天室加入新成员事件
             *
             * @param roomId
             *          聊天室id
             * @param participant
             *          新成员username
             */
            @Override
            public void onMemberJoined(String roomId, String participant) {
                EMMessage message = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
                message.setReceipt(BaseRoomActivity.this.roomId);
                message.setFrom(participant);
                EMTextMessageBody textMessageBody = new EMTextMessageBody("来了");
                message.addBody(textMessageBody);
                message.setChatType(EMMessage.ChatType.ChatRoom);
                EMClient.getInstance().chatManager().saveMessage(message);
                messageView.refreshSelectLast();
                onRoomMemberAdded(participant);
                LogUtils.logTagName(TAG).log("-----新成员进入聊天室-----" + participant);
            }

            /**
             * 聊天室成员主动退出事件
             *
             * @param roomId
             *          聊天室id
             * @param roomName
             *          聊天室名字
             * @param participant
             *          退出的成员的username
             */
            @Override
            public void onMemberExited(String roomId, String roomName, String participant) {
                onRoomMemberExited(participant);
                LogUtils.logTagName(TAG).log("-----成员退出聊天室-----" + participant);
            }

            /**
             * 聊天室人员被移除
             *
             * @param roomId
             *          聊天室id
             *@param roomName
             *          聊天室名字
             * @param participant
             *          被移除人员的username
             */
            @Override
            public void onMemberKicked(String roomId, String roomName, String participant) {
                if (roomId.equals(BaseRoomActivity.this.roomId)) {
                    String curUser = EMClient.getInstance().getCurrentUser();
                    if (curUser.equals(participant)) {
                        EMClient.getInstance().chatroomManager().leaveChatRoom(roomId);
                        UIUtils.showToastCenterShort("你已被移除出此房间");
                        onBackPressed();
                    } else {
                        onRoomMemberExited(participant);
                    }
                }
                LogUtils.logTagName(TAG).log("-----成员被移除出聊天室-----" + participant);
            }
        };
        EMClient.getInstance().chatroomManager().addChatRoomChangeListener(chatRoomChangeListener);
    }

    /**
     * 消息接收监听
     */
    EMMessageListener msgListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {

            for (EMMessage message : messages) {
                String username = null;
                // 群组消息
                if (message.getChatType() == EMMessage.ChatType.GroupChat
                        || message.getChatType() == EMMessage.ChatType.ChatRoom) {
                    username = message.getTo();
                } else {
                    // 单聊消息
                    username = message.getFrom();
                }
                // 如果是当前会话的消息，刷新聊天页面
                if (username.equals(roomId)) {
                    if (message.getBooleanAttribute(StatusConfig.EXTRA_IS_BARRAGE_MSG, false)) {
                        barrageLayout.addBarrage(((EMTextMessageBody) message.getBody()).getMessage(),
                                message.getFrom());
                    }
                    messageView.refreshSelectLast();
                } else {
                    if (message.getChatType() == EMMessage.ChatType.Chat && message.getTo().equals(EMClient.getInstance().getCurrentUser())) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                newMsgNotifyImage.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                    //// 如果消息不是和当前聊天ID的消息
                    //EaseUI.getInstance().getNotifier().onNewMsg(message);
                }
            }
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            EMMessage message = messages.get(messages.size() - 1);
            if (StatusConfig.CMD_GIFT.equals(((EMCmdMessageBody) message.getBody()).action())) {
                giftLayout.showLeftGiftVeiw(activity, EMClient.getInstance().getCurrentUser(), EMClient.getInstance().getCurrentUser());
            }
        }

        @Override
        public void onMessageReadAckReceived(List<EMMessage> messages) {
            if (isMessageListInited) {
                //                messageList.refresh();
            }
        }

        @Override
        public void onMessageDeliveryAckReceived(List<EMMessage> message) {
            if (isMessageListInited) {
                //                messageList.refresh();
            }
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            if (isMessageListInited) {
                messageView.refresh();
            }
        }
    };

    /**
     * 初始化消息
     */
    protected void onMessageListInit() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageView.init(roomId);
                messageView.setMessageViewListener(new MessageViewListener() {
                    @Override
                    public void onMessageSend(String content) {
                        EMMessage message = EMMessage.createTxtSendMessage(content, roomId);
                        if (messageView.isDanmuShow()) {
                            message.setAttribute(StatusConfig.EXTRA_IS_BARRAGE_MSG, true);
                            barrageLayout.addBarrage(content, EMClient.getInstance().getCurrentUser());
                        }
                        message.setChatType(EMMessage.ChatType.ChatRoom);
                        EMClient.getInstance().chatManager().sendMessage(message);
                        message.setMessageStatusCallback(new EMCallBack() {
                            @Override
                            public void onSuccess() {
                                //刷新消息列表
                                messageView.refreshSelectLast();
                                LogUtils.logTagName(TAG).log("-----消息发送成功-----");
                            }

                            @Override
                            public void onError(int i, String s) {
                                UIUtils.showToastCenterShort("消息发送失败！");
                                LogUtils.logTagName(TAG).log("-----消息发送失败！-----" + s);
                            }

                            @Override
                            public void onProgress(int i, String s) {

                            }
                        });
                    }

                    @Override
                    public void onHiderBottomBar() {
                        bottomBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onItemClickListener(int id, String sid) {
                        if (sid != null && !sid.equals(EMClient.getInstance().getCurrentUser())) {
                            messageView.setShowInputView(true);
                            bottomBar.setVisibility(View.INVISIBLE);
                            messageView.setReplyer(sid);
                        }
                        LogUtils.logTagName(TAG).log("-----点击聊天消息的条目-----" + sid);
                    }

                    @Override
                    public void onLoadMore() {

                    }
                });
                messageView.setVisibility(View.VISIBLE);
                bottomBar.setVisibility(View.VISIBLE);
                isMessageListInited = true;
                updateUnreadMsgView();
                showMemberList();
            }
        });
    }

    /**
     * 更新未读显示提示
     */
    protected void updateUnreadMsgView() {
        if (isMessageListInited) {
            for (EMConversation conversation : EMClient.getInstance()
                    .chatManager()
                    .getAllConversations()
                    .values()) {
                if (conversation.getType() == EMConversation.EMConversationType.Chat
                        && conversation.getUnreadMsgCount() > 0) {
                    newMsgNotifyImage.setVisibility(View.VISIBLE);
                    return;
                }
            }
            newMsgNotifyImage.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 显示成员详情
     */
    private void showUserDetailsDialog(AnchorBean anchorBean) {
        final RoomUserDetailsDialog dialog =
                RoomUserDetailsDialog.newInstance(anchorBean);
        dialog.setOnATUserListener(
                new OnATUserListener() {
                    @Override
                    public void onATClick(String username) {
                        dialog.dismiss();
                        messageView.getInputView().setText("@" + username + " ");
                        showInputView();
                    }
                });
        dialog.show(getSupportFragmentManager(), "RoomUserDetailsDialog");
        LogUtils.logTagName(TAG).log("-----显示成员详情-----" + anchorBean.getName());
    }

    /**
     * 显示输入框
     */
    private void showInputView() {
        bottomBar.setVisibility(View.INVISIBLE);
        messageView.setShowInputView(true);
        messageView.getInputView().requestFocus();
        messageView.getInputView().requestFocusFromTouch();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Utils.showKeyboard(messageView.getInputView());
            }
        }, 200);
    }

    /**
     * 显示成员列表
     */
    private void showMemberList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(BaseRoomActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        horizontalRecyclerView.setLayoutManager(layoutManager);
        AnchorAdapter anchorAdapter = new AnchorAdapter(mContext, memberList);
        horizontalRecyclerView.setAdapter(anchorAdapter);
        anchorAdapter.setOnItemClickListener(new OnItemClickRecyclerListener() {
            @Override
            public void onItemClick(View view, int position) {
                showUserDetailsDialog(memberList.get(position));
            }
        });

        /**获取聊天室成员列表*/
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    chatroom =
                            EMClient.getInstance().chatroomManager().fetchChatRoomFromServer(roomId, true);
                    List<String> temp = chatroom.getMemberList();
                    initAnchorData();
                    if (temp != null && temp.size() > 0) {
                        for (int i = 0; i < temp.size(); i++) {
                            AnchorBean anchorBean = new AnchorBean();
                            anchorBean.setName(temp.get(i));
                            anchorBean.setCover(avatarRepository.getAvatar());
                            memberList.add(anchorBean);
                        }
                    }
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        audienceNumView.setText(String.valueOf(memberList.size()));
                        horizontalRecyclerView.getAdapter().notifyDataSetChanged();
                    }
                });
            }
        }).start();
        root_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //根布局点赞
                periscopeLayout.addHeart();
                return false;
            }
        });
    }

    /**
     * 随机添加一些数据后期可以删除
     */
    private void initAnchorData() {
        int index = new Random().nextInt(20);
        for (int i = 0; i < index; i++) {
            AnchorBean anchorBean = new AnchorBean();
            anchorBean.setName("机器人" + i);
            anchorBean.setCover(avatarRepository.getAvatar());
            memberList.add(anchorBean);
        }
    }

    /**
     * 新成员进入聊天室
     */
    private void onRoomMemberAdded(String name) {
        if (!memberList.contains(name)) {
            AnchorBean anchorBean = new AnchorBean();
            anchorBean.setName(name);
            anchorBean.setCover(avatarRepository.getAvatar());
            memberList.add(anchorBean);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                audienceNumView.setText(String.valueOf(memberList.size()));
                horizontalRecyclerView.getAdapter().notifyDataSetChanged();
            }
        });
    }

    /**
     * 成员离开聊天室
     */
    private void onRoomMemberExited(String name) {
        removeMember(name);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                audienceNumView.setText(String.valueOf(memberList.size()));
                horizontalRecyclerView.getAdapter().notifyDataSetChanged();
            }
        });
    }

    /**
     * 移除聊天室成员
     */
    private synchronized void removeMember(String name) {
        if (name == null || "".equals(name)) {
            return;
        }
        for (int i = 0; i < memberList.size(); i++) {
            if (name.equals(memberList.get(i).getName())) {
                memberList.remove(i);
                break;
            }
        }
    }

    @OnClick({R.id.screenshot_image, R.id.comment_image, R.id.present_image, R.id.chat_image})
    public void onBaseClick(View v) {
        switch (v.getId()) {
            case R.id.screenshot_image:
                Bitmap bitmap = screenshot();
                if (bitmap != null) {
                    ScreenshotDialog dialog = new ScreenshotDialog(this, bitmap);
                    dialog.show();
                }
                break;
            case R.id.comment_image:
                //聊天
                showInputView();
                break;
            case R.id.present_image:
                //弹幕
                EMMessage message = EMMessage.createSendMessage(EMMessage.Type.CMD);
                message.setReceipt(roomId);
                EMCmdMessageBody cmdMessageBody = new EMCmdMessageBody(StatusConfig.CMD_GIFT);
                message.addBody(cmdMessageBody);
                message.setChatType(EMMessage.ChatType.ChatRoom);
                EMClient.getInstance().chatManager().sendMessage(message);
                giftLayout.showLeftGiftVeiw(activity, EMClient.getInstance().getCurrentUser(), EMClient.getInstance().getCurrentUser());
                break;
            case R.id.chat_image:
                //私信
                AnchorBean anchorBean = new AnchorBean();
                anchorBean.setAnchorId(anchorId);
                ConversationListFragment fragment = ConversationListFragment.newInstance(anchorBean, false);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.message_container, fragment)
                        .commit();
                break;
        }
    }

    /**
     * 截屏
     */
    private Bitmap screenshot() {
        // 获取屏幕
        View dView = getWindow().getDecorView();
        dView.setDrawingCacheEnabled(true);
        dView.buildDrawingCache();
        Bitmap bmp = dView.getDrawingCache();
        return bmp;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
