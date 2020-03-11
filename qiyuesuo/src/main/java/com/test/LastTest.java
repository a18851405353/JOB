package com.test;

import com.dao.FileDao;

import java.util.HashMap;

/**
 * Created by 74117 on 9/3/2020.
 */

/**
 * 最终测试，主要看能否从数据库中读出数据，derby列名大写也是从这里知道的
 */
public class LastTest {
    public static void main(String[] args) {
        FileDao fileDao = new FileDao();
        HashMap<String,String> hashMap = fileDao.query("6b8c6e07-99f3-407c-a784-28d1cb96a85f");
        System.out.println(hashMap);

    }
}
