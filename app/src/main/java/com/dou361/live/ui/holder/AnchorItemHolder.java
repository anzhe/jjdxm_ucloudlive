package com.dou361.live.ui.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dou361.live.R;
import com.dou361.live.bean.AnchorBean;
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
 * 描 述：房间成员的item
 * <p/>
 * <p/>
 * 修订历史：
 * <p/>
 * ========================================
 */
public class AnchorItemHolder extends BaseViewHolder<AnchorBean> {

    @BindView(R.id.avatar)
    ImageView avatar;
    private AnchorBean anchorBean;

    public AnchorItemHolder(Context mContext, OnItemClickRecyclerListener listener, View itemView) {
        super(mContext, listener, itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void refreshView() {
        anchorBean = getData();
        //暂时使用测试数据
        Glide.with(mContext)
                .load(anchorBean.getCover())
                .placeholder(R.drawable.ease_default_avatar)
                .into(avatar);
    }
}
