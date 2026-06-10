package org.spring.backendprojectex.shop.service.impl;

import lombok.RequiredArgsConstructor;
import org.spring.backendprojectex.member.entity.MemberEntity;
import org.spring.backendprojectex.member.repository.MemberRepository;
import org.spring.backendprojectex.shop.dto.ItemDto;
import org.spring.backendprojectex.shop.entity.FileEntity;
import org.spring.backendprojectex.shop.entity.ItemEntity;
import org.spring.backendprojectex.shop.repository.FileRepository;
import org.spring.backendprojectex.shop.repository.ItemRepository;
import org.spring.backendprojectex.shop.service.ItemService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final FileRepository fileRepository; // 파일업로드

    @Value("${img.path.item}")
    String itemImgPath;

    @Override
    public List<ItemDto> itemListFn() {
        return itemRepository.findAll().stream().map(item ->
                        ItemDto.builder()
                                .id(item.getId())
                                .itemTitle(item.getItemTitle())
                                .itemDetail(item.getItemDetail())
                                .itemPrice(item.getItemPrice())
                                .memberEntity(item.getMemberEntity())
                                .memberId(item.getMemberEntity().getId())
                                .attachFile(item.getAttachFile())
                                .newFileName(item.getFileEntities().get(0).getNewFileName())
                                .oldFileName(item.getFileEntities().get(0).getOldFileName())
                                .createTime(item.getCreateTime())
                                .updateTime(item.getUpdateTime())
                                .build())
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto oneItemFn(Long id) {
        ItemEntity itemEntity = itemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id+"에 해당하는 상품이 존재하지 않습니다"));
        String newFileName = null;
        String oldFileName = null;
        // 첨부 파일이 존재하는지 체크 및 데이터 바인딩
        if (itemEntity.getFileEntities() != null && !itemEntity.getFileEntities().isEmpty()) {
            newFileName = itemEntity.getFileEntities().get(0).getNewFileName();
            oldFileName = itemEntity.getFileEntities().get(0).getOldFileName();
        }
        return ItemDto.builder()
                .id(itemEntity.getId())
                .itemTitle(itemEntity.getItemTitle())
                .itemDetail(itemEntity.getItemDetail())
                .itemPrice(itemEntity.getItemPrice())
                .itemSize(0) // 장바구니 수량 제외 (기본값 0 처리)
                .memberId(itemEntity.getMemberEntity().getId())
                .createTime(itemEntity.getCreateTime())
                .updateTime(itemEntity.getUpdateTime())
                .attachFile(itemEntity.getAttachFile())
                .newFileName(newFileName)
                .oldFileName(oldFileName)
                .build();
    }

    @Override
    public Page<ItemDto> pagingSearchItemList(Pageable pageable, String subject, String search) {
        Page<ItemEntity> itemEntities = null;

        if (subject == null || search == null || search.equals("") || search.length() <= 0) {
            itemEntities = itemRepository.findAll(pageable);
        } else {
            if (subject.equals("itemTitle")) {
                itemEntities = itemRepository.findByItemTitleContaining(pageable, search);
            } else if (subject.equals("itemDetail")) {
                itemEntities = itemRepository.findByItemDetailContaining(pageable, search);
            } else if (subject.equals("itemPrice")) {
                itemEntities = itemRepository.findByItemPriceContaining(pageable, search);
            } else {
                itemEntities = itemRepository.findAll(pageable);
            }
        }

        return itemEntities.map(itemEntity -> {
            int itemSize = 0;
            String newFileName = null;
            String oldFileName = null;

            // FileEntities가 비어 있지 않은지 체크
            if (itemEntity.getFileEntities() != null && !itemEntity.getFileEntities().isEmpty()) {
                newFileName = itemEntity.getFileEntities().get(0).getNewFileName();
                oldFileName = itemEntity.getFileEntities().get(0).getOldFileName();
            }

            return ItemDto.builder()
                    .id(itemEntity.getId())
                    .itemTitle(itemEntity.getItemTitle())
                    .itemDetail(itemEntity.getItemDetail())
                    .itemPrice(itemEntity.getItemPrice())
                    .itemSize(itemSize)
                    .memberId(itemEntity.getMemberEntity().getId())
                    .createTime(itemEntity.getCreateTime())
                    .updateTime(itemEntity.getUpdateTime())
                    .attachFile(itemEntity.getAttachFile())
                    .newFileName(newFileName) // 파일이 있을 경우 파일 이름 설정
                    .oldFileName(oldFileName) // 파일이 있을 경우 파일 이름 설정
                    .build();
        });
    }


    @Override
    public void insertItem(ItemDto itemDto) throws IOException {
        // 1. 회원이 존재하는지 확인
        memberRepository.findById(itemDto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다"));

        // 2. 파일이 첨부되지 않았다면
        if (itemDto.getItemFile()==null||itemDto.getItemFile().isEmpty()) {
            // 파일 없이 아이템 저장
            saveItemWithoutFile(itemDto);
        } else {
            // 파일이 첨부되었다면
            saveItemWithFile(itemDto);
        }
    }
    // 파일이 없는 경우, 아이템만 저장
    private void saveItemWithoutFile(ItemDto itemDto) {
        ItemEntity itemEntity = ItemEntity.builder()
                .itemTitle(itemDto.getItemTitle()) // 아이템 제목
                .itemDetail(itemDto.getItemDetail()) // 아이템 상세
                .itemPrice(itemDto.getItemPrice()) // 아이템 가격
                .attachFile(0) // 파일 첨부 안됨
                .memberEntity(MemberEntity.builder()
                        .id(itemDto.getMemberId()) // 회원 ID
                        .build())
                .build();

        itemRepository.save(itemEntity); // 아이템 저장
    }

    // 파일이 있는 경우, 아이템과 파일을 함께 저장
    private void saveItemWithFile(ItemDto itemDto) throws IOException {
        MultipartFile itemFile = itemDto.getItemFile(); // 첨부된 파일
        String originalFilename = itemFile.getOriginalFilename(); // 원본 파일 이름
        // 고유한 파일 이름 생성 (UUID + 원본 파일 이름)
//        UUID uuid = UUID.randomUUID(); // 랜덤 UUID 생성
//        String newFileName1= uuid + "_" + originalFilename;
        String newFileName = generateUniqueFileName(originalFilename);
        // 파일 저장 경로   E:/backend/item
        String filePath = itemImgPath + "/" + newFileName;
        // 디렉토리가 존재하지 않으면 생성
        File fileDir = new File(itemImgPath);
        if (!fileDir.exists()) {
            fileDir.mkdirs(); // 디렉토리 생성
        }
        // 파일을 지정된 경로에 저장
        saveFile(itemFile, filePath);
        // 파일이 첨부된 아이템 엔티티 생성
        ItemEntity itemEntity = ItemEntity.builder()
                .itemTitle(itemDto.getItemTitle()) // 아이템 제목
                .itemDetail(itemDto.getItemDetail()) // 아이템 상세
                .itemPrice(itemDto.getItemPrice()) // 아이템 가격
                .attachFile(1) // 파일이 첨부됨
                .memberEntity(MemberEntity.builder()
                        .id(itemDto.getMemberId()) // 회원 ID
                        .build())
                .build();
        // 아이템 저장
        ItemEntity savedItem = itemRepository.save(itemEntity);
        // 파일 엔티티 생성 후 저장
//        FileEntity fileEntity = FileEntity.toFileEntity(savedItem, originalFilename, newFileName);
        FileEntity fileEntity = FileEntity.builder()
                .newFileName(newFileName)
                .oldFileName(originalFilename)
                .itemEntity(savedItem)
                .build();
        fileRepository.save(fileEntity); // 파일 정보 저장
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


    @Override
    public ItemDto itemDetailFn(Long id) {
        int itemSize = 0;
        String newFileName = null;
        String oldFileName = null;

        ItemEntity itemEntity = itemRepository.findById(id).orElseThrow(()->new IllegalArgumentException(id+"에 해당하는 상품이 없습니다"));
        // FileEntities가 비어 있지 않은지 체크
        if (itemEntity.getFileEntities() != null && !itemEntity.getFileEntities().isEmpty()) {
            newFileName = itemEntity.getFileEntities().get(0).getNewFileName();
            oldFileName = itemEntity.getFileEntities().get(0).getOldFileName();
        }

        return ItemDto.builder()
                .id(itemEntity.getId())
                .itemTitle(itemEntity.getItemTitle())
                .itemDetail(itemEntity.getItemDetail())
                .itemPrice(itemEntity.getItemPrice())
                .itemSize(itemSize)
                .memberId(itemEntity.getMemberEntity().getId())
                .createTime(itemEntity.getCreateTime())
                .updateTime(itemEntity.getUpdateTime())
                .attachFile(itemEntity.getAttachFile())
                .newFileName(newFileName) // 파일이 있을 경우 파일 이름 설정
                .oldFileName(oldFileName) // 파일이 있을 경우 파일 이름 설정
                .build();
    }
}
