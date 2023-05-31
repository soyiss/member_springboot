package com.example.member;

import com.example.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class memberTest {

    @Autowired
    MemberRepository memberRepository;


    @Test
    @DisplayName("repository method 테스트")
    public void repositoryTest(){
        memberRepository.findByMemberEmail("aaa");
        memberRepository.findByMemberEmailAndMemberPassword("aaa","1234");
    }


}
