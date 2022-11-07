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
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;

@RestController
@RequestMapping(value = "/login")
public class LoginContorller {

    public enum HttpMethodType { POST, GET, DELETE }

    private KakaoController kakao;
    private ApplicationOauthKeyVO appKeyVO = new ApplicationOauthKeyVO();

    @RequestMapping(value = "/sign")
    public ModelAndView sign(){
        System.out.println("@@@@@@ /login/sign start @@@@@@");
        ModelAndView mav = new ModelAndView("thymeleaf/login");
        return mav;
    }

    @RequestMapping(value = "/sign/kakao")
    @ResponseBody
    public String signKakao(@RequestParam HashMap<String,Object> param) throws IOException{
        System.out.println("@@@@@@ /sign/kakao start @@@@@@");
//        ModelAndView mav = new ModelAndView("thymeleaf/common/apiView");
//        mav.addObject("htmlBody",request(appKeyVO.getKAKAO_LOGIN_URL(), HttpMethodType.POST, null));

        return request(appKeyVO.getKAKAO_LOGIN_URL(), HttpMethodType.POST, null);
    }

    public String request(String requestUrl, HttpMethodType httpMethod, StringBuilder sb) throws IOException {
        String line = null;
        InputStream in = null;
        BufferedReader reader = null;
        HttpsURLConnection httpsConn = null;
        StringBuilder rs = new StringBuilder();

        try{
            // Get HTTPS URL connection
            URL url = new URL(requestUrl);
            httpsConn = (HttpsURLConnection) url.openConnection();

            httpsConn.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    // Ignore host name verification. It always returns true.
                    return true;
                }
            });

            if(httpMethod == null){
                httpMethod = httpMethod.GET;
            }

            // Input setting
            httpsConn.setDoInput(true);
            // Output setting
            httpsConn.setDoOutput(true);
            // Header Setting
            httpsConn.setRequestMethod(httpMethod.toString());

            if(sb != null){
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(httpsConn.getOutputStream()));
                bw.write(sb.toString());
                bw.flush();
            }

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
                rs.append(line);
                System.out.printf("%s\n", line);
            }
            return rs.toString();

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
        return null;
    }

    public String mapToParam(HashMap<String, Object> map){
        StringBuilder paramBuilder = new StringBuilder();
        for(String key : map.keySet()){
            paramBuilder.append(paramBuilder.length() > 0 ? "&" : "");
            paramBuilder.append(key + "=" + map.get(key).toString());
        }

        return paramBuilder.toString();
    }

    public void kakaoTest(String accessToken) throws IOException {
        // "https://kauth.kakao.com/oauth/authorize?client_id=c69874219bbb8b7fe847a0e55f3dba05&redirect_uri=http://localhost:8080/login/sign/kakao&response_type=code"  // 로그인 페이지 호출
        // https://kauth.kakao.com/oauth/token  // 엑세스 토큰 url
        // https://kapi.kakao.com/v2/user/me    // 사용자 정보 url

        String urlString = "https://kauth.kakao.com/oauth/token";
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
            httpsConn.setDoOutput(true);
            // Caches setting
//            httpsConn.setUseCaches(false);
            // Read Timeout Setting
//            httpsConn.setReadTimeout(1000);
            // Connection Timeout setting
//            httpsConn.setConnectTimeout(1000);
            // Method Setting(GET/POST)
            /** 카카오 최초 로그인시 */
//            httpsConn.setRequestMethod("GET"); // kakao login
//            httpsConn.setRequestProperty("HeaderKey","HeaderValue");

            /** 카카오 로그인 후 권한 요청 시 */
            httpsConn.setRequestMethod("POST"); // 카카오 로그인 확인 권한
            httpsConn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(httpsConn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=" + appKeyVO.getKAKAO_REST_API_KEY());
            sb.append("&redirect_uri=http://localhost:8080/login/sign/kakao");
            sb.append("&code=" + accessToken);
            bw.write(sb.toString());
            bw.flush();
            // Header Setting


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
