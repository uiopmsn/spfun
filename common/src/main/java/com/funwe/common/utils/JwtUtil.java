package com.funwe.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.HashMap;

/**
 * @Author: Corey
 * @Description:
 * @Date: 2020/2/11 23:10
 */
public class JwtUtil {
    /**
     * 过期时间为12小时
     */
    private static final long EXPIRE_TIME = 12*60*60*1000;

    /**
     * token私钥
     */
    private static final String TOKEN_SECRET = "jwifjoiw2397432jsdjifw,jsds";

    /**
     * 生成签名
     * @param username
     * @param password
     * @return
     */
    public static String sign(String username, String password){
        //过期时间
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        //私钥及加密算法
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        //设置头信息
        HashMap<String, Object> header = new HashMap<>(2);
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        //附带username和userID生成签名
        return JWT.create()
                .withHeader(header)
                .withClaim("username",username)
                .withClaim("password",password)
                .withExpiresAt(date)
                .sign(algorithm);
    }

    /**
     * 校验token
     * @param token
     * @return
     */
    public static boolean verity(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        } catch (JWTVerificationException e) {
            return false;
        }

    }

    /**
     * 校验token 成功返回用户名
     * @param token
     * @return
     */
    public static String verityAndGetUser(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            String userName = jwt.getClaim("username").asString();
            return userName;
        } catch (IllegalArgumentException e) {
            return "";
        } catch (JWTVerificationException e) {
            return "";
        }

    }


}
