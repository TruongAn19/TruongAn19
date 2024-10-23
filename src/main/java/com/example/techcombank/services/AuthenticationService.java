package com.example.techcombank.services;


import com.example.techcombank.dto.request.AuthenticationRequest;
import com.example.techcombank.dto.request.IntrospectRequest;
import com.example.techcombank.dto.response.AuthenticationResponse;
import com.example.techcombank.dto.response.IntrospectResponse;
import com.example.techcombank.exception.ApiException;
import com.example.techcombank.exception.ErrorCode;
import com.example.techcombank.models.User;
import com.example.techcombank.repositories.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.StringJoiner;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();

        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());//Xac minh

        SignedJWT signedJWT = SignedJWT.parse(token);

        var verified =signedJWT.verify(verifier);

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        return IntrospectResponse.builder()
                .valid(String.valueOf(verified && expiryTime.after(new Date())))
                .build();
    }

    public AuthenticationResponse authenticated(AuthenticationRequest request) {
        var  user = userRepository.findByUserName(request.getUserName());
        if(user.size() < 1) {
            throw new ApiException(ErrorCode.USER_CANNOT_FIND);
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getFirst().getPassword());

        if(!authenticated)
            throw new ApiException(ErrorCode.INCORRECT_PASSWORD);

        var token= generateToken(user.getFirst());
         return AuthenticationResponse.builder()
                 .token(token)
                 .authenticated(true)
                 .build();
    }

    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUserName())
                .issuer("truongan.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(Duration.ofHours(1)).toEpochMilli()
                ))
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner("");
        if(!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(stringJoiner::add);
        return stringJoiner.toString();
    }
}
