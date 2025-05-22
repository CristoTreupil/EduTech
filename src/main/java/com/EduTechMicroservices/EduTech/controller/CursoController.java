package com.EduTechMicroservices.EduTech.controller;

import com.EduTechMicroservices.EduTech.model.Curso;
import com.EduTechMicroservices.EduTech.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @GetMapping
    public ResponseEntity<List<Curso>> listarTodos() {
        List<Curso> cursos = cursoService.obtenerTodosLosCursos();
        return ResponseEntity.ok(cursos);
    }


    @PostMapping
    public ResponseEntity<Curso> crear(@RequestBody Curso curso) {
        Curso nuevoCurso = cursoService.guardarCurso(curso);
        return ResponseEntity.ok(nuevoCurso);
    }

    @PutMapping("{id}")
    public ResponseEntity<Curso> actualizar(@PathVariable Long id, @RequestBody Curso cursoDetalles) {
        try {
            Curso curso = cursoService.obtenerCursoPorId(id)
                    .orElseThrow(() -> new Exception("Curso no encontrado"));

            curso.setTitulo(cursoDetalles.getTitulo());
            curso.setDescripcion(cursoDetalles.getDescripcion());
            curso.setDuracionHoras(cursoDetalles.getDuracionHoras());
            curso.setId(cursoDetalles.getId());

            cursoService.guardarCurso(curso);

            return ResponseEntity.ok(curso);
        }catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            cursoService.eliminarCurso(id);
            return ResponseEntity.noContent().build();
        }catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/descuentos")
    public ResponseEntity<List<CursoConDescuentoDto>> conDescuento() {
        List<Curso> list = cursoService.obtenerConDescuento();
        var dto = list.stream().map(c -> new CursoConDescuentoDto(
                c.getId(),
                c.getTitulo(),
                c.getPrecio(),
                c.getDescuento(),
                cursoService.calcularPrecioFinal(c)
        )).collect(Collectors.toList());
        return ResponseEntity.ok(dto);
    }
}
