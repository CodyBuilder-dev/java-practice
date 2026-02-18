package com.example.first.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

@Setter
@Getter
@Service
@RequestScope
public class RequestScopeService {
  public String message;

}
