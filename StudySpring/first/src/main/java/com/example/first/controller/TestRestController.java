package com.example.first.controller;

import com.example.first.service.RequestScopeService;
import com.example.first.service.SessionScopeService;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.example.first.service.TestService;
import com.example.first.vo.TestVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    
    @Autowired
    TestService testService;

    @Autowired
    SessionScopeService sessionScopedService;

    @Autowired
    RequestScopeService requestScopedService;

    @RequestMapping(value="/testValue",method = RequestMethod.GET)
    public String getTestValue2() {
      return "레스트컨트롤러 테스트";
    }

    @RequestMapping("/test")
    public ModelAndView test() {
        ModelAndView mav = new ModelAndView("test");
        mav.addObject("name", "goddaehee"); 

        List<String> testList = new ArrayList<>();
        testList.add("a"); 
        testList.add("b"); 
        testList.add("c"); 
        
        mav.addObject("list", testList); 
        return mav; 
    }

    @RequestMapping("/testTime")
    public String testTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    @RequestMapping("/testSvs")
    public ModelAndView testSvs() {
        ModelAndView mav = new ModelAndView("testSvs");

        List<TestVo> testList = testService.selectTest();
        mav.addObject("list",testList);

        return mav;
    }

    @RequestMapping("/scopes/session")
    public String getSessionScopeMessage(@RequestParam final String message) {
        String previousRequestMessageInProxy = requestScopedService.message;
        String previousRequestMessageInBean = requestScopedService.getMessage();

        requestScopedService.message = message + " (set directly into Proxy)";
        requestScopedService.setMessage(message);

        String currentRequestMessageInProxy = requestScopedService.message;
        String currentRequestMessageInBean = requestScopedService.getMessage();

        String previousSessionMessageInProxy = sessionScopedService.message;
        String previousSessionMessageInBean = sessionScopedService.getMessage();

        sessionScopedService.message = message + " (set directly into Proxy)";
        sessionScopedService.setMessage(message);

        String currentSessionMessageInProxy = sessionScopedService.message;
        String currentSessionMessageInBean = sessionScopedService.getMessage();

        return String.format(
            "RequestScopeService bean address: %s"
            + "\nPrevious: %s, Current: %s"
            + "\nRequestScopeService Proxy address: %s"
            + "\nPrevious(proxy): %s, Current(proxy): %s"
            + "\nSessionScopeService bean address: %s"
            + "\nPrevious: %s, Current: %s"
            + "\nSessionScopeService Proxy address: %s"
            + "\nPrevious(proxy): %s, Current(proxy): %s",
            requestScopedService, // 요청이 들어올 때 마다 주소가 바뀜(새로운 객체가 생성됨)
            previousRequestMessageInBean, currentRequestMessageInBean,
            requestScopedService.getClass(), // 요청에 관계없이 항상 동일한 주소가 찍힘
            previousRequestMessageInProxy, currentRequestMessageInProxy,
            sessionScopedService, // 세션이 바뀔 때 마다 주소가 바뀜(새로운 객체가 생성됨
            previousSessionMessageInBean, currentSessionMessageInBean,
            sessionScopedService.getClass(), // 요청에 관계없이 항상 동일한 주소가 찍힘
            previousSessionMessageInProxy, currentSessionMessageInProxy

        );
    }
}