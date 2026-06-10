package org.spring.backendprojectex.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.spring.backendprojectex.common.Role;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDto {
    private  Long id;

    @NotBlank(message = "이메일를 입력하세요.")
    private String userEmail;

    @NotBlank(message = "비빌번호를 입력하세요.")
    private String userPw;

    @NotBlank(message = "이름을 입력하세요.")
    private String userName;

    private Role role;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}

