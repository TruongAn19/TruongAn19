package com.example.techcombank.controller;

import com.example.techcombank.dto.request.PermissionRequest;
import com.example.techcombank.dto.response.PermissionResponse;
import com.example.techcombank.models.ResponseObject;
import com.example.techcombank.repositories.PermissionRepository;
import com.example.techcombank.services.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/Permission")
public class PermissionController {

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private PermissionService  permissionService;

    @GetMapping(path = "/allPermission")
    ResponseEntity<ResponseObject> getAllPermission() {
        List<PermissionResponse> allPermission = permissionService.getAll();
        return new ResponseEntity(new ResponseObject("", "", allPermission), HttpStatus.OK);
    }

    @PostMapping(path = "/createPermission")
    ResponseEntity<PermissionResponse> createPermission(@RequestBody PermissionRequest request) {
        var result = permissionService.createPermission(request);
        return new ResponseEntity(new ResponseObject("", "", result), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{permission}")
    ResponseEntity<ResponseObject> deletePermission(@PathVariable String permission) {
       return new ResponseEntity(new ResponseObject("", " ", permissionService.getAll()), HttpStatus.OK);
    }
}
