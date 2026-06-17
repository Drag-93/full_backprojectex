package org.spring.backendprojectex.community.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.spring.backendprojectex.common.BasicTime;
import org.spring.backendprojectex.member.entity.MemberEntity;
import org.spring.backendprojectex.shop.entity.FileEntity;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="community_tb06")
public class CommunityEntity extends BasicTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_id")
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private String nickName;
    @Column(nullable = false)
    private String category;
    @Column(nullable = false)
    private int attachFile; //파일 유무(0,1)

    private int count;

    //N:1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private MemberEntity memberEntity;

    //1:N
    @JsonIgnore
    @OneToMany(mappedBy="communityEntity", fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE, orphanRemoval = true)//게시글이 삭제되면 파일도 삭제
    private List<FileEntity> fileEntities;
}
