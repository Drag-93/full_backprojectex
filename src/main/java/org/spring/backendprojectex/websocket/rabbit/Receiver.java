package org.spring.backendprojectex.websocket.rabbit;

import org.spring.backendprojectex.member.entity.MemberEntity;
import org.spring.backendprojectex.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Component
public class Receiver {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private MemberRepository memberRepository;

    public void receiveMessage(Question question) throws Exception{
        Thread.sleep(100);
        LocalDateTime today = LocalDateTime.now();
        String formattedTime = today.format(DateTimeFormatter.ofPattern("a H:mm"));
        //요청 data를 전달만 ->
        String responseText=question.getContent()+"에 대한 답변내용 입니다.";
        String src="";
        String searchData = question.getContent().trim();
        String searchName = searchData.replace("조회이름:", "").trim();
        System.out.println(question.getContent()+"<< question");
        if (searchData.startsWith("조회이름:")) {
            //DB조회 O
            //1. 이름으로 조회 name
            Optional<MemberEntity> optionalMemberEntity = memberRepository.findByUserName(searchName);
            if (optionalMemberEntity.isEmpty()) {
                responseText = "등록된 회원이 아닙니다.";
            } else {
                responseText = "조회이름: " + optionalMemberEntity.get().getUserName() +"<br>" + "아이디: "+ optionalMemberEntity.get().getUserEmail();
                //2. 이름+고향 출력 name, city
            }
        }else if(searchData.startsWith("회원가입")){
            responseText= "<div>"+"<a href='/member/join'>회원가입링크</a>"+"</div>";
        }
        src="<div class='msg bot flex'>"+
                "<div class='icon'>"+
                "<img src='/images/rabbitchat.png' th:alt=\"#{chat}\"/>"+
                "</div>"+
                "<div class='message'>"+
                "<div class='part'>"+
                "<p>"+responseText+"</p>"+
                "<img src='"+src+"' th:alt=\"#{src}\"/>"+
                "</div>"+
                "<div class='time'>"+
                formattedTime+
                "</div>"+
                "</div>"+
                "</div>";
        simpMessagingTemplate.convertAndSend("/topic/question",Answer.builder().message(src).build());

    }
}
