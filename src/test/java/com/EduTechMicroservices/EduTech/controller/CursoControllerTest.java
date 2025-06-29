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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
        Curso c = new Curso(1L, "Spring Boot", "Básico", 10, 10000.0, 0);
        when(cursoService.obtenerCursoPorId(1L)).thenReturn(Optional.of(c));

        mockMvc.perform(get("/api/cursos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Spring Boot"));
    }

    @Test
    void testGetAllCursos() throws Exception {
        List<Curso> list = List.of(
                new Curso(1L,"C1","D1",5,5000.0,0),
                new Curso(2L,"C2","D2",8,8000.0,10)
        );
        when(cursoService.obtenerTodosLosCursos()).thenReturn(list);

        mockMvc.perform(get("/api/cursos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testGetCursosConDescuento() throws Exception {
        List<Curso> disc = List.of(new Curso(3L,"C3","D3",6,6000.0,20));
        when(cursoService.obtenerConDescuento()).thenReturn(disc);

        mockMvc.perform(get("/api/cursos/descuentos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].descuento").value(20));
    }

    @Test
    void testCrearCurso() throws Exception {
        Curso inp = new Curso(null,"N","Desc",4,4000.0,0);
        Curso out = new Curso(4L,"N","Desc",4,4000.0,0);
        when(cursoService.guardarCurso(any(Curso.class))).thenReturn(out);

        String json = """
        {"titulo":"N","descripcion":"Desc","duracionHoras":4,"precio":4000.0,"descuento":0}
        """;

        mockMvc.perform(post("/api/cursos")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(4));
    }

    @Test
    void testActualizarCurso() throws Exception {
        Curso existing = new Curso(5L,"Old","OD",3,3000.0,0);
        when(cursoService.obtenerCursoPorId(5L)).thenReturn(Optional.of(existing));
        when(cursoService.guardarCurso(any(Curso.class))).thenReturn(existing);

        String json = """
        {"titulo":"New","descripcion":"ND","duracionHoras":3,"precio":3000.0,"descuento":5}
        """;

        mockMvc.perform(put("/api/cursos/5")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("New"))
                .andExpect(jsonPath("$.descuento").value(5));
    }

    @Test
    void testEliminarCurso() throws Exception {
        mockMvc.perform(delete("/api/cursos/6"))
                .andExpect(status().isNoContent());
    }
}
