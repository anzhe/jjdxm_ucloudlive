package com.dou361.live.module;

import com.dou361.live.R;
import com.dou361.live.bean.LiveRoom;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
 * 创建日期：2016/10/6 14:39
 * <p>
 * 描 述：临时使用模拟联网后数据，直播间数据
 * <p>
 * <p>
 * 修订历史：
 * <p>
 * ========================================
 */
public class TestRoomLiveRepository {

    static int[] covers = new int[]{
            R.mipmap.live_girl01, R.mipmap.live_girl02, R.mipmap.live_girl03, R.mipmap.live_girl04, R.mipmap.live_girl05,
            R.mipmap.live_girl06, R.mipmap.live_girl07, R.mipmap.live_girl08, R.mipmap.live_girl09
    };

    static String[] chatRoomIds = new String[]{"218746635482562996", "218747106892972464", "218747152489251244", "218747179836113332", "218747226120257964", "218747262707171768", "218747179836113332", "218747226120257964", "218747262707171768"};
    static String[] liveRoomIds = new String[]{"em_100001", "em_100002", "em_100003", "em_100004", "em_100005", "em_100006", "em_100007", "em_100008", "em_100009"};
    public static String[] anchorIds = new String[]{"test1", "test2", "test3", "test4", "test5", "test6", "test7", "test8", "test9"};

    /**
     * 生成测试数据
     */
    public static List<LiveRoom> getLiveRoomList() {
        List<LiveRoom> roomList = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            LiveRoom liveRoom = new LiveRoom();
            liveRoom.setName("Test" + i);
            liveRoom.setAudienceNum(new Random().nextInt(2000) + 1);
            liveRoom.setId(liveRoomIds[i - 1]);
            liveRoom.setChatroomId(chatRoomIds[i - 1]);
            liveRoom.setCover(covers[i - 1]);
            liveRoom.setAnchorId(anchorIds[i - 1]);
            roomList.add(liveRoom);
        }

        return roomList;
    }

    public static String getLiveRoomId(String username) {
        for (int i = 0; i < 9; i++) {
            if (anchorIds[i].equals(username)) {
                return liveRoomIds[i];
            }
        }
        return null;
    }

    public static String getChatRoomId(String username) {
        for (int i = 0; i < 9; i++) {
            if (anchorIds[i].equals(username)) {
                return chatRoomIds[i];
            }
        }
        return null;
    }
}
