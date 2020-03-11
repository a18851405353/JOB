package com.dao;

import com.filter.FilterClass;
import org.apache.log4j.Logger;
import org.python.antlr.ast.Str;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 74117 on 9/3/2020.
 */

/**
 * Dao层，用于数据访问
 */
public class FileDao {
    private static Logger logger = Logger.getLogger(FileDao.class);
    /**
     * 返回数据库连接
     * @return
     */
    public Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            connection = DriverManager.getConnection("jdbc:derby:C:\\Users\\74117\\dedb;user=db_user1;password=111111;create=true");
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.debug("拿到derby数据库连接connection了" );
        return connection;
    }


    /**
     *上传文件时将信息写入数据库
     * 参数在上传服务中收集数据，以HashMap形式传递
     * 其中publicKey是RSA公钥，privateKeyshiRSA私钥，ENkeyStr 是数字信封，是AES密钥经RSA私钥加密后的结果
     * @param infoMap
     */
    public void upLoad(Map<String,String> infoMap){
        Connection connection = getConnection();
        logger.debug("开始往derby中记录数据" );
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("INSERT  INTO fileInfo(uuid,size,type,name,address,publicKey,privateKey,EnkeyStr,time) VALUES (?,?,?,?,?,?,?,?,?)");
            statement.setString(1,infoMap.get("uuid"));
            statement.setString(2,infoMap.get("size"));
            statement.setString(3,infoMap.get("type"));
            statement.setString(4,infoMap.get("name"));
            statement.setString(5,infoMap.get("address"));
            statement.setString(6,infoMap.get("publicKey"));
            statement.setString(7,infoMap.get("privateKey"));
            statement.setString(8,infoMap.get("EnkeyStr"));
            statement.setString(9,infoMap.get("time"));
            statement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 数据查询接口，同于查询元数据，并搭配文件下载功能时的文件名，格式传递
     * @param uuid
     * @return
     */

    public HashMap<String, String> query(String uuid) {
        logger.debug("根据uuid查询数据库元数据" );
        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet= null;
        ResultSetMetaData metaData = null;
        HashMap<String,String> result = null;
        try {
            statement = connection.prepareStatement("SELECT * FROM fileInfo WHERE uuid = ? ");
            statement.setString(1,uuid);
            resultSet = statement.executeQuery();
            metaData = resultSet.getMetaData();
            result = new HashMap<>();
            //resultSet必须用迭代器，不然不能读取数据
            while (resultSet.next()){
                for (int i = 1;i <= metaData.getColumnCount();i++)
                {
                    String name = metaData.getColumnName(i);
                    String value = (String) resultSet.getObject(i);
                    result.put(name,value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}
