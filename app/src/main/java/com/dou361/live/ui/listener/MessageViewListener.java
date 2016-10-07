package com.dou361.live.ui.listener;

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
 * 创建日期：2016/10/5 20:36
 * <p>
 * 描 述：房间中监听事件
 * <p>
 * <p>
 * 修订历史：
 * <p>
 * ========================================
 */
public interface MessageViewListener {

    /**
     * 消息发送监听
     */
    void onMessageSend(String content);

    /**
     * 隐藏输入面板
     */
    void onHiderBottomBar();

    /**
     * 消息点击监听
     * @param id  点击回传内容随便定义满足int类型
     * @param sid 点击回传内容随便定义满足String类型
     */
    void onItemClickListener(int id, String sid);

    /**
     * 消息加载更多
     */
    void onLoadMore();
}