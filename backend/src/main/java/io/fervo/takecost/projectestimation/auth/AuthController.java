package io.fervo.takecost.projectestimation.auth;

import io.fervo.takecost.projectestimation.config.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "Authentication", description = "Endpoints for user authentication")
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Operation(summary = "Login", description = "Authenticate user and return a JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "JWT token generated successfully", content = @Content),
            @ApiResponse(responseCode = "401", description = "Invalid username or password", content = @Content),
    })
    @PostMapping("/login")
    public Map<String, String> login(@Valid @RequestBody LoginRequest credentials) {
        log.info("Logging in user: {}", credentials.username());

        try {
            var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(credentials.username(), credentials.password())
            );
            log.debug("Authentication result: {}", authentication);

            var user = (User) authentication.getPrincipal();
            var token = jwtUtils.generateToken(user);

            var response = new HashMap<String, String>();
            response.put("token", token);
            return response;
        } catch (BadCredentialsException e) {
            log.warn("Invalid credentials for user: {}", credentials.username());
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Invalid username or password"
            );
        }
    }
}
