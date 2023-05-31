package com.example.member.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name="member_table")
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length=50,unique = true,nullable = false)
    private String memberEmail;
    @Column(length=20,nullable = false)
    private String memberPassword;
    @Column(length=20,nullable = false)
    private String memberName;
    @Column(length=20)
    private String memberBirth;
    @Column(length = 30)
    private String memberMobile;

}
