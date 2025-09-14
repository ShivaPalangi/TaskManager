package com.example.TaskManager.entity;

import com.example.TaskManager.enums.MembershipRoles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "membership")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Membership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MembershipRoles role = MembershipRoles.MEMBER;

    @ManyToOne(optional = false)
    @JoinColumn(name = "team", nullable = false)
    private Team team;

    @ManyToOne(optional = false)
    @JoinColumn(name = "employee", nullable = false)
    private User employee;

    @OneToMany(mappedBy = "responsible")
    private List<Task> assignedTasks;

    @OneToMany(mappedBy = "createdBy")
    private List<Task> createdTasks;
}