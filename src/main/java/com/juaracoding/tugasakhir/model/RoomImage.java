//package com.juaracoding.tugasakhir.model;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//public class RoomImage {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "IDImage")
//    private Long id;
//
//    @Column(name = "LinkImage")
//    private String linkImage;
//
//    @ManyToOne
//    @JoinColumn(name = "IDRoom", nullable = false)
//    private Room room;
//}
