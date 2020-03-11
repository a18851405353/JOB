package com.service;

import com.dao.FileDao;
import com.utils.CommonUtil;
import com.utils.Config;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * Created by 74117 on 6/3/2020.
 */

/**
 * 超级重头戏，文件上传，花了我好多功夫
 */
public class UpLoad {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(UpLoad.class);
    private UpLoad upLoad ;
    // 限制文件的上传大小
    private int maxPostSize = 100 * 1024 * 1024; // 最大100M
    public void toFile(HttpServletRequest request,HttpServletResponse httpServletResponse) throws NoSuchAlgorithmException, IOException {
        logger.debug("开始文件上传 服务");
        Map<String, String> fieldMap = null;
        //用fileitem 处理上传数据
        DiskFileItemFactory factory = new DiskFileItemFactory();
        //每次读取文件大小
        factory.setSizeThreshold(4096);
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding("utf-8");
        //限制文件大小
        upload.setSizeMax(maxPostSize);
        String uuid = UUID.randomUUID().toString();
        String directory = getDirectory();
        //中转文件，因为使用配置文件，随时可能更改，所以每次使用前验证该文件夹是否存在，而且也加密需要一个初始文件
        String tranPath = Config.getInstance().getProperty("transition");
        File transition = new File(tranPath);
        createDirectory(transition);
        File file = new File(tranPath+"\\"+uuid);


        String s = null;
        try
        {
            //解析http请求中的文件
            List requestItems = upload.parseRequest(request);
            Iterator iter = requestItems.iterator();
            fieldMap = new HashMap<String,String>();
            FileItem fileItem = (FileItem)requestItems.get(0);

            s = fileItem.getName();
            //将文件上传对象写入中转文件
            while(iter.hasNext())
            {
                FileItem item = (FileItem)iter.next();
                if(!item.isFormField())
                {
                    item.write(file);
                }
            }
    } catch (Exception e) {
            e.printStackTrace();
        }
        long i = file.length();
        String keyStr = UUID.randomUUID().toString();
        //实际文件目录
        File lastFile = new File(directory+"\\"+uuid);
        try {
            //文件AES加密
            AES.fileEncrypt(file,lastFile,keyStr);
            file.delete();
        } catch (Exception e) {
            logger.error("AES文件加密失败");
            e.printStackTrace();
        }
        RSA.genKeyPair();
        Map<Integer,String> keyMap =  RSA.keyMap;
        //reponse  方法，设置返回头，将uuid和privateKey传给客户
        response(httpServletResponse,uuid,keyMap.get(1));
        String EnkeyStr = null;
        try {
            //将对称密钥加密成数字信封
            EnkeyStr = RSA.priEncrypt(keyStr,keyMap.get(1));
        } catch (Exception e) {
            logger.error("封装数字信封失败");
            e.printStackTrace();
        }
        //用hashmap收集数据，Dao层集中处理
        fieldMap.put("name",s.substring(0,s.lastIndexOf('.')));
        fieldMap.put("type",s.substring(s.lastIndexOf('.')+1));
        fieldMap.put("uuid",uuid);
        fieldMap.put("size",i+"");
        fieldMap.put("address",lastFile.getAbsolutePath());
        fieldMap.put("publicKey",keyMap.get(0));
        fieldMap.put("privateKey",keyMap.get(1));
        String time = CommonUtil.DateFormat(new Date(),"yyyy-MM-dd HH:mm:ss");
        fieldMap.put("time",time);
        fieldMap.put("EnkeyStr",EnkeyStr);
        logger.debug("数据信息准备完毕，即将写入数据库");
        FileDao fileDao = new FileDao();
        fileDao.upLoad(fieldMap);
        logger.debug("文件上传成功");
    }


        public String getDirectory()
        {
            String s1 = Config.getInstance().getProperty("upload_store_root");
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String s2 = format.format(new Date());
            File file = new File(s1+"\\"+s2);
            createDirectory(file);
            if (!file.exists())
            {
                file.mkdir();
            }
            return s1+"\\"+s2;
        }

        public boolean createDirectory(File file){
            while (!file.getParentFile().exists()){
                if (createDirectory(file.getParentFile())){
                    break;
                }
            }
            file.mkdir();
            return true;
        }
        public void response(HttpServletResponse httpServletResponse,String UUID,String privateKey) throws IOException {
            logger.debug("开始添加文件上传response（私钥和uuid），返回给客户端");
            PrintWriter writer = httpServletResponse.getWriter();
            httpServletResponse.setContentType("application/json;charset=utf-8");
            httpServletResponse.setCharacterEncoding("UTF-8");
            //无敌JSONObjecet，强的一批，非常好用，添加json对象，前端直接读取
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("UUID",UUID);
            jsonObject.put("privateKey",privateKey);
            writer.write(jsonObject.toString());
            writer.flush();
            writer.close();
        }

    }
