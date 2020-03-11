package com.service;

import com.dao.FileDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.utils.GetParamter;
import com.utils.HashKit;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 74117 on 10/3/2020.
 */

/**
 * 获取元数据接口
 */
public class Message {
    private static Logger logger = Logger.getLogger(Message.class);
    public void getMessage(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        HashMap<String, String> result = null;
        try {
            result = juge(httpServletRequest,httpServletResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.debug("开始查询元数据服务");
        JSONObject jsonObject = new JSONObject();
        if (result.get("succ").equals("false"))
        {
            logger.debug("juge方法失败");
            jsonObject.put("message","uuid或私钥错误，请仔细检查");
            return;
        }
        int size = Integer.parseInt(result.get("SIZE"));
        jsonObject.put("fileName",result.get("NAME"));
        jsonObject.put("size",(size/1024)+"kb");
        jsonObject.put("time",result.get("TIME"));
        jsonObject.put("type",result.get("TYPE"));
        jsonObject.put("address",result.get("ADDRESS"));
        httpServletResponse.setContentType("application/json;charset=utf-8");
        PrintWriter writer = null;
        try {
            writer = httpServletResponse.getWriter();
        } catch (IOException e) {
            logger.error("查询服务写入元数据失败");
            e.printStackTrace();
        }
        writer.write(jsonObject.toString());
        logger.debug("元数据查询成功");
        writer.flush();
        writer.close();
    }

    /**
     * juge方法，为提高效率和降低损耗所作的努力，兼备判断和获取数据的功能，判断主要通过result里面的succ键值对
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     * @throws Exception
     */
    public HashMap<String, String> juge(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse) throws Exception {
        logger.debug("开始juge方法");
        //从request payload中获取数据的方法，网上找到的，非常之牛
        String paramterStr = GetParamter.getRequestPayload(httpServletRequest);
        Map<String, Object> response = new ObjectMapper().readValue(paramterStr, HashMap.class);

        String uuid = (String) response.get("uuid");
        FileDao fileDao = new FileDao();
        //查询文件数据
        HashMap<String, String> result = fileDao.query(uuid);
        logger.debug("开始判断uuid是否存在");
        if (result.isEmpty())
        {
            logger.error("uuid不存在");
            result.put("succ","false");
            return result;
        }
        //获取数字签名以及原文
        logger.debug("开始获取明文和数字签名");
        String SID = httpServletRequest.getHeader("X-SID");
        String Signature = httpServletRequest.getHeader("X-Signature");
        //数据从数据库中出来之后，所有key指全部大写，原因不明，可能是derby特性
        String publicKey = result.get("PUBLICKEY");
        System.out.println(HashKit.rsaCheckContent(SID,Signature,publicKey,"UTF-8"));
        logger.debug("开始验证数字签名");
        //验证签名算法，也是网上找的，膜拜大佬
        //System.out.println(publicKey);
        if (HashKit.rsaCheckContent(SID,Signature,publicKey,"UTF-8"))
        {
            result.put("succ","true");
            return result;
        }
        result.put("succ","false");
        return result;

    }

}
