package com.example.TaskManager.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "company")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    private String description;

    @ManyToOne(optional = false)
    @JoinColumn(name = "owner", nullable = false, updatable = false)
    private User owner;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL,  orphanRemoval = true)
    private List<Team> teams;
}