package com.makingTeam.controller;

import com.makingTeam.vo.LoginVO;
import com.makingTeam.vo.common.ApplicationOauthKeyVO;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;

@RestController
@RequestMapping(value = "/login")
public class LoginContorller {

    private KakaoController kakao;
    private ApplicationOauthKeyVO appKeyVO;

    @RequestMapping(value = "/sign")
    public ModelAndView sign(){
        System.out.println("@@@@@@ /login/sign start @@@@@@");
        ModelAndView mav = new ModelAndView("thymeleaf/login");
        return mav;
    }

    @RequestMapping(value = "/sign/kakao")
    @ResponseBody
    public String signKakao(LoginVO vo){
        System.out.println("@@@@@@ /sign/kakao start @@@@@@");
//        System.out.println(param.get("id"));
//        System.out.println(param.get("pw"));
        System.out.println(vo.getId());
        System.out.println(vo.getPw());

//        HttpsURLConnection conn;
//        try {
//            URL url = new URL("http://naver.com");
//            conn = (HttpsURLConnection) url.openConnection();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        try {
            kakaoTest();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public void kakaoTest() throws IOException {
        String urlString = "https://kauth.kakao.com/oauth/authorize?client_id=c69874219bbb8b7fe847a0e55f3dba05&redirect_uri=http://localhost:8080/login/sign&response_type=code";
        String line = null;
        InputStream in = null;
        BufferedReader reader = null;
        HttpsURLConnection httpsConn = null;

        try {
            // Get HTTPS URL connection
            URL url = new URL(urlString);
            httpsConn = (HttpsURLConnection) url.openConnection();

            // Set Hostname verification
            httpsConn.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    // Ignore host name verification. It always returns true.
                    return true;
                }
            });

            // Input setting
            httpsConn.setDoInput(true);
            // Output setting
            //httpsConn.setDoOutput(true);
            // Caches setting
            httpsConn.setUseCaches(false);
            // Read Timeout Setting
            httpsConn.setReadTimeout(1000);
            // Connection Timeout setting
            httpsConn.setConnectTimeout(1000);
            // Method Setting(GET/POST)
            httpsConn.setRequestMethod("GET");
            // Header Setting
            httpsConn.setRequestProperty("HeaderKey","HeaderValue");

            int responseCode = httpsConn.getResponseCode();
            System.out.println("응답코드 : " + responseCode);
            System.out.println("응답메세지 : " + httpsConn.getResponseMessage());

            // SSL setting
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, null, null); // No validation for now
            httpsConn.setSSLSocketFactory(context.getSocketFactory());

            // Connect to host
            httpsConn.connect();
            httpsConn.setInstanceFollowRedirects(true);

            // Print response from host
            if (responseCode == HttpsURLConnection.HTTP_OK) { // 정상 호출 200
                in = httpsConn.getInputStream();
            } else { // 에러 발생
                in = httpsConn.getErrorStream();
            }
            reader = new BufferedReader(new InputStreamReader(in));
            while ((line = reader.readLine()) != null) {
                System.out.printf("%s\n", line);
            }

            reader.close();
        } catch (UnknownHostException e) {
            System.out.println("UnknownHostException : " + e);
        } catch (MalformedURLException e) {
            System.out.println(urlString + " is not a URL I understand");
        } catch (IOException e) {
            System.out.println("IOException :" + e);
        } catch (Exception e) {
            System.out.println("error : " + e);
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (httpsConn != null) {
                httpsConn.disconnect();
            }
        }
    }
}
