package com.example.member;

import com.example.member.dto.MemberDTO;
import com.example.member.repository.MemberRepository;
import com.example.member.service.MemberService;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@SpringBootTest
public class memberTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberService memberService;


    @Test
    @DisplayName("repository method 테스트")
    public void repositoryTest(){
        memberRepository.findByMemberEmail("aaa");
        memberRepository.findByMemberEmailAndMemberPassword("aaa","1234");
    }

    private MemberDTO newMember(int i){
        MemberDTO memberDTO  =  new MemberDTO();
        memberDTO.setMemberEmail("testemail" +i);
        memberDTO.setMemberPassword("testpass" +i);
        memberDTO.setMemberName("testname" +i);
        memberDTO.setMemberBirth("2000-01-01");
        memberDTO.setMemberMobile("010-1111-1111");
        return memberDTO;
    }

    @Test
    public void testData(){
        for(int i=1; i<=20; i++){
            memberService.save(newMember(i));
        }
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("회원가입테스트")
    public void memberSaveTest(){
        MemberDTO memberDTO = newMember(999);
        Long savedId = memberService.save(memberDTO);
        MemberDTO dto = memberService.findById(savedId);
        assertThat(memberDTO.getMemberEmail()).isEqualTo(dto.getMemberEmail());
    }
    @Test
    @Transactional
    @Rollback
    @DisplayName("로그인 테스트")
    public void loginTest() {
        MemberDTO memberDTO = newMember(999);
        memberService.save(memberDTO);
        MemberDTO loginDTO = new MemberDTO();
        loginDTO.setMemberEmail("wrong email");
        loginDTO.setMemberPassword("1234");

        // 리턴값이 없어서 일치를 판단할수없는 상황이라 익셉션이 나는지 안나는지를 판단하는 테스트이다
        // 익셉션이 발생하는지를 판단하는 코드(assertThatThrownBy 메서드 사용)
        // 이메일과 패스워드를 틀렸을때 (NoSuchElementException.class)의 익셉션이 터졌니?
        assertThatThrownBy(() -> memberService.loginAxios(loginDTO))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("회원 삭제테스트")
    public void deleteTest(){
        MemberDTO memberDTO  = newMember(999);
        Long savedId = memberService.save(memberDTO);
        memberService.delete(savedId);

        //삭제를 한 후  파인드바이디를 했을때 NoSuchElementException 익셉션이 터져야 정상적으로 돌아가는거다
        assertThatThrownBy(() -> memberService.findById(savedId))
                .isInstanceOf(NoSuchElementException.class);
    }

}
