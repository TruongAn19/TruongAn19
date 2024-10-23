package com.example.techcombank.services;

import com.example.techcombank.dto.request.RoleRequest;
import com.example.techcombank.dto.response.RoleResponse;
import com.example.techcombank.mapper.RoleMapper;
import com.example.techcombank.models.Role;
import com.example.techcombank.repositories.PermissionRepository;
import com.example.techcombank.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PermissionRepository permissionRepository;

    public Role createRole(RoleRequest request) {
        var role = roleMapper.toRole(request);

        var permissions = permissionRepository.findAllById(request.getPermissions());
        System.out.println("Permissions retrieved: " + permissions);
        role.setPermissions(new HashSet<>(permissions));

        System.out.println("Permissions retrieved: " + roleRepository.save(role));
        return roleRepository.save(role);
    }

    public List<RoleResponse> getAllRole() {
        var roles = roleRepository.findAll();
        System.out.println("ROles: " + roles);
        return roles.stream().map(roleMapper::toRoleResponse).toList();
    }

    public void deleteRole(String role) {
        roleRepository.deleteById(role);
    }
}
