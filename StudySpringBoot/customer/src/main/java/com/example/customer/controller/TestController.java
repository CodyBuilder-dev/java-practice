package com.example.customer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/test")
public class TestController {

  @GetMapping("/testPage")
  public String testPage() {
    return "testPageName";
  }

  @GetMapping("/testString")
  public ResponseEntity<String> testStringApi() {
    return ResponseEntity.ok("Test String");
  }

  @GetMapping("/testStringResponseBody")
  @ResponseBody
  public String testStringRequestBodyApi(String requestBody) {
    return "Test String";
  }

  @GetMapping("/testClass")
  public TestClass testObjectApi() {
    return new TestClass();
  }

  @GetMapping("/testClassResponseBody")
  @ResponseBody
  public TestClass testObjectResponseBodyApi() {
    return new TestClass();
  }


  @GetMapping("/testClassResponseEntity")
  public ResponseEntity<TestClass> testEntityApi() {
    return ResponseEntity.ok(new TestClass());
  }

  public static class TestClass {
    private String name = "Test Name";
    private int value = 42;

    public String getName() {
      return name;
    }

    public int getValue() {
      return value;
    }
  }
}
