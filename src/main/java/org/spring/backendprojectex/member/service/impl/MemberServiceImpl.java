package org.spring.backendprojectex.member.service.impl;

import lombok.RequiredArgsConstructor;
import org.spring.backendprojectex.common.Role;
import org.spring.backendprojectex.member.dto.MemberDto;
import org.spring.backendprojectex.member.entity.MemberEntity;
import org.spring.backendprojectex.member.repository.MemberRepository;
import org.spring.backendprojectex.member.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    // Bean으로 등록
    private final PasswordEncoder passwordEncoder;

    @Override
    public void memberInsert(MemberDto memberDto) {

        Optional<MemberEntity> optionalMemberEntity= memberRepository.findByUserEmail(memberDto.getUserEmail());
        if(optionalMemberEntity.isPresent()){
            throw new IllegalArgumentException("이메일이 존재 합니다!");
        }
        memberRepository.save(MemberEntity.builder()
                .role(Role.MEMBER)
                .userEmail(memberDto.getUserEmail())
                .userName(memberDto.getUserName())
                .userPw(passwordEncoder.encode(memberDto.getUserPw()))  //비빌번호 암호회
                .build());

    }

    @Override
    public MemberDto memberDetail(Long id){
        MemberEntity memberEntity=memberRepository.findById(id).orElseThrow(()->new IllegalArgumentException("회원정보가 존재하지 않습니다."));

        return MemberDto.builder()
                .id(memberEntity.getId())
                .userEmail(memberEntity.getUserEmail())
                .userPw(memberEntity.getUserPw())
                .userName(memberEntity.getUserName())
                .role(memberEntity.getRole())
                .updateTime(memberEntity.getUpdateTime())
                .createTime(memberEntity.getCreateTime())
                .build();
    }

    @Override
    public Page<MemberDto> pagingSearchMemberList(Pageable pageable, String subject, String search) {
        return null;
    }
}
