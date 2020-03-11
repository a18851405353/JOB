package com.utils;

import java.io.InputStream;
import java.util.Properties;

/**
 * config.properties文件配置项读取
 */
public class Config
{
	private static Config propertiesConfig;
	private Properties properties;
	
	private Config()
	{
		properties = new Properties();
		try
		{
			InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
			properties.load(inputStream);
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	public static Config getInstance()
	{
		if(propertiesConfig == null)
		{
			propertiesConfig = new Config();
		}
		return propertiesConfig;
	}
	/**
	 * 读取配置值
	 * @param key
	 * @return
	 */
	public String getProperty(String key)
	{
		return properties.getProperty(key);
	}
}
