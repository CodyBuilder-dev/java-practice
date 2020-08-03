package com.example.first.controller;

// Annotation들도 import 해줘야 함
// VS Code Extension 덕분인지 Annotation 사용시 자동으로 import가 추가되긴 함
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {
    
    @RequestMapping(value = "/home")
    public String home() {
        return "index.html";
    }

    @ResponseBody // 안 쓰고 view 대신 data를 반환할 경우 에러
    @RequestMapping("/valueTest")
    public String valueTest() {
        String value = "테스트 string";
        return value;
    }




    @RequestMapping("/test")
    public ModelAndView test() throws Exception{
        ModelAndView mav = new ModelAndView("test");
        mav.addObject("name", "goddaehee"); 

        List<String> testList = new ArrayList<String>(); 
        testList.add("a"); 
        testList.add("b"); 
        testList.add("c"); 
        
        mav.addObject("list", testList); 
        return mav; 
    }


}