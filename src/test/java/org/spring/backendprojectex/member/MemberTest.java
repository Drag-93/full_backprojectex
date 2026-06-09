package org.spring.backendprojectex.member;

import org.junit.jupiter.api.Test;
import org.spring.backendprojectex.common.Role;
import org.spring.backendprojectex.member.entity.MemberEntity;
import org.spring.backendprojectex.member.repository.MemberRepository;
import org.spring.backendprojectex.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class MemberTest {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    MemberService memberService;

    //1회원가입 -> 5
    @Test
    void insert() {


        for (int i = 2; i <= 10; i++) {
            memberRepository.save(MemberEntity.builder()
                    .userEmail("m"+i+"@email.com")
                    .userPw(passwordEncoder.encode("11"))
                    .userName("m"+i)
                    .role(Role.MEMBER)
                    .build());
        }
    }
}
