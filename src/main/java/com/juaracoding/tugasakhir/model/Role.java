package com.juaracoding.tugasakhir.model;

import com.juaracoding.tugasakhir.enums.RoleType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "MstRole")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDRole")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "RoleType")
    private RoleType roleType;

    @ManyToMany
    @JoinTable(name = "MapRoleMenu",
        uniqueConstraints = {
            @UniqueConstraint(name = "unique-role-menu",
                    columnNames = {"IDRole","IDMenu"})},
        joinColumns = @JoinColumn(name = "IDRole", foreignKey = @ForeignKey(name = "fk-to-map-role")),
        inverseJoinColumns = @JoinColumn(name = "IDMenu", foreignKey = @ForeignKey(name = "fk-to-map-menu"))
    )
    private List<Menu> ltMenu;

    @Column(name = "CreatedBy",updatable = false,nullable = false)
    private String createdBy;
    @Column(name = "CreatedDate",updatable = false,nullable = false)
    private Date createdDate = new Date();
    @Column(name = "UpdatedBy",insertable = false)
    private String updatedBy;
    @Column(name = "UpdatedDate",insertable = false)
    private Date updatedDate;

}
