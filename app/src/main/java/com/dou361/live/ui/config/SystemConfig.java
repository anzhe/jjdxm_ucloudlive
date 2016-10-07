package com.dou361.live.ui.config;

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
 * 描 述：全局变量配置
 * <p>
 * <p>
 * 修订历史：
 * <p>
 * ========================================
 */
public class SystemConfig {


    /**
     * 测试使用
     */
    public static final String IP = "http://www.dou361.com";
    private static final String preName = "/index.php/BroadcastLive";
    /**
     * ucloud播放的地址使用
     */
    public static final String ucloud_player_url = "rtmp://vlive3.rtmp.cdn.ucloud.com.cn/ucloud/";
    public static final String ucloud_push_url = "publish3.cdn.ucloud.com.cn";
    /**
     * 直播相关接口
     */
    public static final String LIVE_HEAD = preName;
    public static final int PAGE_SIZE = 10;

    public static int Req_Success = 0;
}
