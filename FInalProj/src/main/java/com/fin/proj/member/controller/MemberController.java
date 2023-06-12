package com.fin.proj.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fin.proj.member.model.exception.MemberException;
import com.fin.proj.member.model.service.MemberService;
import com.fin.proj.member.model.vo.Member;

@Controller
public class MemberController {
	
	@Autowired
	private MemberService mService;
	
	@RequestMapping("loginView.me")
	public String loginView() {
		return "login";
	}
	
	@GetMapping(value="login.me")
	public String login(@RequestParam("uId") String uId, @RequestParam("uPwd") String uPwd,
						Member m, Model model) {
		
		m.setuId(uId);
		m.setuPwd(uPwd);
		
		Member loginUser = mService.login(m);
		
		if(loginUser != null) {
			System.out.println("로그인 성공");
			return "redirect:home.do";
		} else {
			System.out.println("로그인 실패");
			throw new MemberException("로그인에 실패하였습니다.");
		}
	}
	
	@RequestMapping("enroll.me")
	public String enroll() {
		return "enroll";
	}
	
	@RequestMapping("findId.me")
	public String findId() {
		return "findId";
	}
	
	@RequestMapping("findPwd.me")
	public String findPwd() {
		return "findPwd";
	}

}
