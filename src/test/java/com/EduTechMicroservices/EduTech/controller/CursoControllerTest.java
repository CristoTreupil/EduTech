package com.EduTechMicroservices.EduTech.controller;

import com.EduTechMicroservices.EduTech.model.Curso;
import com.EduTechMicroservices.EduTech.service.CursoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CursoController.class)
public class CursoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CursoService cursoService;

    @Test
    void testGetCursoById() throws Exception {
        // Creamos un curso con todos los parámetros del constructor
        Curso curso = new Curso(1L, "Spring Boot", "Curso básico de Spring Boot", 10, 10000.0, 0);

        // Simulamos la respuesta del servicio con Mockito (ahora con Optional)
        when(cursoService.obtenerCursoPorId(1L)).thenReturn(Optional.of(curso));

        // Ejecutamos la petición simulada y validamos la respuesta
        mockMvc.perform(get("/api/cursos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Spring Boot"))
                .andExpect(jsonPath("$.descripcion").value("Curso básico de Spring Boot"))
                .andExpect(jsonPath("$.duracionHoras").value(10))
                .andExpect(jsonPath("$.precio").value(10000.0))
                .andExpect(jsonPath("$.descuento").value(0));
    }

    @Test
    void testGetAllCursos() throws Exception {
        List<Curso> cursos = List.of(
                new Curso(1L, "Spring Boot", "Básico", 10, 10000.0, 0),
                new Curso(2L, "Java Avanzado", "Avanzado", 20, 15000.0, 5)
        );

        when(cursoService.obtenerTodosLosCursos()).thenReturn(cursos);

        mockMvc.perform(get("/api/cursos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].titulo").value("Spring Boot"))
                .andExpect(jsonPath("$[1].titulo").value("Java Avanzado"));
    }
}
