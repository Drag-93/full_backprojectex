package org.spring.backendprojectex.community;

import org.junit.jupiter.api.Test;
import org.spring.backendprojectex.community.dto.CommunityDto;
import org.spring.backendprojectex.community.service.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CommunityTest {

    @Autowired
    CommunityService communityService;

    @Test
    void insert(){
        for(int i=1;i<=10;i++){
            communityService.communityInsert(CommunityDto.builder()
                            .count(0)
                            .title("제목"+i)
                            .content("내용"+i)
                            .nickName("닉네임"+i)
                            .category("공지사항")
                            .memberId(1L)
                    .build());
        }
    }
}
