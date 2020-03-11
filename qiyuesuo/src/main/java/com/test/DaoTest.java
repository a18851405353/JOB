package com.test;

import com.dao.FileDao;
import com.utils.CommonUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

/**
 * Created by 74117 on 9/3/2020.
 */

/**
 * 数据库测试，主要看连接是否正常可用
 */
public class DaoTest {
    public static void main(String[] args) {
        FileDao fileDao = new FileDao();
        Connection connection = fileDao.getConnection();
        Statement statement = null;
        ResultSet resultSet = null;
        try {
             statement = connection.createStatement();
             resultSet = statement.executeQuery("SELECT * from fileInfo");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(resultSet);
        System.out.println(CommonUtil.DateFormat(new Date(),"yyyy-MM-dd HH:mm:ss"));

    }
}
