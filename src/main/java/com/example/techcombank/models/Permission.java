package com.example.techcombank.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "permission")
public class Permission {
    @Id
    private String name;
    private String description;
}
