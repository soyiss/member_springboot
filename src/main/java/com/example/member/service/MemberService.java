package com.example.member.service;

import com.example.member.dto.MemberDTO;
import com.example.member.entity.MemberEntity;
import com.example.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service

@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Long save(MemberDTO memberDTO) {
        MemberEntity memberEntity = MemberEntity.toSaveEntity(memberDTO);
        return memberRepository.save(memberEntity).getId();
    }

    public List<MemberDTO> findAll() {
        List<MemberEntity> memberEntityList = memberRepository.findAll();
        List<MemberDTO> memberDTOList = new ArrayList<>();
        for (MemberEntity memberEntity: memberEntityList) {
            memberDTOList.add(MemberDTO.toDTO(memberEntity));
        }
        return memberDTOList;
    }

    public boolean login(MemberDTO memberDTO) {
        Optional<MemberEntity> memberEntity =
                memberRepository.findByMemberEmailAndMemberPassword(memberDTO.getMemberEmail(), memberDTO.getMemberPassword());
        if (memberEntity.isPresent()) {
            return true;
        } else {
            return false;
        }
    }

    public MemberDTO findByEmail(String loginEmail) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberEmail(loginEmail);
        if (optionalMemberEntity.isPresent()) {
            MemberEntity memberEntity = optionalMemberEntity.get();
            MemberDTO memberDTO = MemberDTO.toDTO(memberEntity);
            return memberDTO;

        } else {
            return null;
        }
    }

    public void update(MemberDTO memberDTO) {
        MemberEntity memberEntity = MemberEntity.toupdateEntity(memberDTO);
        memberRepository.save(memberEntity);
    }

    public void loginAxios(MemberDTO memberDTO) {
        // chaining method (체이닝 메서드)
        //.orElseThrow()메소드는 옵셔널 객체의 값이 없으면 바로 예외를 던져주는 메소드이다,NoSuchElementException("이메일 또는 비밀번호가 틀립니다.")가 발동함
        memberRepository.findByMemberEmailAndMemberPassword(memberDTO.getMemberEmail(), memberDTO.getMemberPassword())
                .orElseThrow(() -> new NoSuchElementException("이메일 또는 비밀번호가 틀립니다"));
    }

    public MemberDTO findById(Long id) {
        //익명함수
        //넘겨줄 매개변수가 없으니까 ()안에는 아무것도 안씀
        //orElseThrow로 없을 경우에 대한 처리가 끝났으니까 리턴을 dto객체로 받을수있다
       MemberEntity memberEntity  = memberRepository.findById(id).orElseThrow(()->new NoSuchElementException());
        return MemberDTO.toDTO(memberEntity);

        //위에 두줄말고 다르게 쓰는 방법
//        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(id);
//        if(optionalMemberEntity.isPresent()){
//            MemberEntity memberEntity1  = optionalMemberEntity.get();
//            return MemberDTO.toDTO(memberEntity);
//        }else{
//            return  null;
//        }
    }

    public void delete(Long id) {
        memberRepository.deleteById(id);
    }
}
