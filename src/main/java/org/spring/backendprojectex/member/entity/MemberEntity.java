package org.spring.backendprojectex.member.entity;

import jakarta.persistence.*;
import lombok.*;
import org.spring.backendprojectex.common.BasicTime;
import org.spring.backendprojectex.common.Role;
import org.spring.backendprojectex.shop.entity.ItemEntity;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="member_tb06")
public class MemberEntity extends BasicTime {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="member_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String userEmail;

    @Column(nullable = false)
    private String userPw;

    @Column(nullable = false)
    private String userName;

    @Enumerated(EnumType.STRING)
    private Role role;

    //1:N -> 관리자만 상품 등록
    @OneToMany(mappedBy = "memberEntity",fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ItemEntity> itemEntities;

}
