package com.example.member.controller;

import com.example.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor // final을 적용한 필드를 매개변수로 하는 생성자
public class MemberController {
    private final MemberService memberService;
}
