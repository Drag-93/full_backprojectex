package org.spring.backendprojectex.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.spring.backendprojectex.common.Role;
import org.spring.backendprojectex.member.entity.MemberEntity;
import org.spring.backendprojectex.member.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Service
public class MyOAuth2UserService extends DefaultOAuth2UserService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    //OAuth -> 사용자
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        ClientRegistration clientRegistration = userRequest.getClientRegistration(); //사용자 정보
        String registrationId=clientRegistration.getRegistrationId();   //google, kakao, naver
        //로그인 사용자(구글, 카카오, 네이버)
        return oAuth2UserSuccess(oAuth2User, registrationId);
    }

    private OAuth2User oAuth2UserSuccess(OAuth2User oAuth2User, String registrationId) {
        String userEmail="";
        String userName="";
        String userPw="";

        if(registrationId.equals("google")){
            userEmail=oAuth2User.getAttribute("email");
            userName=oAuth2User.getAttribute("name");
            System.out.println((String) oAuth2User.getAttribute("email"));
            System.out.println((String) oAuth2User.getAttribute("name"));
        }else if(registrationId.equals("kakao")){
            Map<String, Object> kakaoAccount =
                    (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");
            Map<String, Object> profile =
                    (Map<String, Object>) kakaoAccount.get("profile");

            userEmail = (String) kakaoAccount.get("email");
            userName = (String) profile.get("nickname");

            System.out.println("카카오 id: " + oAuth2User.getAttributes().get("id"));
            System.out.println("카카오 email: " + userEmail);
            System.out.println("카카오 nickname: " + userName);

        }else if(registrationId.equals("naver")){
            System.out.println("네이버 로그인");
            Map<String,Object> response=(Map<String,Object>) oAuth2User.getAttributes().get("response");

            userEmail=(String)response.get("email");
            userName=(String)response.get("name");

            System.out.println(response);
            System.out.println((String)response.get("id"));
            System.out.println((String)response.get("nickname"));
            System.out.println((String)response.get("gender"));
            System.out.println((String)response.get("email"));
            System.out.println((String)response.get("name"));
        }
        if(userEmail==null||userEmail.isEmpty()){
            throw new OAuth2AuthenticationException("이메일 정보가 없습니다.");
        }

        Optional<MemberEntity> optionalMemberEntity=memberRepository.findByUserEmail(userEmail);

        //등록 되어 있으면
        if(optionalMemberEntity.isPresent()){
            //기존 회원 관리
            return new MyUserDetailsImpl(optionalMemberEntity.get());
        }
        //처음 SNS회원 등록(신규 가입)
        //비밀번호 암호화
        userPw=passwordEncoder.encode("11");
        MemberEntity memberEntity=MemberEntity.builder()
                .userEmail(userEmail)
                .userPw(userPw)
                .userName(userName)
                .role(Role.MEMBER).build();
        memberRepository.save(memberEntity);

        return new MyUserDetailsImpl(memberEntity, oAuth2User.getAttributes());
    }

}
