package com.example.TaskManager.entity;

import com.example.TaskManager.enums.TokenType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Token {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String token;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType = TokenType.BEARER;

    private boolean revoked;
    // برای وقتی که توکن توسط سیستم باطن میشه (لاگ اوت یا توسط ادمین محدود میشه)

    private boolean expired;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;
    // یک کاربر می‌تونه چند توکن فعال داشته باشه (میتونه روی چندین دیوایس لاگین کنه)
}