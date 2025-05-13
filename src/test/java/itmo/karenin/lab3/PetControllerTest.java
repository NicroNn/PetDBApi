package itmo.karenin.lab3;

import itmo.karenin.lab3.config.JwtConfig;
import itmo.karenin.lab3.config.SecurityConfig;
import itmo.karenin.lab3.controller.PetController;
import itmo.karenin.lab3.dto.PetResponseDTO;
import itmo.karenin.lab3.entity.Pet;
import itmo.karenin.lab3.mapping.PetMapper;
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
@WebMvcTest(PetController.class)
class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PetService petService;

    @MockBean
    private PetMapper petMapper;

    @MockBean
    private JwtConfig jwtConfig;

    @Test
    @WithMockUser(roles = "USER")
    void getPet_ExistingId_ReturnsPet() throws Exception {
        Pet pet = new Pet();
        PetResponseDTO dto = new PetResponseDTO();
        when(petService.getPetById(1L)).thenReturn(pet);
        when(petMapper.toResponseDto(pet)).thenReturn(dto);

        mockMvc.perform(get("/api/pets/1")
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createPet_ValidInput_ReturnsCreated() throws Exception {
        Pet pet = new Pet();
        PetResponseDTO dto = new PetResponseDTO();
        when(petMapper.toEntity(any())).thenReturn(pet);
        when(petService.createPet(any())).thenReturn(pet);
        when(petMapper.toResponseDto(pet)).thenReturn(dto);

        mockMvc.perform(post("/api/pets")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Buddy\", \"ownerId\":1}"))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "USER")
    void searchPets_WithParams_ReturnsPage() throws Exception {
        Page<Pet> page = new PageImpl<>(List.of(new Pet()));
        when(petService.searchPets(any(), any(), any(), any())).thenReturn(page);
        when(petMapper.toResponseDto(any())).thenReturn(new PetResponseDTO());

        mockMvc.perform(get("/api/pets/search")
                        .with(csrf()))
                .andExpect(status().isOk());
    }
}