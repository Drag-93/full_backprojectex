package org.spring.backendprojectex.websocket.rabbit;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Answer {
    //응답
    private String answer;
    private String message;

}
