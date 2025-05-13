package itmo.karenin.lab3;

import itmo.karenin.lab3.config.JwtConfig;
import itmo.karenin.lab3.config.SecurityConfig;
import itmo.karenin.lab3.controller.OwnerController;
import itmo.karenin.lab3.dto.OwnerDTO;
import itmo.karenin.lab3.entity.Owner;
import itmo.karenin.lab3.mapping.OwnerMapper;
import itmo.karenin.lab3.mapping.PetMapper;
import itmo.karenin.lab3.service.OwnerService;
import itmo.karenin.lab3.service.PetService;
import itmo.karenin.lab3.service.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings("removal")
@WebMvcTest(OwnerController.class)
class OwnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OwnerService ownerService;

    @MockBean
    private PetService petService;

    @MockBean
    private OwnerMapper ownerMapper;

    @MockBean
    private PetMapper petMapper;

    @MockBean
    private JwtConfig jwtConfig;

    @Test
    @WithMockUser(roles = "USER")
    void getOwner_ExistingId_ReturnsOk() throws Exception {
        Owner owner = new Owner();
        OwnerDTO dto = new OwnerDTO();
        when(ownerService.getOwnerById(1L)).thenReturn(owner);
        when(ownerMapper.toDto(owner)).thenReturn(dto);

        mockMvc.perform(get("/api/owners/1")
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createOwner_ValidInput_ReturnsCreated() throws Exception {
        OwnerDTO dto = new OwnerDTO();
        Owner owner = new Owner();
        when(ownerMapper.toEntity(any())).thenReturn(owner);
        when(ownerService.createOwner(any())).thenReturn(owner);
        when(ownerMapper.toDto(any())).thenReturn(dto);

        mockMvc.perform(post("/api/owners")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "USER")
    void getAllOwners_ReturnsPage() throws Exception {
        Page<Owner> page = new PageImpl<>(List.of(new Owner()));
        when(ownerService.getAllOwners(any())).thenReturn(page);
        when(ownerMapper.toDto(any())).thenReturn(new OwnerDTO());

        mockMvc.perform(get("/api/owners")
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    void deleteOwner_shouldReturn403_whenNoPermission() throws Exception {
        mockMvc.perform(delete("/owners/1"))
                .andExpect(status().isForbidden());
    }
}