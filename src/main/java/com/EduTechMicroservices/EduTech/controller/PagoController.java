package com.EduTechMicroservices.EduTech.controller;


import com.EduTechMicroservices.EduTech.model.Pago;
import com.EduTechMicroservices.EduTech.service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @GetMapping
    public ResponseEntity<List<Pago>> obtenerTodosLosPagos() {
        List<Pago> pagos = pagoService.obtenerTodosLosPagos();
        return ResponseEntity.ok(pagos);
    }

    @GetMapping("{id}")
    public ResponseEntity<Pago> buscaPagoPorId(@PathVariable Long id) {
        try {
            Pago pago = pagoService.obtenerPagoPorId(id)
                    .orElseThrow(() -> new Exception("Pago no encontrado"));
            return ResponseEntity.ok(pago);
        }catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Pago> crearPago(@RequestBody Pago pago) {
        Pago nuevoPago = pagoService.guardarPago(pago);
        return ResponseEntity.ok(nuevoPago);
    }

    @PutMapping("{id}")
    public ResponseEntity<Pago> actualizarPago(@PathVariable Long id, @RequestBody Pago pagoDetalles) {
        try {
            Pago pago = pagoService.obtenerPagoPorId(id)
                    .orElseThrow(() -> new Exception("Pago no encontrado"));

            pago.setMonto(pagoDetalles.getMonto());
            pago.setFechaPago(pagoDetalles.getFechaPago());
            pago.setCursoId(pagoDetalles.getCursoId());
            pago.setUsuarioId(pagoDetalles.getUsuarioId());
            pago.setId(pagoDetalles.getId());

            pagoService.guardarPago(pago);
            return ResponseEntity.ok(pago);
        }catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> eliminarPago(@PathVariable Long id) {
        try {
            pagoService.eliminarPago(id);
            return ResponseEntity.noContent().build();
        }catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}
