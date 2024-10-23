package com.example.techcombank.models;

import jakarta.persistence.*;
import lombok.*;


import java.util.List;
import java.util.Set;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "role")
public class Role {
    @Id
    private String name;
    private String description;

    @ManyToMany
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<User> users;


    @ManyToMany
    private Set<Permission> permissions;

}
