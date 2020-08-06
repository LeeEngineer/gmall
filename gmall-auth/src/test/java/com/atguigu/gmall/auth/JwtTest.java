package com.atguigu.gmall.auth;

import com.atguigu.gmall.common.utils.JwtUtils;
import com.atguigu.gmall.common.utils.RsaUtils;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Lee_engineer
 * @create 2020-08-04 23:05
 */
public class JwtTest {

    private static final String PUBKEYPATH = "D:\\gulimall\\key\\rsa.pub";
    private static final String PRIKEYPATH = "D:\\gulimall\\key\\rsa.pri";



    public static void main(String[] args) throws Exception {
//        genKey(PUBKEYPATH,PRIKEYPATH,"ja9871JLS88SKLkjs");
        PublicKey publicKey = RsaUtils.getPublicKey(PUBKEYPATH);
        PrivateKey privateKey = RsaUtils.getPrivateKey(PRIKEYPATH);
        String token = genToken(privateKey);
        System.out.println(token);

        Map<String, Object> map = getInfoFromToken(token, publicKey);
        System.out.println("username=" + map.get("username"));
        System.out.println("id=" + map.get("id"));


    }

    public static void genKey(String pubkeyPath,String prikeyPath,String secret) throws Exception {
        RsaUtils.generateKey(pubkeyPath,prikeyPath,secret);
    }

    public static String genToken(PrivateKey privateKey) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("id", "11");
        map.put("username", "liuyan");
        // 生成token
        String token = JwtUtils.generateToken(map, privateKey, 5);
        return token;
    }

    public static Map<String,Object> getInfoFromToken(String token,PublicKey publicKey) throws Exception {

        Map<String, Object> map = JwtUtils.getInfoFromToken(token, publicKey);
        return map;

    }

}
