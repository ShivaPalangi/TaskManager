package com.example.TaskManager.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "team")
@Data
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    private String description;

    @ManyToOne(optional = false)
    @JoinColumn(name = "company", nullable = false)
    private Company company;

    @OneToMany(mappedBy = "team")
    private List<Membership> memberships;
}