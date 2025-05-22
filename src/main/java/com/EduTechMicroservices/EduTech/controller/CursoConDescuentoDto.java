package com.EduTechMicroservices.EduTech.controller;

public record CursoConDescuentoDto(Long id,
                                   String titulo,
                                   double precioOriginal,
                                   int descuento,
                                   double precioFinal) {
}
