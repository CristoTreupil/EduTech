package com.EduTechMicroservices.EduTech.controller;

import com.EduTechMicroservices.EduTech.model.Usuario;
import com.EduTechMicroservices.EduTech.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Test
    void testGetAllUsuarios() throws Exception {
        Usuario u1 = new Usuario();
        u1.setId(1L);
        u1.setNombre("A");
        u1.setEmail("a@x.com");
        Usuario u2 = new Usuario();
        u2.setId(2L);
        u2.setNombre("B");
        u2.setEmail("b@x.com");

        when(usuarioService.obtenerTodosLosUsuarios()).thenReturn(List.of(u1, u2));

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testGetUsuarioById() throws Exception {
        Usuario u = new Usuario();
        u.setId(3L);
        u.setNombre("C");
        u.setEmail("c@x.com");
        when(usuarioService.obtenerUsuarioPorId(3L)).thenReturn(Optional.of(u));

        mockMvc.perform(get("/api/usuarios/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("c@x.com"));
    }

    @Test
    void testCrearUsuario() throws Exception {
        Usuario in = new Usuario();
        in.setNombre("D");
        in.setEmail("d@x.com");
        Usuario out = new Usuario();
        out.setId(4L);
        out.setNombre("D");
        out.setEmail("d@x.com");
        when(usuarioService.guardarUsuario(any(Usuario.class))).thenReturn(out);

        String json = """
            {
              "nombre": "D",
              "email": "d@x.com"
            }
            """;

        mockMvc.perform(post("/api/usuarios")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(4));
    }

    @Test
    void testActualizarUsuario() throws Exception {
        Usuario exist = new Usuario();
        exist.setId(5L);
        exist.setNombre("E");
        exist.setEmail("e@x.com");
        when(usuarioService.obtenerUsuarioPorId(5L)).thenReturn(Optional.of(exist));
        when(usuarioService.guardarUsuario(any(Usuario.class))).thenReturn(exist);

        String json = """
            {
              "nombre": "E2",
              "email": "e2@x.com"
            }
            """;

        mockMvc.perform(put("/api/usuarios/5")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("E2"));
    }

    @Test
    void testEliminarUsuario() throws Exception {
        mockMvc.perform(delete("/api/usuarios/6"))
                .andExpect(status().isNoContent());
    }
}
