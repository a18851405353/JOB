package com.service;

import com.dao.FileDao;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.utils.CommonUtil;
import com.utils.Config;
import com.utils.GetParamter;
import com.utils.HashKit;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.python.antlr.ast.Str;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by 74117 on 7/3/2020.
 */

/**
 * 文件下载服务，
 */

public class DownLoad {
    private static Logger logger = Logger.getLogger(DownLoad.class);
    public void getFile(HttpServletRequest httpServletRequest,
                        HttpServletResponse httpServletResponse) throws Exception {
        //juge方法，详细看下面
        logger.debug("开始获取文件");
        HashMap<String, String> result = juge(httpServletRequest,httpServletResponse);
        if (result.get("succ").equals("false"))
        {
            return;
        }
        String path = result.get("ADDRESS");
        String keyStr = RSA.pubDecrypt(result.get("ENKEYSTR"),result.get("PUBLICKEY"));
        String fileName = result.get("NAME");
        File file = new File(path);
        String tranPath = Config.getInstance().getProperty("transition");
        File transition = new File(tranPath);
        createDirectory(transition);
        File lastFile = new File(tranPath+"\\"+ UUID.randomUUID());
        AES.fileDecrypt(file,lastFile,keyStr);
        String lastPath = lastFile.getAbsolutePath();
        InputStream in = new BufferedInputStream(new FileInputStream(lastPath));
        //byte[] buffer = new byte[in.available()];
        httpServletResponse.reset();
        OutputStream toClient = new BufferedOutputStream(httpServletResponse.getOutputStream());
        httpServletResponse.setContentType("application/octet-stream");
        httpServletResponse.setHeader("Content-Dispoistion","attachment;filename="+ URLEncoder.encode(fileName,"UTF-8"));
        String origin = httpServletRequest.getHeader("Origin");
        //再次跨域，原因不明，猜测是因为httpServletResponse被重新编为流的原因
        if(!CommonUtil.isNullOrEmpty(origin))
        {
            httpServletResponse.setHeader("Access-Control-Allow-Origin", origin);
        }
        byte[] buff =new byte[1024];
        int n;
        while((n=in.read(buff))!=-1){
            //将字节数组的数据全部写入到输出流中
            toClient.write(buff,0,n);
        }
        lastFile.delete();
        logger.debug("文件即将开始下载");
        in.close();
        toClient.flush();
        toClient.close();
    }

    /**
     * juge方法，主要用于判断请求是否合法，顺便获取数据result，避免重复工作
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
        if (HashKit.rsaCheckContent(SID,Signature,publicKey,"UTF-8"))
        {
            logger.error("数字签名验证失败");
            result.put("succ","true");
            return result;
        }
        result.put("succ","false");
        return result;

    }
    //创建目录
    public boolean createDirectory(File file){
        while (!file.getParentFile().exists()){
            if (createDirectory(file.getParentFile())){
                break;
            }
        }
        file.mkdir();
        return true;
    }
}
