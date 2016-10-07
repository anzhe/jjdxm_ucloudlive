package com.dou361.live.ui.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dou361.live.R;
import com.dou361.live.bean.LiveRoom;
import com.dou361.live.ui.listener.OnItemClickRecyclerListener;

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
 * 描 述：关注的item
 * <p/>
 * <p/>
 * 修订历史：
 * <p/>
 * ========================================
 */
public class FollowItemHolder extends BaseViewHolder<LiveRoom> {

    @BindView(R.id.photo)
    ImageView imageView;
    @BindView(R.id.author)
    TextView anchor;
    @BindView(R.id.audience_num)
    TextView audienceNum;

    public FollowItemHolder(Context mContext, OnItemClickRecyclerListener listener, View itemView) {
        super(mContext, listener, itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void refreshView() {
        LiveRoom liveRoom = getData();
        anchor.setText(liveRoom.getName());
        audienceNum.setText(liveRoom.getAudienceNum() + "人");
        Glide.with(mContext)
                .load(liveRoom.getCover())
                .placeholder(R.color.placeholder)
                .into(imageView);
    }
}
