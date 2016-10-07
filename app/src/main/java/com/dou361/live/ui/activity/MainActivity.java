package com.dou361.live.ui.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dou361.live.R;
import com.dou361.live.ui.fragment.ConversationListFragment;
import com.dou361.live.ui.fragment.HomeFragment;
import com.dou361.live.ui.fragment.MineFragment;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.util.List;

import butterknife.BindView;

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
 * 创建日期：2016/10/4 11:41
 * <p>
 * 描 述：主页
 * <p>
 * <p>
 * 修订历史：
 * <p>
 * ========================================
 */
public class MainActivity extends BaseActivity {

    private int index;
    /**
     * 当前fragment的index
     */
    private int currentTabIndex;
    private Fragment[] fragments;

    @BindView(R.id.btn_home)
    ImageView btn_home;
    @BindView(R.id.btn_mine)
    ImageView btn_mine;
    @BindView(R.id.tv_unread_msg_number)
    TextView unreadLabel;
    private ImageView[] mTabs;

    @Override
    public boolean openSliding() {
        return false;
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_main);
        /**
         * 初始化组件
         */
        mTabs = new ImageView[2];
        mTabs[0] = btn_home;
        mTabs[1] = btn_mine;
        // 把第一个tab设为选中状态
        mTabs[0].setSelected(true);
        fragments = new Fragment[]{new HomeFragment(),/* ConversationListFragment.newInstance(null, true),*/ new MineFragment()};
        /**添加显示第一个fragment*/
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragments[0])
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUnreadLabel();
        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
    }

    EMMessageListener messageListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> list) {
            refreshUIWithMessage();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> list) {
        }

        @Override
        public void onMessageReadAckReceived(List<EMMessage> list) {
        }

        @Override
        public void onMessageDeliveryAckReceived(List<EMMessage> list) {
        }

        @Override
        public void onMessageChanged(EMMessage emMessage, Object o) {
            refreshUIWithMessage();
        }
    };

    private void refreshUIWithMessage() {
        runOnUiThread(new Runnable() {
            public void run() {
                // refresh unread count
                updateUnreadLabel();
                if (currentTabIndex == 1) {
                    // refresh conversation list
                    if (fragments[1] != null) {
                        ((ConversationListFragment) fragments[1]).refreshList();
                    }
                }
            }
        });
    }

    /**
     * update unread message count
     */
    public void updateUnreadLabel() {
        int count = getUnreadMsgCountTotal();
        if (count > 0) {
            unreadLabel.setText(String.valueOf(count));
            unreadLabel.setVisibility(View.VISIBLE);
        } else {
            unreadLabel.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * get unread message count
     *
     * @return
     */
    public int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal = 0;
        int chatroomUnreadMsgCount = 0;
        unreadMsgCountTotal = EMClient.getInstance().chatManager().getUnreadMsgsCount();
        for (EMConversation conversation : EMClient.getInstance().chatManager().getAllConversations().values()) {
            if (conversation.getType() == EMConversation.EMConversationType.ChatRoom)
                chatroomUnreadMsgCount = chatroomUnreadMsgCount + conversation.getUnreadMsgCount();
        }
        return unreadMsgCountTotal - chatroomUnreadMsgCount;
    }

    /**
     * button点击事件
     *
     * @param view
     */
    public void onTabClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_home:
                index = 0;
                break;
            case R.id.btn_mine:
                index = 1;
                break;
            case R.id.btn_publish:
                startActivity(new Intent(this, StartLiveActivity.class));
                break;
        }
        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.fragment_container, fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }
        mTabs[currentTabIndex].setSelected(false);
        // 把当前tab设为选中状态
        mTabs[index].setSelected(true);
        currentTabIndex = index;
    }
}
