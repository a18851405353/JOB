package com.filter;

import com.utils.CommonUtil;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by 74117 on 5/3/2020.
 */

/**
 * 由于此次使用前后端分离开发，所以需要解决跨域问题，此次因为是jetty服务器，不能直接配置filter，得用服务器容器context过渡
 */
public class FilterClass implements Filter {
    private static Logger logger = Logger.getLogger(FilterClass.class);
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.debug("过滤器初始化");
        System.out.println("允许跨域");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        logger.debug("开始配置http权限");
        // 不使用*，自动适配跨域域名，避免携带Cookie时失效
        String origin = httpRequest.getHeader("Origin");
        if(!CommonUtil.isNullOrEmpty(origin))
        {
            httpResponse.setHeader("Access-Control-Allow-Origin", origin);
        }
        // 允许跨域请求包含某请求头
        String headers = httpRequest.getHeader("Access-Control-Request-Headers");
        if(!CommonUtil.isNullOrEmpty(headers))
        {
            httpResponse.setHeader("Access-Control-Allow-Headers", headers);
            httpResponse.setHeader("Access-Control-Expose-Headers", headers);
        }
        //httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type,X-Requested-With");
        // *允许任何域 // 允许的外域请求方式
        httpResponse.setHeader("Access-Control-Allow-Methods", "*");
        // 在999999秒内，不需要再发送预检验请求，可以缓存该结果
        httpResponse.setHeader("Access-Control-Max-Age", "999999");
        // 明确许可客户端发送Cookie，不允许删除字段即可
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        chain.doFilter(request, response);

    }

    @Override
    public void destroy() {

    }
}
