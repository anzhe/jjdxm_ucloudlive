package com.dou361.live.module;

import android.content.Context;

import com.dou361.live.ui.application.BaseApplication;

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
 * 描 述：临时使用模拟联网后数据，主播的头像
 * <p>
 * <p>
 * 修订历史：
 * <p>
 * ========================================
 */
public class TestAnchorRepository {
    static List<Integer> avatarlist = new ArrayList<>();
    List<Integer> indexList = new ArrayList<>();
    static int SIZE = 9;
    static {
        Context context = BaseApplication.getInstance().getApplicationContext();
        for(int i = 1; i <= SIZE; i++){
            String name = "avatar_girl0"+i;
            int resId = context.getResources().getIdentifier(name,"mipmap",context.getPackageName());
            avatarlist.add(resId);
        }
    }

    public TestAnchorRepository(){
        fillIndexList();
    }

    private void fillIndexList(){
        for(int i = 0; i < SIZE; i++){
            indexList.add(i);
        }
    }

    public int getAvatar(){
        if(indexList.size() != 0) {
            int index = new Random().nextInt(indexList.size());
            int gotIndex = indexList.remove(index);
            return avatarlist.get(gotIndex);
        }else{
            fillIndexList();
            return getAvatar();
        }

    }
}
