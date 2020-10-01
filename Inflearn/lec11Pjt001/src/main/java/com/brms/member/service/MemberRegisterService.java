package com.brms.member.service;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.brms.member.Member;
import com.brms.member.dao.MemberDao;

public class MemberRegisterService implements InitializingBean, DisposableBean {

	@Autowired
	private MemberDao memberDao;
	
	public MemberRegisterService() { }
	
	public void register(Member member) {
		memberDao.insert(member);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("MemberRegisterService 객체 생성");
	}
	@Override
	public void destroy() throws Exception {
		System.out.println("MemberRegisterService 객체 소멸");
	}


}
