package io.fervo.takecost.projectestimation.controller;

import io.fervo.takecost.projectestimation.dto.LoginRequest;
import io.fervo.takecost.projectestimation.security.JwtUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public Map<String, String> login(@Valid @RequestBody LoginRequest credentials) {
        log.info("Logging in user: {}", credentials.username());

        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(credentials.username(), credentials.password())
        );
        log.debug("Authentication result: {}", authentication);

        var user = (User) authentication.getPrincipal();
        var token = jwtUtils.generateToken(user);

        var response = new HashMap<String, String>();
        response.put("token", token);
        return response;
    }
}
