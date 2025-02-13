package com.juaracoding.tugasakhir.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.*;

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
    @Column (name = "LastName", length = 50, nullable = false)
    private String lastName;
    @Column (name = "Email", length = 60, nullable = false, unique = true)
    private String email;
    @Column(name = "NoHp", length = 16, nullable = false, unique = true)
    private String noHp;
    @Column (name = "Address", length = 255, nullable = false)
    private String address;

    /** ubah saat migrasi DB */
    @Column(name = "IsRegistered",columnDefinition = ("bit default 0"))
    private Boolean isRegistered=false;
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        List<Menu> lt = this.role.getLtMenu();
//        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
//        for (Menu menu :
//                lt) {
//            grantedAuthorities.add(new SimpleGrantedAuthority(menu.getName()));
//        }
//        return grantedAuthorities;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return UserDetails.super.isAccountNonExpired();
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return UserDetails.super.isAccountNonLocked();
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return UserDetails.super.isCredentialsNonExpired();
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return UserDetails.super.isEnabled();
//    }

    @Column(name = "TanggalLahir")
    private LocalDate tanggalLahir;
    @Transient
    private Integer umur;
//    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
//    private List<Booking> bookingsList = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "RoleIdRole", foreignKey = @ForeignKey(name = "fk-user-to-role"))
    private Role role;

    @Column(name = "OTP",length = 60)
    private String otp;

    @Column(name = "CreatedBy",updatable = false,nullable = false)
    private String createdBy;
    @Column(name = "CreatedDate",updatable = false,nullable = false)
    private Date createdDate = new Date();
    @Column(name = "UpdatedBy",insertable = false)
    private String updatedBy;
    @Column(name = "UpdatedDate",insertable = false)
    private Date updatedDate;

}
