package com.dou361.live.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dou361.live.R;
import com.dou361.live.module.TestRoomLiveRepository;
import com.dou361.live.ui.activity.PlayerLiveActivity;
import com.dou361.live.ui.adapter.FollowAdapter;
import com.dou361.live.ui.listener.OnItemClickRecyclerListener;
import com.dou361.live.ui.widget.GridMarginDecoration;

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
 * 创建日期：2016/10/4 13:22
 * <p>
 * 描 述：关注
 * <p>
 * <p>
 * 修订历史：
 * <p>
 * ========================================
 */
public class FollowLiveFragment extends BaseFragment implements OnItemClickRecyclerListener {


    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_live_follow, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recycleview);
//        GridLayoutManager glm = (GridLayoutManager) recyclerView.getLayoutManager();
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new GridMarginDecoration(6));
        FollowAdapter adapter = new FollowAdapter(getActivity(), TestRoomLiveRepository.getLiveRoomList());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(View view, int postion) {
        startActivity(PlayerLiveActivity.class, TestRoomLiveRepository.getLiveRoomList().get(postion));
    }

}
