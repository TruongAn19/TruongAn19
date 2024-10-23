package com.example.techcombank.controller;

import com.example.techcombank.dto.request.AuthenticationRequest;
import com.example.techcombank.dto.request.IntrospectRequest;
import com.example.techcombank.dto.response.IntrospectResponse;
import com.example.techcombank.models.ResponseObject;
import com.example.techcombank.repositories.UserRepository;
import com.example.techcombank.services.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping(path = "/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping(path = "/log-in")
    ResponseEntity<ResponseObject> authenticate(@RequestBody AuthenticationRequest request) {
        var result = authenticationService.authenticated(request);
        return new ResponseEntity<>(new ResponseObject("Oke", "Login success", result), HttpStatus.OK);
    }

    @PostMapping(path = "/introspect")
    ResponseEntity<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);
        return new ResponseEntity(new ResponseObject("Oke", "Get token success", result), HttpStatus.OK);
    }
}
