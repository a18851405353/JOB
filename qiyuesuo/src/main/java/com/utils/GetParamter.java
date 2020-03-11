package com.utils;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by 74117 on 9/3/2020.
 */

/**
 * 将request payload中参数用hashmap存起来
 */
public class GetParamter {
    private static Logger logger = Logger.getLogger(GetParamter.class);
    public static String getRequestPayload(HttpServletRequest req) {
        logger.debug("获取request payload中数据");
        StringBuilder sb = new StringBuilder();
        try(BufferedReader reader = req.getReader();) {
            char[]buff = new char[1024];
            int len;
            while((len = reader.read(buff)) != -1) {
                sb.append(buff,0, len);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
