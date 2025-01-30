package com.juaracoding.tugasakhir.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "MstUser")
public class User {
    @Id
    @Column(name = "IDUser")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", length = 50, nullable = false, unique = true)
    private String username;
    @Column(name = "Password", length = 60, nullable = false, unique = true)
    private String password;
    @Column (name ="FirstName", length = 50, nullable = false)
    private String firstName;
    @Column (name = "LastName", length = 50)
    private String lastName;
    @Column (name = "Email", length = 60, nullable = false, unique = true)
    private String email;
    @Column(name = "NoHp", length = 16, nullable = false, unique = true)
    private String noHp;
    @Column (name = "Address", length = 255, nullable = false)
    private String address;

    @ManyToOne
    @JoinColumn(name = "role_id_role", foreignKey = @ForeignKey(name = "fk-user-to-role"))
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Booking> bookingsList = new ArrayList<>();

    @Column(name = "CreatedBy",updatable = false,nullable = false)
    private String createdBy;
    @Column(name = "CreatedDate",updatable = false,nullable = false)
    private Date createdDate = new Date();

    @Column(name = "UpdatedBy",insertable = false)
    private String updatedBy;
    @Column(name = "UpdatedDate",insertable = false)
    private Date updatedDate;
}
