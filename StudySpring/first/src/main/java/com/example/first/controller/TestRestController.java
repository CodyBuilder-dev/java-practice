package com.example.first.controller;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//RequestMethod도 import 필요
import org.springframework.web.bind.annotation.RequestMethod;

//JSP를 위한 ModelAndView 
import org.springframework.web.servlet.ModelAndView;

/*Spring 4.0이상부터는 @RestController 사용 가능
@RestController = @ResponseBody + @Controller
*/
@RestController
public class TestRestController {
    
    // @Autowired
    // TestService testService;

    @RequestMapping(value="/testValue",method = RequestMethod.GET)
    public String getTestValue2() {
        String TestValue = "레스트컨트롤러 테스트";
        return TestValue;
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