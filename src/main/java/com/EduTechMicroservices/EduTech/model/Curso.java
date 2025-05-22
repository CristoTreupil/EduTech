package com.EduTechMicroservices.EduTech.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cursos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo, descripcion;
    private int duracionHoras;

    private double precio; // añadido
    private int descuento; // porcentaje entero


    @Transient
    @JsonProperty("precioFinal")
    public double getPrecioFinal(){
        return precio * (1 - descuento / 100.0);
    }

}
