package com.example.member.controller;

import com.example.member.dto.MemberDTO;
import com.example.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/save")
    public String saveForm() {
        return "memberPages/memberSave";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute MemberDTO memberDTO) {
        memberService.save(memberDTO);
        return "index";
    }

    @GetMapping("/")
    public String findAll(Model model) {
        List<MemberDTO> memberDTOList = memberService.findAll();
        model.addAttribute("memberList", memberDTOList);
        return "memberPages/memberList";
    }


    @GetMapping("/login")
    public String loginForm(@RequestParam(value="redirectURI", defaultValue = "/member/mypage")String redirectURI,Model model) {
        //defaultValue = "/member/mypage"는 memberMain으로 가기위한 주소
        // @RequestParam(value="redirectURI", defaultValue = "/member/mypage"의 목적은 사용자가 직전에 사용한 주소값을 받아주기 위한것이다
        System.out.println("MemberController.loginForm");
        System.out.println("redirectURI = " + redirectURI);
        model.addAttribute("redirectURI",redirectURI);
        return "memberPages/memberLogin";
    }

    @PostMapping("/login")
    public String memberLogin(@ModelAttribute MemberDTO memberDTO, HttpSession session,@RequestParam("redirectURI") String redirectURI) {
        System.out.println("MemberController.memberLogin");
        System.out.println("URI" + redirectURI);
        boolean loginResult = memberService.login(memberDTO);
        if (loginResult) {
            session.setAttribute("loginEmail", memberDTO.getMemberEmail());
//            return "memberPages/memberMain";

            //로그인 성공하면 사용자가 직전에 요청한 주소로 redirect
            //인터셉터 걸리지 않고 처음부터 로그인하는 사용자였다면
            // redirect:/member/mypage로 요청되며, memberMain 화면으로 전환됨.
            return "redirect:"+redirectURI;
        } else {
            return "memberPages/memberDetail";
        }
    }

    // throws Exception은 예외가 생기는 경우(이메일,비번 틀렸을때) 곧바로 예외를 axios의 catch로 던질거다.
    @PostMapping("/login/axios")
    public ResponseEntity memberLoginAxios(@RequestBody MemberDTO memberDTO, HttpSession session) throws Exception {
        memberService.loginAxios(memberDTO);
        session.setAttribute("loginEmail", memberDTO.getMemberEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/axios/{id}")
    public ResponseEntity detailAxios(@PathVariable Long id) throws Exception {
        MemberDTO memberDTO = memberService.findById(id);
        return new ResponseEntity<>(memberDTO, HttpStatus.OK);
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // 세션에 담긴 값 전체 삭제
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/update")
    public String updateForm(HttpSession session, Model model) {
        String loginEmail = (String) session.getAttribute("loginEmail");

        MemberDTO memberDTO = memberService.findByEmail(loginEmail);
        model.addAttribute("memberDTO", memberDTO);
        //위에 두줄대신 이렇게 써도된다
//        model.addAttribute("memberDTO",memberService.findByEmail(loginEmail));
        return "/memberPages/memberUpdate";
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody MemberDTO memberDTO) {
        memberService.update(memberDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 주소 요청이 delete인 경우 어노테이션을 DeleteMapping을 사용한다
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        memberService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        MemberDTO memberDTO = memberService.findById(id);
        model.addAttribute("member", memberDTO);
        return "memberPages/memberDetail";
    }

    @GetMapping("/mypage")
    public String myPage() {
        return "memberPages/memberMain";
    }

    @PostMapping("/dup-check")
    public ResponseEntity emailCheck(@RequestBody MemberDTO memberDTO) {
//        memberService.findByEmail(memberDTO.getMemberEmail());
        boolean result = memberService.emailCheck(memberDTO.getMemberEmail());
        if (result) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        }

    }

    /*
        1. Pattern.compile(exp)을 사용하여 정규식 패턴을 컴파일합니다.
        2. pattern.matcher(memberDTO.getMemberPassword())를 통해 memberDTO 객체의 비밀번호를 정규식 패턴과 비교할 수 있는 Matcher 객체를 생성합니다.
        3. matcher.matches()를 호출하여 비밀번호가 정규식 패턴과 일치하는지를 확인합니다. 일치할 경우 true를 반환하고, 그렇지 않을 경우 false를 반환합니다.
        4. 확인 결과를 반환합니다.
        5. 따라서 클라이언트는 /pass-rule 엔드포인트로 POST 요청을 보내고, 요청 본문에 회원 비밀번호를 포함시키면 서버는 해당 비밀번호가 정규식 규칙을 만족하는지를 확인하고,
           그 결과를 boolean 값으로 반환합니다. 이를 통해 클라이언트는 회원 비밀번호의 유효성을 검증할 수 있습니다.
    */
//    @PostMapping("/pass-rule")
//    public boolean passRule(@RequestBody MemberDTO memberDTO) {
//        String exp = "^(?=.*[a-z])(?=.*\\d)(?=.*[-_!#])[A-Za-z\\d-_!#]{5,10}$";
//        Pattern pattern = Pattern.compile(exp);
//        Matcher matcher = pattern.matcher(memberDTO.getMemberPassword());
//        boolean isValid = matcher.matches();
//        return isValid;
//    }
}