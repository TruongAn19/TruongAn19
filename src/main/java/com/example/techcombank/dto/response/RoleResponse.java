package com.example.techcombank.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RoleResponse {
    private String name;
    private String description;

    Set<PermissionResponse> permissions;
}
