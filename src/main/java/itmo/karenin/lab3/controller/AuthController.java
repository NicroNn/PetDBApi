package itmo.karenin.lab3.controller;

import itmo.karenin.lab3.config.JwtConfig;
import itmo.karenin.lab3.dto.AuthRequest;
import itmo.karenin.lab3.dto.AuthResponse;
import itmo.karenin.lab3.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        User user = (User) authentication.getPrincipal();
        String token = jwtConfig.generateJwtToken(user);
        return ResponseEntity.ok(
                new AuthResponse(token, user.getUsername(), user.getRole().name())
        );
    }
}
