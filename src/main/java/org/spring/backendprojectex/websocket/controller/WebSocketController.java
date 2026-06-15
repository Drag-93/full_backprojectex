package org.spring.backendprojectex.websocket.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.spring.backendprojectex.member.entity.MemberEntity;
import org.spring.backendprojectex.member.repository.MemberRepository;
import org.spring.backendprojectex.websocket.dto.BotMessage;
import org.spring.backendprojectex.websocket.dto.ClientMessage;
import org.spring.backendprojectex.websocket.rabbit.Question;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Log4j2
@Controller
@RequiredArgsConstructor
public class WebSocketController {
    private final MemberRepository memberRepository;
    //기본 실행
    @MessageMapping("/hellow")  // /app/hellow
    @SendTo("/topic/greetings") // 응답
    public BotMessage greeting(ClientMessage message) throws Exception{        //처음 접속 됐을 때 -> // 서버에서 -> 클라이언트
        //처음 접속 됐을 때 -> // 서버에서 -> 클라이언트
        Thread.sleep(50);
        LocalDateTime today = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
        String formattedDay = today.format(formatter);
        String formattedTime = today.format(DateTimeFormatter.ofPattern("a H:mm"));

        String firstMessage = "<div class='msg'>" +
                "<div class = 'content'>" + message.getContent() + "</div>" +
                "<div class='head-img'><img src='/images/chat.png'></div>" +
                "<div class='message'>안녕하세요, 챗봇(WebSocket)입니다.<br>" +
                "궁금한 점은 저에게 물어보세요</div>" +
                "<div class='time'>" + formattedDay + " " + formattedTime + "</div>" +
                "</div>";
        return new BotMessage(firstMessage);
    }

    @MessageMapping("/message")
    @SendTo("/topic/message")
    public BotMessage message(ClientMessage message) throws Exception{
        Thread.sleep(50);
        LocalDateTime today = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
        String formattedDay = today.format(formatter);
        String formattedTime = today.format(DateTimeFormatter.ofPattern("a H:mm"));
        String searchData = message.getContent().trim();
        String searchName = searchData.replace("조회이름:", "").trim();
        String responseText = message.getContent() + "에 대한 답변내용 입니다.";
        String answerText = "답변 내용이 없습니다.";
        //조회이름:이름 -> 이름
        System.out.println("조회이름: " + searchName);
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
            responseText= "<div><a href='/member/join'>회원가입링크</a></div>";
        }
        //DB조회 X
        String firstMessage = "<div class='msg'>" +
                "<div class='head-img'><img src='/images/chat.png'></div>" +
                "<div class='message'>" + responseText + "</div>" +
                "<div class='time'>" + formattedDay + " " + formattedTime + "</div>" +
                "</div>";
        return new BotMessage(firstMessage);


    }

    //RabbitMQ 메시지 리스너(받은 메시지 처리)
    //RabbitMQ -> question
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.question.routing.key}")
    private String routingKey;

    @MessageMapping("/bot")
    public void chatbot(Question message) throws Exception{
        rabbitTemplate.convertAndSend(exchange,routingKey,message);
    }

}
