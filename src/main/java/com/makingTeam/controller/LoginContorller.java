package com.makingTeam.controller;

import com.makingTeam.vo.LoginVO;
import com.makingTeam.vo.common.ApplicationOauthKeyVO;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
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
import java.util.Arrays;
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
        mav.addObject("loginKakao", appKeyVO.getKAKAO_LOGIN_URL());
        return mav;
    }

    @RequestMapping(value = "/sign/kakao")
    @ResponseBody
    public void getAuthorizeKakao(@RequestParam HashMap<String,Object> param) throws IOException{
        System.out.println("@@@@@@ /sign/kakao start @@@@@@");

        if(param != null){
            try {
                HashMap<String, Object> map = new HashMap<String, Object>();
                StringBuilder sb = new StringBuilder();

                sb.append("grant_type=authorization_code");
                sb.append("&client_id=" + appKeyVO.getKAKAO_REST_API_KEY());
                sb.append("&redirect_uri=" + appKeyVO.getREDIRECT_URL_SIGN());
                sb.append("&code=" + param.get("code").toString());

                map.put("HttpMethodType", HttpMethodType.POST.toString());
                map.put("StringBuilder", sb);

                String result = request(appKeyVO.getURL(appKeyVO.getACCESSTOKEN_URL()), map);

                getUserInfoKakao(stringToHashMap(result).get("access_token").toString());

            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void getUserInfoKakao(String access_token){
        System.out.println("@@@@@ start getUserInfoKakao @@@@@");

        try {

            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("HttpMethodType", HttpMethodType.POST.toString());
            map.put("adminPaths", appKeyVO.getGET_USER_URL());
            map.put("accessToken", access_token);
            request(appKeyVO.getURL(appKeyVO.getGET_USER_URL()), map);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String request(String requestUrl, HashMap<String,Object> paramMap) throws IOException {
        String line = null;
        InputStream in = null;
        BufferedReader reader = null;
        HttpsURLConnection httpsConn = null;
        StringBuilder sb = null;
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

            // HttpMethodType httpMethod
            if(paramMap.get("HttpMethodType") == null){
                paramMap.put("HttpMethodType", HttpMethodType.GET);
            }

            if(paramMap.get("StringBuilder") != null){
                sb = (StringBuilder) paramMap.get("StringBuilder");
            }

            if(paramMap.get("adminPaths") != null){
                String findPath = appKeyVO.getAdminPaths()[Arrays.binarySearch(appKeyVO.getAdminPaths(), paramMap.get("adminPaths").toString())];
                if(findPath != null){
                    httpsConn.setRequestProperty("Authorization", "Bearer " + paramMap.get("access_token".toString()));
                }
            }

            // Input setting
            httpsConn.setDoInput(true);
            // Output setting
            httpsConn.setDoOutput(true);
            // Header Setting
            httpsConn.setRequestMethod(paramMap.get("HttpMethodType").toString());

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

    /**
     * @param : HashMap
     * @return : String
     * */
    public String mapToParam(HashMap<String, Object> map){
        StringBuilder paramBuilder = new StringBuilder();
        for(String key : map.keySet()){
            paramBuilder.append(paramBuilder.length() > 0 ? "&" : "");
            paramBuilder.append(key + "=" + map.get(key).toString());
        }

        return paramBuilder.toString();
    }

    /**
     * @param : String
     * @return : HashMap
     * @설명 : 문자열을 HasMap 객체로 변환
     * */
    public HashMap<String, Object> stringToHashMap(String str) throws ParseException {
        return new JSONParser(str).object();
    }

}
