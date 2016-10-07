package com.dou361.live.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dou361.live.R;
import com.dou361.live.module.TestRoomLiveRepository;
import com.dou361.live.ui.adapter.SearchAdapter;

import java.util.ArrayList;
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
 * 创建日期：2016/10/5 15:57
 * <p>
 * 描 述：搜索界面
 * <p>
 * <p>
 * 修订历史：
 * <p>
 * ========================================
 */
public class SearchActivity extends BaseActivity {

    @BindView(R.id.edit_text)
    EditText editText;
    @BindView(R.id.recView)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.empty_view)
    TextView emptyView;
    SearchAdapter adapter;
    List<String> searchList;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_search);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchList = new ArrayList<>();

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!TextUtils.isEmpty(v.getText())) {
                        searchUser(v.getText().toString());
                    } else {
                        Toast.makeText(SearchActivity.this, "请输入搜索关键字", Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void searchUser(String searchText) {
        //没有实际服务器数据，这里只搜索主播
        searchList.clear();
        String[] anchorIds = TestRoomLiveRepository.anchorIds;
        for (String anchor : anchorIds) {
            if (anchor.contains(searchText.trim()) || anchor.equals(searchText.trim())) {
                searchList.add(anchor);
            }
        }
        if (searchList.size() == 0) {
            emptyView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setVisibility(View.INVISIBLE);
        }
        if (adapter == null) {
            adapter = new SearchAdapter(this, searchList);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }

    }
}
