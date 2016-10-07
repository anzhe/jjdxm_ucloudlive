package com.dou361.live.ui.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

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
 * 创建日期：2016/10/4 15:35
 * <p>
 * 描 述：向导页面的适配器
 * <p>
 * <p>
 * 修订历史：
 * <p>
 * ========================================
 */
public class GuideAdapter extends PagerAdapter {

    private List<View> imageLists;

    public GuideAdapter(List<View> imageLists) {
        this.imageLists = imageLists;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView(imageLists.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ((ViewPager) container).addView(imageLists.get(position));
        return imageLists.get(position);
    }

    @Override
    public int getCount() {
        return imageLists.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }
}
