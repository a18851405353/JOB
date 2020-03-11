package com.test;

import java.io.File;

/**
 * Created by 74117 on 6/3/2020.
 */
//创目录测试
public class FileTest {
    public static void main(String[] args) {
        String s = "D:\\fdsa\\fadsg\\hgsh\\gadgd\\gad";
        File file = new File(s);
        createFile(file);
    }
    public  static boolean createFile(File file){
        while (!file.getParentFile().exists()){
            if (createFile(file.getParentFile())){
                break;
            }
        }
        file.mkdir();
        return true;
    }
}
