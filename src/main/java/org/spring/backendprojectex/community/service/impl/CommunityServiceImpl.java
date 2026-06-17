package org.spring.backendprojectex.community.service.impl;

import lombok.RequiredArgsConstructor;
import org.spring.backendprojectex.community.dto.CommunityDto;
import org.spring.backendprojectex.community.entity.CommunityEntity;
import org.spring.backendprojectex.community.repository.CommunityRepository;
import org.spring.backendprojectex.community.service.CommunityService;
import org.spring.backendprojectex.member.entity.MemberEntity;
import org.spring.backendprojectex.member.repository.MemberRepository;
import org.spring.backendprojectex.shop.dto.ItemDto;
import org.spring.backendprojectex.shop.entity.FileEntity;
import org.spring.backendprojectex.shop.entity.ItemEntity;
import org.spring.backendprojectex.shop.repository.FileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService {
    private final CommunityRepository communityRepository;
    private final MemberRepository memberRepository;
    private final FileRepository fileRepository;

    @Value("${img.path.community}")
    String communityImgPath;

    @Override
    public Page<CommunityDto> communityListFn(Pageable pageable, String subject, String search) {
        Page<CommunityEntity> communityEntities = null;

        if (subject == null || search == null || search.equals("") || search.length() <= 0) {
            communityEntities = communityRepository.findAllByOrderByIdDesc(pageable);
        } else {
            if (subject.equals("title")) {
                communityEntities = communityRepository.findByTitleContainingOrderByIdDesc(search,pageable);
            } else if (subject.equals("content")) {
                communityEntities = communityRepository.findByContentContainingOrderByIdDesc(search,pageable);
            } else if (subject.equals("nickName")) {
                communityEntities = communityRepository.findByNickNameContainingOrderByIdDesc(search,pageable);
            } else if (subject.equals("category")) {
                communityEntities = communityRepository.findByCategoryContainingOrderByIdDesc(search,pageable);
            } else {
                communityEntities = communityRepository.findAll(pageable);
            }
        }

        return communityEntities.map(communityEntity -> {
            String newFileName = null;
            String oldFileName = null;

            // FileEntities가 비어 있지 않은지 체크
            if (communityEntity.getFileEntities() != null && !communityEntity.getFileEntities().isEmpty()) {
                newFileName = communityEntity.getFileEntities().get(0).getNewFileName();
                oldFileName = communityEntity.getFileEntities().get(0).getOldFileName();
            }

            return CommunityDto.builder()
                    .id(communityEntity.getId())
                    .content(communityEntity.getContent())
                    .title(communityEntity.getTitle())
                    .nickName(communityEntity.getNickName())
                    .count(communityEntity.getCount())
                    .category(communityEntity.getCategory())
                    .memberId(communityEntity.getMemberEntity().getId())
                    .attachFile(communityEntity.getAttachFile())
                    .newFileName(communityEntity.getFileEntities().get(0).getNewFileName())
                    .oldFileName(communityEntity.getFileEntities().get(0).getOldFileName())
                    .createTime(communityEntity.getCreateTime())
                    .updateTime(communityEntity.getUpdateTime())
                    .build();
        });
    }

    @Override
    public void communityInsert(CommunityDto communityDto) throws IOException{
        // 1. 회원이 존재하는지 확인
        memberRepository.findById(communityDto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다"));

        // 2. 파일이 첨부되지 않았다면
        if (communityDto.getCommunityFile()==null||communityDto.getCommunityFile().isEmpty()) {
            // 파일 없이 아이템 저장
            saveCommunityWithoutFile(communityDto);
        } else {
            // 파일이 첨부되었다면
            saveCommunityWithFile(communityDto);
        }
    }

    @Override
    public CommunityDto communityDetailFn(Long id) {
        CommunityEntity communityEntity = communityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id+"에 해당하는 게시글이 존재하지 않습니다"));
        String newFileName = null;
        String oldFileName = null;
        // 첨부 파일이 존재하는지 체크 및 데이터 바인딩
        if (communityEntity.getFileEntities() != null && !communityEntity.getFileEntities().isEmpty()) {
            newFileName = communityEntity.getFileEntities().get(0).getNewFileName();
            oldFileName = communityEntity.getFileEntities().get(0).getOldFileName();
        }
        return CommunityDto.builder()
                .id(communityEntity.getId())
                .content(communityEntity.getContent())
                .title(communityEntity.getTitle())
                .nickName(communityEntity.getNickName())
                .count(communityEntity.getCount())
                .category(communityEntity.getCategory())
                .memberId(communityEntity.getMemberEntity().getId())
                .attachFile(communityEntity.getAttachFile())
                .newFileName(newFileName)
                .oldFileName(oldFileName)
                .createTime(communityEntity.getCreateTime())
                .updateTime(communityEntity.getUpdateTime())
                .build();
    }

    private void saveCommunityWithoutFile(CommunityDto communityDto){
        CommunityEntity communityEntity=CommunityEntity.builder()
                .count(0)
                .title(communityDto.getTitle())
                .content(communityDto.getContent())
                .nickName(communityDto.getNickName())
                .category(communityDto.getCategory())
                .memberEntity(MemberEntity.builder().id(communityDto.getMemberId()).build())
                .attachFile(0)
                .build();

        communityRepository.save(communityEntity);
    }
    private void saveCommunityWithFile(CommunityDto communityDto) throws IOException {
        MultipartFile communityFile=communityDto.getCommunityFile();
        String originalFilename=communityFile.getOriginalFilename();

        String newFileName=generateUniqueFileName(originalFilename);

        String filePath=communityImgPath+"/"+newFileName;
        //디렉토리가 존재하지 않으면 생성
        File fileDir=new File(communityImgPath);
        if(!fileDir.exists()){
            fileDir.mkdirs(); // 데릭토리 생성
        }
        saveFile(communityFile,filePath);
        //파일이 첨부된 커뮤니티 엔티티 생성
        CommunityEntity communityEntity=CommunityEntity.builder()
                .count(0)
                .title(communityDto.getTitle())
                .content(communityDto.getContent())
                .nickName(communityDto.getNickName())
                .category(communityDto.getCategory())
                .memberEntity(MemberEntity.builder().id(communityDto.getMemberId()).build())
                .attachFile(1)
                .build();
        //게시글 저장
        CommunityEntity savedFile=communityRepository.save(communityEntity);

        // 파일 엔티티 생성 후 저장
        FileEntity fileEntity = FileEntity.builder()
                .newFileName(newFileName)
                .oldFileName(originalFilename)
                .communityEntity(savedFile)
                .build();
        fileRepository.save(fileEntity);
    }
    // 고유한 파일 이름 생성
    private String generateUniqueFileName(String originalFilename) {
        UUID uuid = UUID.randomUUID(); // 랜덤 UUID 생성
        return uuid + "_" + originalFilename; // 고유한 파일 이름 반환
    }

    // 파일을 지정된 경로에 저장하는 메소드
    private void saveFile(MultipartFile file, String filePath) throws IOException {
        File destinationFile = new File(filePath.replace("file://", ""));
        file.transferTo(destinationFile);
    }
}
