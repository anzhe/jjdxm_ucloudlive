package com.dou361.live.ui.holder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.dou361.live.R;
import com.dou361.live.ui.listener.OnItemClickRecyclerListener;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * ========================================
 * <p/>
 * 版 权：dou361.com 版权所有 （C） 2015
 * <p/>
 * 作 者：陈冠明
 * <p/>
 * 个人网站：http://www.dou361.com
 * <p/>
 * 版 本：1.0
 * <p/>
 * 创建日期：2016/10/5
 * <p/>
 * 描 述：直播间消息的item
 * <p/>
 * <p/>
 * 修订历史：
 * <p/>
 * ========================================
 */
public class RoomMessageItemHolder extends BaseViewHolder<EMMessage> {

    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.content)
    TextView content;
    private EMMessage message;

    public RoomMessageItemHolder(Context mContext, OnItemClickRecyclerListener listener, View itemView) {
        super(mContext, listener, itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void refreshView() {
        message = getData();
        name.setText(message.getFrom());
        content.setText(((EMTextMessageBody) message.getBody()).getMessage());
    }
}
