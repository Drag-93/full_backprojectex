package org.spring.backendprojectex.shop.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.spring.backendprojectex.member.entity.MemberEntity;
import org.spring.backendprojectex.shop.entity.FileEntity;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDto {
    private Long id;
    @NotBlank(message = "상품명을 입력하세요")
    private String itemTitle;
    @NotBlank(message = "상품 상세내용을 입력하세요")
    private String itemDetail;
    @NotNull(message = "상품 가격을 입력하세요")
    private int itemPrice;

    //파일(이미지)============
    @Column(nullable = false)
    private int attachFile; //파일 유무(1,0)

    //파일 업로드 파일을 저장 할 수 있는 객체 ****
    private MultipartFile itemFile; //form에서 파일(이미지)
    private List<FileEntity> fileEntities;
    private String oldFileName; //원래이름
    private String newFileName; //DB저장이름
    //View -> thymeleaf, react
    private Long memberId;
    private int itemSize;  // -> ItemList itemSize
    private MemberEntity memberEntity;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
