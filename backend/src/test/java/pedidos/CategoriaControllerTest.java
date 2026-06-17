package pedidos;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pedidos.dtos.categoria.CategoriaCreate;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listarCategorias_deberiaRetornar200() throws Exception {
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void crearCategoria_conDatosValidos_deberiaRetornar201() throws Exception {
        CategoriaCreate dto = new CategoriaCreate();
        dto.setNombre("Hamburguesas");
        dto.setDescripcion("Las mejores hamburguesas");

        mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Hamburguesas"))
                .andExpect(jsonPath("$.descripcion").value("Las mejores hamburguesas"))
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void buscarCategoriaPorId_cuandoExiste_deberiaRetornar200() throws Exception {
        CategoriaCreate dto = new CategoriaCreate();
        dto.setNombre("Pizzas");
        dto.setDescripcion("Las mejores pizzas");

        String response = mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(get("/api/categories/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Pizzas"));
    }

    @Test
    void buscarCategoriaPorId_cuandoNoExiste_deberiaRetornar404() throws Exception {
        mockMvc.perform(get("/api/categories/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void actualizarCategoria_conDatosValidos_deberiaRetornar200() throws Exception {
        CategoriaCreate createDto = new CategoriaCreate();
        createDto.setNombre("Nombre original");
        createDto.setDescripcion("Descripcion original");

        String response = mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        CategoriaCreate editDto = new CategoriaCreate();
        editDto.setNombre("Nombre actualizado");
        editDto.setDescripcion("Descripcion actualizada");

        mockMvc.perform(put("/api/categories/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(editDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Nombre actualizado"));
    }

    @Test
    void eliminarCategoria_cuandoExiste_deberiaRetornar204() throws Exception {
        CategoriaCreate dto = new CategoriaCreate();
        dto.setNombre("A eliminar");
        dto.setDescripcion("Esta categoria se va a eliminar");

        String response = mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(delete("/api/categories/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/categories/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void eliminarCategoria_cuandoNoExiste_deberiaRetornar404() throws Exception {
        mockMvc.perform(delete("/api/categories/999"))
                .andExpect(status().isNotFound());
    }
}