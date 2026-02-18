package com.example.first.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

@Setter
@Getter
@Service
@SessionScope
public class SessionScopeService {
  public String message;

}
