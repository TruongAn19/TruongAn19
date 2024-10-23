package com.example.techcombank.mapper;


import com.example.techcombank.dto.request.RoleRequest;
import com.example.techcombank.dto.response.RoleResponse;
import com.example.techcombank.models.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);
    RoleResponse toRoleResponse(Role role);
}
