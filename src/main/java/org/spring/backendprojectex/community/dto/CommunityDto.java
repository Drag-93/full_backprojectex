package org.spring.backendprojectex.community.dto;

import jakarta.persistence.Column;
import lombok.*;
import org.spring.backendprojectex.member.entity.MemberEntity;
import org.spring.backendprojectex.shop.entity.FileEntity;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityDto {
    private Long id;

    private String title;

    private String content;

    private String nickName;

    private String category;

    private int count;

    private MemberEntity memberEntity;

    private Long memberId;

    //파일(이미지)============
    @Column(nullable = false)
    private int attachFile; //파일 유무(1,0)

    //파일 업로드 파일을 저장 할 수 있는 객체 ****
    private MultipartFile communityFile; //form에서 파일(이미지)
    private List<FileEntity> fileEntities;
    private String oldFileName; //원래이름
    private String newFileName; //DB저장이름

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
