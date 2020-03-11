package com.test;

import com.service.RSA;
import com.utils.HashKit;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 74117 on 8/3/2020.
 */

/**
 * 验证客户端和服务端，RSA加解密是否匹配，既前端私钥加密后端是否可以用公钥解密，或验证数字签名是否正常
 */
public class KeyTest {
    public static void main(String[] args) throws Exception {
//        RSA.genKeyPair();
//        Map<Integer,String> keyMap = RSA.keyMap;
//        System.out.println(keyMap.get(0));
//        System.out.println(keyMap.get(1));

        //System.out.println(RSA.priEncrypt("fasfdasfadsf","MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJDlc7oZ+GjRKujRJLPECU59BjIFa51Puc/OvYoGbiMV3z+tGaUFN3SBsCEFadM9T4vSa/8Syg1tfkho6sWh55O0JAOEw0Ba3l8NLIrTBgcWlEiyB8GgH6TcuGorlJZ8uBKW9z1dgwHAPHziYjJKQXJZUJ1a++0Hd3shHuGyJGtBAgMBAAECgYAuDjC1o5GYx3trGyceWJ5Yq1avyZdGHlRCYEVQvPB/CHG4jK3NDRZuWtZ/+HeFpERHfi/DmNZOZFIyc97DyM2RY6AMjlCLc/BeUNKfBgBZOGK52/H9JTanv91YhghRt/J+y6iVDyQpvGQCgEswuc0jt3eeojGmsj5q8RUR3J70LQJBAMtXzdgbRyAukJjHiWLb0Pe/F0rl7EjfKZi2VwHPm/m6LZ26DWIZGe7beQFvXhi+Uv6YXjWC4NPBSW0fhEkJzVMCQQC2awwxfaO2vOdgC0uJEiVl2o94Q/KTI/VUY4ARJLpoMsQ4c2+rRZ3UO7S4FxURslgcjlBnJ/OQJqbml73WOT6bAkEAjg4RJ+AmmDzvl0wOlpmqKum8g/6MS3xbuuvnbMR02slOBNptbxLKIywAJRiATBjqMiKP7iI4nrZxbWKPL9dBMQJAdMBUwIQyOO30hFMWWDdl+ieFdaq/YY89IHLCb7Z7G9+KGLtBOHRJDKHeC4iycm4b2ML5ENzQFW23mpR56tceiwJBAJyzuACLBwVamoZifIBGQ8GlHjRe2eKj9gPv7qMzCPZPDPHOl0R6xdoa5mXXlj3o/BxN0sKAgVPt5wlIy9nwTF8=\n"));
        //System.out.println(RSA.pubDecrypt("Ye0Bp08/qOKY7HfFtpJEYPud0TPxWYETIl6uv7uzXKUIkxKrNfDGpIOMyMsVb0exi1lZUFginaN01wvWh7kdUbZQ32SDAe9XQN+vhSSE1ZQs0KBJJxdB/z28F0SLHm2sVTvQZD2pXFqj8dodfREBUQkWa8DXUgz0Yb3ybCV/7iY=","MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCQ5XO6Gfho0Sro0SSzxAlOfQYyBWudT7nPzr2KBm4jFd8/rRmlBTd0gbAhBWnTPU+L0mv/EsoNbX5IaOrFoeeTtCQDhMNAWt5fDSyK0wYHFpRIsgfBoB+k3LhqK5SWfLgSlvc9XYMBwDx84mIySkFyWVCdWvvtB3d7IR7hsiRrQQIDAQAB"));
        System.out.println(HashKit.rsaCheckContent("E2eP8QycBb6JKG2E4P7RPF5J5cQBdEaW","MF+GEiSWkEaxztiBjuRd5sImTm9qlRjlhxDYnQJ56EcuWu+wnMmcRGm5XealKuW7exeo70M/mcfbbSZwsJgDbBCMj67/MXOdHNBoxnOse1EXUzAw4JP3PkFTShj2hL12c6IHVBi2ozwzRyEteRG/hek5D0vmKQ950ozdzsh/Vig=","MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCh99I6wpK5ibW+r4tRzMajpUeTwzFrcjS2/1X8GkH74oOa+0/B6mJvDaU5+axbjkbQrwOD+qLx8aEOn0rxLtTlWel8Xby0V9UDXhXHhWurnwAZbZPl18fut3LyOkC5jAnrHRMSyKlg7dQukClDpxGyIuCgYZ449vbtAh9C5mWJnwIDAQAB","UTF-8"));
        //System.out.println(RSA.pubEncrypt("fasfdasfadsf","MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCQ5XO6Gfho0Sro0SSzxAlOfQYyBWudT7nPzr2KBm4jFd8/rRmlBTd0gbAhBWnTPU+L0mv/EsoNbX5IaOrFoeeTtCQDhMNAWt5fDSyK0wYHFpRIsgfBoB+k3LhqK5SWfLgSlvc9XYMBwDx84mIySkFyWVCdWvvtB3d7IR7hsiRrQQIDAQAB"));
        //System.out.println(RSA.priDecrypt("iyWYDqUPCgGqA/VLFH2W/yYCU62XIHt9+dL1eKkLf2XmQscBzsBY1f+P6p79yYbNd2ODz7BCCC6J+bTqH8EqH9tZx6Qu0xiDUzwFVPf5YmlRa4GLP/BmeJvLos8mTT5tdt/B27+JzVkLN8R1zplQTtAU/xTyx8zwA3bXoi/kfwU=","MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJDlc7oZ+GjRKujRJLPECU59BjIFa51Puc/OvYoGbiMV3z+tGaUFN3SBsCEFadM9T4vSa/8Syg1tfkho6sWh55O0JAOEw0Ba3l8NLIrTBgcWlEiyB8GgH6TcuGorlJZ8uBKW9z1dgwHAPHziYjJKQXJZUJ1a++0Hd3shHuGyJGtBAgMBAAECgYAuDjC1o5GYx3trGyceWJ5Yq1avyZdGHlRCYEVQvPB/CHG4jK3NDRZuWtZ/+HeFpERHfi/DmNZOZFIyc97DyM2RY6AMjlCLc/BeUNKfBgBZOGK52/H9JTanv91YhghRt/J+y6iVDyQpvGQCgEswuc0jt3eeojGmsj5q8RUR3J70LQJBAMtXzdgbRyAukJjHiWLb0Pe/F0rl7EjfKZi2VwHPm/m6LZ26DWIZGe7beQFvXhi+Uv6YXjWC4NPBSW0fhEkJzVMCQQC2awwxfaO2vOdgC0uJEiVl2o94Q/KTI/VUY4ARJLpoMsQ4c2+rRZ3UO7S4FxURslgcjlBnJ/OQJqbml73WOT6bAkEAjg4RJ+AmmDzvl0wOlpmqKum8g/6MS3xbuuvnbMR02slOBNptbxLKIywAJRiATBjqMiKP7iI4nrZxbWKPL9dBMQJAdMBUwIQyOO30hFMWWDdl+ieFdaq/YY89IHLCb7Z7G9+KGLtBOHRJDKHeC4iycm4b2ML5ENzQFW23mpR56tceiwJBAJyzuACLBwVamoZifIBGQ8GlHjRe2eKj9gPv7qMzCPZPDPHOl0R6xdoa5mXXlj3o/BxN0sKAgVPt5wlIy9nwTF8=\n"));


}

}
