package com.example.techcombank.controller;


import com.example.techcombank.dto.request.RoleRequest;
import com.example.techcombank.dto.response.RoleResponse;
import com.example.techcombank.models.ResponseObject;
import com.example.techcombank.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/Role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping(path = "/getAllRole")
    ResponseEntity<ResponseObject> getAllRole() {
        List<RoleResponse> allRole = roleService.getAllRole();
        return new ResponseEntity(new ResponseObject("", "", allRole), HttpStatus.OK);
    }

    @PostMapping(path = "/createRole")
    ResponseEntity<RoleResponse> createRole(@RequestBody RoleRequest request) {
        var result = roleService.createRole(request);
        return new ResponseEntity(new ResponseObject("", "", result), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{role}")
    ResponseEntity<ResponseObject> deleteRole(@PathVariable String role) {
        return new ResponseEntity(new ResponseObject("", "", roleService.getAllRole()), HttpStatus.OK);
    }
}
