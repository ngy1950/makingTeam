package com.makingTeam.controller;

import com.makingTeam.service.SampleService;
import com.makingTeam.vo.sampleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class SampleController {

    @RequestMapping(value = "/main")
    public String main(){
        return "index.html";
    }

    @ResponseBody
    @RequestMapping(value = "/sample")
    public String sample(){
        System.out.print("sample!!!");
        String data = "@ResponseBody 어노테이션을 통해 반환";
        return data;
    }

    @RestController
    public class SampleRestController{

        @Autowired
        SampleService service;

        @RequestMapping(value = "/sampleValue", method = RequestMethod.GET)
        public String getSampleValue(){
            String data = "@RestController = @Controller + @ResponseBody 이며 ,view 반환 외 json 데이터 반환 시 사용";
            return data;
        }

        /*
         * 스프링부트에서의 jsp 호출 테스트
         */
        @RequestMapping("/jspSample")
        public ModelAndView jspSample() throws Exception{
            System.out.println("@@@@@ jspSample start @@@@@");
            ModelAndView mav = new ModelAndView("jspSample");
            mav.addObject("name", "홍길동");

            List<String> jspSample = new ArrayList<String>();
            jspSample.add("국어 : 100점");
            jspSample.add("수학 : 90점");
            jspSample.add("영어 : 75점");

            mav.addObject("list", jspSample);
            return mav;
        }

        /*
         *  thtmeleaf 일반 호출 에제
         */
        @RequestMapping("/SampleThymeleaf")
        public String sampleThymeleaf(Model model){
            System.out.println("@@@@@ SampleThymeleaf start @@@@@");
            String text = "타임리프에 전달되는 데이터입니다.";
            model.addAttribute("text",text);
            return "thymeleaf/SampleThymeleaf";
        }

        /*
         *  thtmeleaf 일반 호출 에제
         */
        @RequestMapping("/SampleThymelea2f")
        public ModelAndView sampleThymeleaf2(Model model){
            System.out.println("@@@@@ SampleThymeleaf start @@@@@");
            ModelAndView mav = new ModelAndView("thymeleaf/SampleThymeleaf");
            String text = "타임리프에 전달되는 데이터입니다.";
            mav.addObject("text", text);
            return mav;
        }

        @RequestMapping(value = "/emp", method = RequestMethod.GET)
        public ModelAndView selectSampleEmp(HttpServletRequest request){
            System.out.println("@@@@@ DATABASE selectEmp start @@@@@");
            ModelAndView mav = new ModelAndView();
            List<sampleVO> sampleList = service.selectEmp();
            mav.addObject("LIST", sampleList);
            mav.setViewName("thymeleaf/emp");
            return mav;
        }

        @RequestMapping(value = "/apiView", method = RequestMethod.GET)
        public ModelAndView getApiView(HttpServletRequest request){
            System.out.println("@@@@@ apiView start @@@@@");
            return new ModelAndView("thymeleaf/common/apiView");
        }

        @RequestMapping(value = "/sampleHashMap")
        @ResponseBody
        public String sampleHasMap(@RequestParam HashMap<String,Object> param){
            return "";
        }
    }
}
