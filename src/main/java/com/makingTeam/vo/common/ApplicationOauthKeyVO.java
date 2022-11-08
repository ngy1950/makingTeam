package com.makingTeam.vo.common;

import lombok.Getter;

import java.util.List;

@Getter
public class ApplicationOauthKeyVO {
    final private String KAKAO_REST_API_KEY;
    final private String KAKAO_JAVASCRIPT_KEY;
    final private String KAKAO_ADMIN_KEY;
    final private String HOST_URL = "https://kauth.kakao.com/";
    final private String ACCESSTOKEN_URL = "oauth/token";
    final private String GET_USER_URL = "v2/user/me";
    final private String REDIRECT_URL_SIGN = "http://localhost:8080/login/sign/kakao";
    private String[] adminPaths = null;

    public ApplicationOauthKeyVO(){
        this.KAKAO_REST_API_KEY = "c69874219bbb8b7fe847a0e55f3dba05";
        this.KAKAO_JAVASCRIPT_KEY = "72fe965decd3024c98db2fe76e0718eb";
        this.KAKAO_ADMIN_KEY = "c95328242fa70e59fe844f7c859f5250";
        this.adminPaths = new String[]{ACCESSTOKEN_URL, GET_USER_URL};
    }
    public String getKAKAO_LOGIN_URL(){
        return "https://kauth.kakao.com/oauth/authorize?client_id=" + this.getKAKAO_REST_API_KEY() + "&redirect_uri=" + REDIRECT_URL_SIGN + "&response_type=code";
    }
    public String getURL(String request_uri){
        return HOST_URL + request_uri;
    }
}
