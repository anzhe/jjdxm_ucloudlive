package com.dou361.live.ui.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dou361.live.R;
import com.dou361.live.ui.listener.OnItemClickRecyclerListener;

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
 * 创建日期：2016/10/5 17:59
 * <p>
 * 描 述：搜索的item
 * <p>
 * <p>
 * 修订历史：
 * <p>
 * ========================================
 */
public class SearchItemHolder extends BaseViewHolder<String> {
    ImageView avatarView;
    TextView usernameView;

    public SearchItemHolder(Context mContext, OnItemClickRecyclerListener listener, View itemView) {
        super(mContext, listener, itemView);
        avatarView = (ImageView) itemView.findViewById(R.id.avatar);
        usernameView = (TextView) itemView.findViewById(R.id.username);
    }

    @Override
    public void refreshView() {
        String username = (String) getData();
        usernameView.setText(username + "");
    }
}