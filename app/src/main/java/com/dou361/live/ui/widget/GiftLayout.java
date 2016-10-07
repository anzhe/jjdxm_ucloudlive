package com.dou361.live.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.dou361.live.R;
import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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
 * 创建日期：2016/7/31 10:14
 * <p/>
 * 描 述：总的礼物布局
 * <p/>
 * <p/>
 * 修订历史：
 * <p/>
 * ========================================
 */
public class GiftLayout extends LinearLayout {

    @BindView(R.id.left_gift_view1)
    LiveLeftGiftView leftGiftView;
    @BindView(R.id.left_gift_view2)
    LiveLeftGiftView leftGiftView2;

    /**
     * 礼物管理
     */
    volatile boolean isGiftShowing = false;
    volatile boolean isGift2Showing = false;
    List<String> toShowList = Collections.synchronizedList(new LinkedList<String>());

    public GiftLayout(Context context) {
        super(context);
        init(context, null);
    }

    public GiftLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GiftLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.widget_gift_layout, this);
        ButterKnife.bind(this);

    }

    /**
     * 显示礼物管理
     */
    public synchronized void showLeftGiftVeiw(Activity activity, String name, String url) {
        if (!isGift2Showing) {
            showGift2Derect(activity, name, url);
        } else if (!isGiftShowing) {
            showGift1Derect(activity, name, url);
        } else {
            toShowList.add(name);
        }
    }

    /**
     * 显示礼物1
     */
    private void showGift1Derect(final Activity activity, final String name, final String url) {
        isGiftShowing = true;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                leftGiftView.setVisibility(View.VISIBLE);
                leftGiftView.setName(name);
                leftGiftView.setAvatar(url);
                leftGiftView.setTranslationY(0);
                ViewAnimator.animate(leftGiftView)
                        .alpha(0, 1)
                        .translationX(-leftGiftView.getWidth(), 0)
                        .duration(600)
                        .thenAnimate(leftGiftView)
                        .alpha(1, 0)
                        .translationY(-1.5f * leftGiftView.getHeight())
                        .duration(800)
                        .onStop(new AnimationListener.Stop() {
                            @Override
                            public void onStop() {
                                String pollName = null;
                                try {
                                    pollName = toShowList.remove(0);
                                } catch (Exception e) {

                                }
                                if (pollName != null) {
                                    showGift1Derect(activity, pollName, url);
                                } else {
                                    isGiftShowing = false;
                                }
                            }
                        })
                        .startDelay(2000)
                        .start();
                ViewAnimator.animate(leftGiftView.getGiftImageView())
                        .translationX(-leftGiftView.getGiftImageView().getX(), 0)
                        .duration(1100)
                        .start();
            }
        });
    }

    /**
     * 显示礼物2
     */
    private void showGift2Derect(final Activity activity, final String name, final String url) {
        isGift2Showing = true;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                leftGiftView2.setVisibility(View.VISIBLE);
                leftGiftView2.setName(name);
                leftGiftView2.setAvatar(url);
                leftGiftView2.setTranslationY(0);
                ViewAnimator.animate(leftGiftView2)
                        .alpha(0, 1)
                        .translationX(-leftGiftView2.getWidth(), 0)
                        .duration(600)
                        .thenAnimate(leftGiftView2)
                        .alpha(1, 0)
                        .translationY(-1.5f * leftGiftView2.getHeight())
                        .duration(800)
                        .onStop(new AnimationListener.Stop() {
                            @Override
                            public void onStop() {
                                String pollName = null;
                                try {
                                    pollName = toShowList.remove(0);
                                } catch (Exception e) {

                                }
                                if (pollName != null) {
                                    showGift2Derect(activity, pollName, url);
                                } else {
                                    isGift2Showing = false;
                                }
                            }
                        })
                        .startDelay(2000)
                        .start();
                ViewAnimator.animate(leftGiftView2.getGiftImageView())
                        .translationX(-leftGiftView2.getGiftImageView().getX(), 0)
                        .duration(1100)
                        .start();
            }
        });
    }

    /**
     * 重置礼物堆集合
     */
    public void release() {
        if (toShowList != null) {
            toShowList.clear();
        }
    }
}
