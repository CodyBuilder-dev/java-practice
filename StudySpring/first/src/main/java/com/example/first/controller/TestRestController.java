package com.example.first.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//RequestMethod도 import 필요
import org.springframework.web.bind.annotation.RequestMethod;


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
}