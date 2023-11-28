package com.example.author.persistence.domain;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String isbn; //(International Standard Book Number) là mã số tiêu chuẩn quốc tế để xác định một quyển sách.
    private String title;

    @ManyToOne
    @JoinColumn(name="author_id")
    private Author author;
}