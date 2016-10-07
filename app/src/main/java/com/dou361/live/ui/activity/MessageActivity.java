package com.dou361.live.ui.activity;

import com.dou361.live.R;
import com.dou361.live.bean.AnchorBean;
import com.dou361.live.ui.config.StatusConfig;
import com.dou361.live.ui.fragment.ChatFragment;

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
 * 创建日期：2016/10/4 12:55
 * <p>
 * 描 述：消息界面
 * <p>
 * <p>
 * 修订历史：
 * <p>
 * ========================================
 */
public class MessageActivity extends BaseActivity {
    @Override
    protected void initView() {
        setContentView(R.layout.activity_message);
        AnchorBean anchorBean = (AnchorBean) getIntent().getSerializableExtra(StatusConfig.ARG_ANCHOR);
        getSupportFragmentManager().beginTransaction().add(R.id.root, ChatFragment.newInstance(anchorBean, true)).commit();
    }
}
