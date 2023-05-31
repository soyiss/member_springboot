package com.example.member.repository;

import com.example.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity,Long> {
    // 인터페이스에서 정의할수있는 메소드는 추상메소드만 정의할수 있다
    /*
        select => findBy
        select * from member_table where member_email=?
     */

    // 아이디가 아닌 다른컬럼을 기준으로 조회를 하려면 repository에다가 메서드를 정의해야된다
    // 규칙에 맞게끔 메서드를 정의해주면 JPA가 알아서 쿼리를 추가해준다

    Optional<MemberEntity> findByMemberEmail(String memberEmail);
    // 만약 쿼리문을 select * from member_table where member_email=? and member_password=? 이런형태의 쿼리문을 쓰고싶다면(조건이 2개인 쿼리문)
    // 밑에처럼 메서드를 정의하면 된다( 대 소문자 중요@!!)
    //findByMemberEmailAndMemberPassword에 쓰는 이름은 Entity에 정의한 컬럼이름을 쓰면된다.

    Optional<MemberEntity> findByMemberEmailAndMemberPassword(String memberEmail,String memberPassword);
}
