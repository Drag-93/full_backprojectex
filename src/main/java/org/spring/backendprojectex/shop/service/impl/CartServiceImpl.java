package org.spring.backendprojectex.shop.service.impl;

import lombok.RequiredArgsConstructor;
import org.spring.backendprojectex.member.entity.MemberEntity;
import org.spring.backendprojectex.member.repository.MemberRepository;
import org.spring.backendprojectex.shop.dto.ItemDto;
import org.spring.backendprojectex.shop.dto.ItemListDto;
import org.spring.backendprojectex.shop.entity.CartEntity;
import org.spring.backendprojectex.shop.entity.ItemEntity;
import org.spring.backendprojectex.shop.entity.ItemListEntity;
import org.spring.backendprojectex.shop.repository.CartRepository;
import org.spring.backendprojectex.shop.repository.ItemListRepository;
import org.spring.backendprojectex.shop.repository.ItemRepository;
import org.spring.backendprojectex.shop.service.CartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final ItemListRepository itemListRepository;

    @Override
    public void insertCart(ItemDto itemDto) {
        //1. 회원 및 상품 확인
        MemberEntity memberEntity=memberRepository.findById(itemDto.getMemberId())
                .orElseThrow(()-> new IllegalArgumentException("회원 정보가 없습니다."));
        ItemEntity itemEntity=itemRepository.findById(itemDto.getId())
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 상품입니다."));
        //2. 장바구니 확인 및 없을 시 생성
        CartEntity cartEntity=cartRepository.findByMemberEntityId(memberEntity.getId())
                .orElseGet(()->cartRepository.save(CartEntity.builder().memberEntity(memberEntity).build()));
        //3. 현재 장바구니에 해당 상품이 이미 있는지 '한 건만' 확인
        List<ItemListEntity> itemListEntities=itemListRepository.findByCartEntityIdAndItemEntityId(cartEntity.getId(),itemEntity.getId());

        if(itemListEntities.isEmpty()){
            //처음 담는 상품인 경우 새로 생성 및 저장
            ItemListEntity itemListEntity=ItemListEntity.builder()
                    .cartEntity(cartEntity)
                    .itemEntity(itemEntity)
                    .itemSize(itemDto.getItemSize()) // 전달받은 수량만큼 설정
                    .build();
            itemListRepository.save(itemListEntity);
        }else {
            //이미 존재하는 경우 수량을 덮어쓰거나(또는 더하거나)선택 가능
            // 여기서는 원본 로직대로 전달받은 값으로 변경하되, 더티 체킹 활용
            ItemListEntity existingItem = itemListEntities.get(0);
            //수량 덮어쓰기
//            existingItem.setItemSize(itemDto.getItemSize());
            //수량 더하기
            int newSize = existingItem.getItemSize()+itemDto.getItemSize();
            existingItem.setItemSize(newSize);
            //따로 save()를 호출하지 않아도 트랜잭션이 끝나면서 자동으로 DB에 update쿼리가 나감
        }

    }

    @Override
    public List<ItemListDto> cartList(Long memberId) {
        MemberEntity memberEntity = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보가 없습니다."));
        Optional<CartEntity> optionalCartEntity = cartRepository.findByMemberEntityId(memberEntity.getId());
        if(optionalCartEntity.isEmpty()){
            return new ArrayList<>();
        }

        List<ItemListEntity> itemListEntities = itemListRepository.findAllByCartEntityId(optionalCartEntity.get().getId());

        return itemListEntities.stream().map(itemList ->
                ItemListDto.builder()
                        .id(itemList.getId())
                        .itemSize(itemList.getItemSize())
                        .cartEntity(itemList.getCartEntity())
                        .itemEntity(itemList.getItemEntity())
                        .createTime(itemList.getCreateTime())
                        .updateTime(itemList.getUpdateTime())
                        .build()
        ).toList();
    }

    @Override
    public int countCartItems(Long memberId) {
        return cartRepository.countByMemberEntity(MemberEntity.builder().id(memberId).build());

    }
}
