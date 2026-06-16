package org.spring.backendprojectex.shop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.spring.backendprojectex.common.BasicTime;
import org.spring.backendprojectex.member.entity.MemberEntity;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="item_tb06")
public class ItemEntity  extends BasicTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="item_id")
    private Long id;

    @Column(nullable = false,unique = true)
    private String itemTitle;

    @Column(nullable = false)
    private String itemDetail;

    @Column(nullable = false)
    private int itemPrice;

    @Column(nullable = false)
    private int attachFile; //파일 유무(0,1)

    //N:1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private MemberEntity memberEntity;

    //1:N
    @JsonIgnore
    @OneToMany(mappedBy="itemEntity", fetch = FetchType.LAZY,
    cascade = CascadeType.REMOVE, orphanRemoval = true)//게시글이 삭제되면 파일도 삭제
    private List<FileEntity> fileEntities;

    //1:N
    @OneToMany(mappedBy = "itemEntity",fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    private List<ItemListEntity> itemListEntities;
}
