package com.example.techcombank.mapper;

import com.example.techcombank.dto.request.PermissionRequest;
import com.example.techcombank.dto.response.PermissionResponse;
import com.example.techcombank.models.Permission;
import com.example.techcombank.repositories.PermissionRepository;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
        Permission toPermission(PermissionRequest request);
        PermissionResponse toPermissionResponse(Permission permission);
}
