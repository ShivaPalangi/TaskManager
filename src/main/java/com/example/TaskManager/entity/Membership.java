package com.example.TaskManager.entity;

import com.example.TaskManager.enums.MembershipRoles;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "membership")
@Data
public class Membership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String position;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MembershipRoles role = MembershipRoles.MEMBER;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "team", nullable = false)
    private Team team;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "employee", nullable = false)
    private Employee employee;

    @OneToMany(mappedBy = "responsible", cascade = CascadeType.ALL)
    private List<Task> assignedTasks;

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL)
    private List<Task> createdTasks;
}