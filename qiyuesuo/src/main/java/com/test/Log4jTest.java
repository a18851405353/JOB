package com.test;

import org.apache.log4j.Logger;

/**
 * Created by 74117 on 10/3/2020.
 */
public class Log4jTest {
    private static Logger logger = Logger.getLogger(Log4jTest.class);
    public static void main(String[] args) {
        logger.debug("我是log4j，我开始工作");
        logger.error("我发生错误，请马上解决");
    }
}
