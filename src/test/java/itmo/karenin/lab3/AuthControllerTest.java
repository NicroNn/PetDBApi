package itmo.karenin.lab3;

import itmo.karenin.lab3.config.JwtConfig;
import itmo.karenin.lab3.config.SecurityConfig;
import itmo.karenin.lab3.controller.AuthController;
import itmo.karenin.lab3.entity.Role;
import itmo.karenin.lab3.entity.User;
import itmo.karenin.lab3.service.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("removal")
@WebMvcTest(AuthController.class)
@Import({SecurityConfig.class, JwtConfig.class})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void login_ValidCredentials_ReturnsToken() throws Exception {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("admin");
        user.setRole(Role.ADMIN);

        when(authenticationManager.authenticate(any()))
                .thenReturn(new UsernamePasswordAuthenticationToken(
                        user, null, user.getAuthorities()
                ));

        mockMvc.perform(
                        post("/api/auth/login") // Исправленный синтаксис запроса
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "username": "admin",
                                            "password": "admin"
                                        }
                                        """)
                )
                .andExpect(status().isOk());
    }
}
