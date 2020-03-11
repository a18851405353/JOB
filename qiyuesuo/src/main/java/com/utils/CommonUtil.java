package com.utils;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import javax.swing.ImageIcon;

import org.codehaus.jackson.map.ObjectMapper;

public class CommonUtil
{

	/**
	 * java对象转json字符串
	 * @param javaObj
	 * @return
	 */
	public static String jsonToStr(Object javaObj)
	{
		ObjectMapper mapper = new ObjectMapper();
		try
		{
			return mapper.writeValueAsString(javaObj);
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	/**json字符串转java对象
	 * @param jsonStr
	 * @param classType
	 * @return
	 */
	public static Object strToJson(String jsonStr,Class<?> classType)
	{
		ObjectMapper mapper = new ObjectMapper();
		try
		{
			return mapper.readValue(jsonStr, classType);
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}


	/**
	 * Calendar格式化输出
	 * @param cal
	 * @param format 格式化表达式，例如:yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String calendarFormat(Calendar cal,String format)
	{
		if (cal == null)
			return "";
		SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String res = bartDateFormat.format(cal.getTime());
		return res;
	}
	
	/**
	 * Date格式化输出
	 * @param date
	 * @param format 格式化表达式，例如:yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String DateFormat(Date date,String format)
	{
		if (date == null)
			return "";
		SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String res = bartDateFormat.format(date);
		return res;
	}
	
	/**
	 * 数据库byte[]转图片，GIF存储用到
	 * @param bytes
	 * @return
	 */
	public static ImageIcon getImageIcon(byte[] bytes)
	{
		ImageIcon icon = null;
		icon = new ImageIcon(bytes);
		return icon;
	}
	public static InputStream getFileInputStream(String filePath)
	{
		return ClassLoader.getSystemResourceAsStream(filePath);
	}
	
	/**
	 * 文件类路径转绝对路径
	 * 
	 * @param path
	 * @return
	 */
	public static String getFilePathFormClassPath(String path)
	{
		return ClassLoader.getSystemResource(path).getPath();
	}
	/**
	 * 获得源代码中包含的文件的url表达式：file:\xxxxx
	 * @param path 路径
	 * @return
	 */
	public static String getFileUrlPath(String path)
	{
		return ClassLoader.getSystemResource(path).toString();
	}
	/**
	 * 获得文件后缀
	 * 
	 * @param file
	 * @return
	 */
	public static String getFilePrefix(File file)
	{
		String fileName = file.getName();
		String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
		return prefix;
	}
	/**
	 * 获得文件名称(无后缀)
	 * @param file
	 * @return
	 */
	public static String getFileNameNoPrefix(File file)
	{
		String fileName = file.getName();
		String name = fileName.substring(0,fileName.lastIndexOf("."));
		return name;
	}
	/**
	 * 文件转byte[]
	 */
	public static byte[] fileToBytes(File file)
	{
		FileChannel channel = null;
		FileInputStream fs = null;
		try
		{
			fs = new FileInputStream(file);
			channel = fs.getChannel();
			ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
			while ((channel.read(byteBuffer)) > 0)
			{
			}
			return byteBuffer.array();
		} catch (IOException e)
		{
			throw new RuntimeException(e);
		} finally
		{
			try
			{
				channel.close();
				fs.close();
				file = null;
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * byte[]转文件
	 */
	public static void bytesToFile(File file, byte[] data)
	{
		FileOutputStream fos = null;
		try
		{
			if (!file.exists())
			{
				file.createNewFile(); // 如果文件不存在，则创建
			}
			fos = new FileOutputStream(file);
			fos.write(data, 0, data.length);
			fos.flush();
			
		} catch (Exception e)
		{
			throw new RuntimeException(e);
		} finally
		{
			if (fos != null)
			{
				try
				{
					fos.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	
	public static Color getHtmlColor(String color)
	{
		if (color.startsWith("#"))
		{
			color = color.substring(1);
		}
		try
		{
			return new Color(Integer.parseInt(color, 16));
		} catch (NumberFormatException e)
		{
			throw new RuntimeException(String.format("颜色转化出错:s%", color));
		}
	}


	/**
	 * Object转double
	 * 
	 * @param obj
	 * @return
	 */
	public static double getNumber(Object obj)
	{
		
		double number = Double.NaN;
		
		if (obj instanceof Double)
		{
			number = (Double) obj;
		} else if (obj instanceof Float)
		{
			number = (Float) obj;
		} else if (obj instanceof Integer)
		{
			number = (Integer) obj;
		} else if (obj instanceof String)
		{
			try
			{
				number = Double.parseDouble((String) obj);
			} catch (Exception ex)
			{
			}
		}
		
		return number;
	}
	
	/**
	 * 获得浮点数的小数
	 * 
	 * @param d
	 * @return
	 */
	public static double getDecimal(double d)
	{
		String str = String.valueOf(d).replaceAll("\\d+\\.", "");
		return Double.parseDouble("0." + str);
	}
	
	/**
	 * 非四舍五入方式处理浮点数
	 * 
	 * @param d
	 * @param decimalNum
	 *            小数点位数
	 * @return
	 */
	public static double getDeciamlCastOff(double d, int decimalNum)
	{
		String s = Double.toString(d);
		String s1 = s.substring(0, s.indexOf(".") + decimalNum + 1);
		return Double.parseDouble(s1);
	}
	
	/**
	 * 判断字符串是否为空或空字符串
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNullOrEmpty(String str)
	{
		if (str == null)
			return true;
		if (str.trim().length() == 0)
			return true;
		return false;
	}
	
	/**
	 * 生产32位的UUID
	 * 
	 * @return
	 */
	public static String createUUID()
	{
		return UUID.randomUUID().toString().replace("-", "");
	}
	/**
	 * 生成区间随机数
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static double createRandomDouble(double min, double max)
	{
		return Math.random() * (max - min) + min;
	}
}
