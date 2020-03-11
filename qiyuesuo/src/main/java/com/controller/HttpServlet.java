package com.controller;

import com.service.DownLoad;
import com.service.Message;
import com.service.UpLoad;
import com.test.Log4jTest;
import com.utils.CommonUtil;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;

/**
 * Created by 74117 on 6/3/2020.
 */

/**
 * 控制器，根据URL用于转发请求
 */
@WebServlet(name = "HttpServlet")
public class HttpServlet extends javax.servlet.http.HttpServlet {
    private static Logger logger = Logger.getLogger(HttpServlet.class);
    protected void doPost(HttpServletRequest httpServletRequest,
                          HttpServletResponse httpServletResponse) throws ServletException, IOException {
        String url = httpServletRequest.getRequestURI();
        logger.debug("获得请求，从url分配服务");
        String [] urls = url.split("/");
        String action = urls[urls.length-1];
        File file = null;
        if(action != null)
        {
            //根据action，做我们的处理
            //上传
            if(action.toLowerCase().equals("upload"))
            {
                logger.debug("action为upload，开始文件上传服务");
                UpLoad upLoad = new UpLoad();
                try {
                    upLoad.toFile(httpServletRequest,httpServletResponse);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
            //下载
            else if(action.toLowerCase().equals("download"))
            {
                logger.debug("action为udownload，开始文件下载服务");
                DownLoad downLoad = new DownLoad();
                try {
                    downLoad.getFile(httpServletRequest, httpServletResponse);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //获取文件元数据
            else if(action.toLowerCase().equals("query"))
            {
                logger.debug("action为query，开始获取数据元数据服务");
                Message message = new Message();
                try {
                    message.getMessage(httpServletRequest,httpServletResponse);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //最近的10个文件查询接口
            else if(action.toLowerCase().equals("latest"))
            {

            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
