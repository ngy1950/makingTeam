package com.makingTeam.vo.common;

import lombok.Getter;

@Getter
public class ApplicationOauthKeyVO {
    final private String KAKAO_REST_API_KEY;
    final private String KAKAO_JAVASCRIPT_KEY;
    final private String KAKAO_ADMIN_KEY;
    final private String REDIRECT_LOGIN_URL = "http://localhost:8080/login/sign/kakao";
    final private String KAKAO_ACCESSTOKEN_URL = "https://kauth.kakao.com/oauth/token";
    final private String KAKAO_GET_USER_URL = "https://kapi.kakao.com/v2/user/me";

    final private String REDIRECT_URI_LOGIN_SIGN_KAKAO = "http://localhost:8080/login/sign/kakao";
//    https://kauth.kakao.com/oauth/authorize?client_id=c69874219bbb8b7fe847a0e55f3dba05&redirect_uri=http://localhost:8080/login/sign/kakao&response_type=code
    public ApplicationOauthKeyVO(){
        this.KAKAO_REST_API_KEY = "c69874219bbb8b7fe847a0e55f3dba05";
        this.KAKAO_JAVASCRIPT_KEY = "72fe965decd3024c98db2fe76e0718eb";
        this.KAKAO_ADMIN_KEY = "c95328242fa70e59fe844f7c859f5250";
    }
    public String getKAKAO_LOGIN_URL(){
        return "https://kauth.kakao.com/oauth/authorize?client_id=" + this.getKAKAO_REST_API_KEY() + "&redirect_uri=" + REDIRECT_LOGIN_URL + "&response_type=code";
    }
    public String getKAKAO_LOGIN_URL(String redirect_uri){
        return "https://kauth.kakao.com/oauth/authorize?client_id=" + this.getKAKAO_REST_API_KEY() + "&redirect_uri=" + redirect_uri + "&response_type=code";
    }
}
