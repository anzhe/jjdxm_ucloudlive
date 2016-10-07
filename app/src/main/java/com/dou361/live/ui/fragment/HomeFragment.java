package com.dou361.live.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dou361.live.R;
import com.dou361.live.bean.AnchorBean;
import com.dou361.live.ui.activity.MessageActivity;
import com.dou361.live.ui.activity.SearchActivity;
import com.dou361.live.ui.adapter.HomeAdapter;
import com.hyphenate.chat.EMClient;

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
 * 创建日期：2016/10/4 12:57
 * <p>
 * 描 述：首页
 * <p>
 * <p>
 * 修订历史：
 * <p>
 * ========================================
 */
public class HomeFragment extends BaseFragment {

    @BindView(R.id.iv_search)
    ImageView iv_search;
    @BindView(R.id.iv_msg)
    ImageView iv_msg;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupViewPager();
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager() {
        HomeAdapter adapter = new HomeAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new FollowLiveFragment(), "关注");
        adapter.addFragment(new HotLiveFragment(), "热门");
        adapter.addFragment(new NearbyLiveFragment(), "附近");
        viewPager.setAdapter(adapter);
    }

    @OnClick({R.id.iv_search, R.id.iv_msg})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_search:
                startActivity(SearchActivity.class);
                break;
            case R.id.iv_msg:
                AnchorBean anchorBean = new AnchorBean();
                anchorBean.setAnchorId(EMClient.getInstance().getCurrentUser());
                anchorBean.setName(EMClient.getInstance().getCurrentUser());
                startActivity(MessageActivity.class, anchorBean);
                break;
        }
    }

}
