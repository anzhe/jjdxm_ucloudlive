package com.dou361.live.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dou361.baseutils.utils.UIUtils;
import com.dou361.live.R;
import com.dou361.live.module.TestRoomLiveRepository;
import com.dou361.live.ui.activity.PlayerLiveActivity;
import com.dou361.live.ui.adapter.HotAdapter;
import com.dou361.live.ui.listener.OnItemClickRecyclerListener;
import com.dou361.live.ui.widget.GridMarginDecoration;

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
 * 创建日期：2016/10/4 13:03
 * <p>
 * 描 述：热门
 * <p>
 * <p>
 * 修订历史：
 * <p>
 * ========================================
 */
public class HotLiveFragment extends BaseFragment implements OnItemClickRecyclerListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.sfl_refresh)
    SwipeRefreshLayout sfl_refresh;
    @BindView(R.id.recycleview)
    RecyclerView recyclerView;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_live_hot, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new GridMarginDecoration(6));
        HotAdapter adapter = new HotAdapter(getActivity(), TestRoomLiveRepository.getLiveRoomList());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        sfl_refresh.setOnRefreshListener(this);

    }

    @Override
    public void onItemClick(View view, int postion) {
        startActivity(PlayerLiveActivity.class, TestRoomLiveRepository.getLiveRoomList().get(postion));
    }

    @Override
    public void onRefresh() {
        sfl_refresh.setRefreshing(true);
        UIUtils.postDelayed(new Runnable() {
            @Override
            public void run() {
                sfl_refresh.setRefreshing(false);
            }
        }, 1000);

    }
}
