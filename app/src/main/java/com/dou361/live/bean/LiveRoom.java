package com.dou361.live.bean;

import java.io.Serializable;

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
 * 创建日期：2016/10/4 11:32
 * <p>
 * 描 述：直播间
 * <p>
 * <p>
 * 修订历史：
 * <p>
 * ========================================
 */
public class LiveRoom implements Serializable {

    /**
     * 直播id
     */
    private String id;
    /**
     * 名称
     */
    private String name;
    /**
     * 观看人数
     */
    private int audienceNum;
    /**
     * 头像
     */
    private int avatar;
    /**
     * 封面
     */
    private int cover;
    /**
     * 聊天室id
     */
    private String chatroomId;
    /**
     * 主播id
     */
    private String anchorId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAudienceNum() {
        return audienceNum;
    }

    public void setAudienceNum(int audienceNum) {
        this.audienceNum = audienceNum;
    }

    public int getCover() {
        return cover;
    }

    public void setCover(int cover) {
        this.cover = cover;
    }

    public String getChatroomId() {
        return chatroomId;
    }

    public void setChatroomId(String chatroomId) {
        this.chatroomId = chatroomId;
    }

    public String getAnchorId() {
        return anchorId;
    }

    public void setAnchorId(String anchorId) {
        this.anchorId = anchorId;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }
}
