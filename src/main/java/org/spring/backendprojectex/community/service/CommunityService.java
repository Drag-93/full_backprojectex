package org.spring.backendprojectex.community.service;

import jakarta.validation.Valid;
import org.spring.backendprojectex.community.dto.CommunityDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface CommunityService {


    Page<CommunityDto> communityListFn(Pageable pageable, String subject, String search);

    void communityInsert(@Valid CommunityDto communityDto) throws IOException;

    CommunityDto communityDetailFn(Long id);
}
