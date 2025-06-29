package com.EduTechMicroservices.EduTech.controller;

import com.EduTechMicroservices.EduTech.model.Pago;
import com.EduTechMicroservices.EduTech.repository.CursosMasCompradosDto;
import com.EduTechMicroservices.EduTech.service.PagoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
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

@WebMvcTest(PagoController.class)
public class PagoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PagoService pagoService;

    @Test
    void testGetAllPagos() throws Exception {
        List<Pago> pagos = List.of(
                new Pago(1L, 1L, 1L, 100.0, LocalDate.now()),
                new Pago(2L, 2L, 2L, 200.0, LocalDate.now())
        );
        when(pagoService.obtenerTodosLosPagos()).thenReturn(pagos);

        mockMvc.perform(get("/api/pagos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testGetPagoById() throws Exception {
        Pago p = new Pago(3L, 3L, 3L, 300.0, LocalDate.now());
        when(pagoService.obtenerPagoPorId(3L)).thenReturn(Optional.of(p));

        mockMvc.perform(get("/api/pagos/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.monto").value(300.0));
    }

    @Test
    void testCrearPago() throws Exception {
        Pago in = new Pago(null, 4L, 4L, 150.0, LocalDate.now());
        Pago out = new Pago(4L, 4L, 4L, 150.0, LocalDate.now());
        when(pagoService.guardarPago(any(Pago.class))).thenReturn(out);

        String json = """
            {
              "usuarioId": 4,
              "cursoId": 4,
              "monto": 150.0,
              "fechaPago": "%s"
            }
            """.formatted(LocalDate.now());

        mockMvc.perform(post("/api/pagos")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(4));
    }

    @Test
    void testActualizarPago() throws Exception {
        Pago exist = new Pago(5L, 5L, 5L, 500.0, LocalDate.now());
        when(pagoService.obtenerPagoPorId(5L)).thenReturn(Optional.of(exist));
        when(pagoService.guardarPago(any(Pago.class))).thenReturn(exist);

        String json = """
            {
              "usuarioId": 5,
              "cursoId": 5,
              "monto": 550.0,
              "fechaPago": "%s"
            }
            """.formatted(LocalDate.now());

        mockMvc.perform(put("/api/pagos/5")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.monto").value(550.0));
    }

    @Test
    void testEliminarPago() throws Exception {
        mockMvc.perform(delete("/api/pagos/6"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetCursosMasComprados() throws Exception {
        CursosMasCompradosDto dto = new CursosMasCompradosDto() {
            @Override public String getTitulo() { return "C1"; }
            @Override public Long getCantidadCompras() { return 9L; }
        };
        when(pagoService.obtenerCursosMasComprados()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/pagos/cursos-mas-comprados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("C1"))
                .andExpect(jsonPath("$[0].cantidadCompras").value(9));
    }
}
