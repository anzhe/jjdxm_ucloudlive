package com.dou361.live.ui.activity;

import android.support.v7.widget.Toolbar;
import android.view.View;

import com.dou361.live.R;

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
 * 创建日期：2016/10/4 12:55
 * <p>
 * 描 述：个人中心
 * <p>
 * <p>
 * 修订历史：
 * <p>
 * ========================================
 */
public class PersonDataActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_person_data);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

}
