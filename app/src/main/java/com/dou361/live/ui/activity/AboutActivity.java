package com.dou361.live.ui.activity;


import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.dou361.baseutils.utils.SystemUtils;
import com.dou361.live.R;

import butterknife.BindView;

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
 * 创建日期：2016/10/8 16:25
 * <p/>
 * 描 述：关于我们
 * <p/>
 * <p/>
 * 修订历史：
 * <p/>
 * ========================================
 */
public class AboutActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_version_name)
    TextView tv_version_name;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_about);

        tv_version_name.setText(SystemUtils.getVersionName());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

}
