package com.example.member.config;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

// 로그인 여부를 확인하고  로그인 상태라면 사용자가 요청한 주소로 보내고
// 로그인 하지 않은 상태라면 컨트롤러의 로그인 요청 주소로 넘김
public class loginCheckInterceptor implements HandlerInterceptor {
    // 인터셉터는 컨트롤러로 가기전에 한번더 체크하는것

    @Override //메서드 재정의(매개변수를 바꿀 수 없다)
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

        //사용자가 요청한 주소 확인
        String requestURI = request.getRequestURI();
        System.out.println("requestURI = " + requestURI);
        //세션 객체 생성
        HttpSession session =request.getSession();
        // 세션에 저장된 로그인 정보 확인
        if(session.getAttribute("loginEmail") == null){
            //로그인 하지 않았다면 로그인 페이지로 보내면서
            // 요청한 주소값도 같이 보냄
            response.sendRedirect("/member/login?redirectURI="+requestURI);
            return false;
        }else{
            //로그인 상태라면 요청한 페이지로 보냄(거르지않음)
            return true;

        }
    }


}
