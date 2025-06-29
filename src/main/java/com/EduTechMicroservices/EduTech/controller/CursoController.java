package com.EduTechMicroservices.EduTech.controller;

import com.EduTechMicroservices.EduTech.model.Curso;
import com.EduTechMicroservices.EduTech.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @GetMapping
    public CollectionModel<EntityModel<Curso>> listarTodos() {
        List<EntityModel<Curso>> cursos = cursoService.obtenerTodosLosCursos().stream()
                .map(curso -> EntityModel.of(curso,
                        linkTo(methodOn(CursoController.class).obtenerPorId(curso.getId())).withSelfRel(),
                        linkTo(methodOn(CursoController.class).listarTodos()).withRel("cursos")))
                .collect(Collectors.toList());

        return CollectionModel.of(cursos,
                linkTo(methodOn(CursoController.class).listarTodos()).withSelfRel());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Curso>> obtenerPorId(@PathVariable Long id) {
        return cursoService.obtenerCursoPorId(id)
                .map(curso -> {
                    EntityModel<Curso> resource = EntityModel.of(curso,
                            linkTo(methodOn(CursoController.class).obtenerPorId(id)).withSelfRel(),
                            linkTo(methodOn(CursoController.class).listarTodos()).withRel("cursos"));
                    return ResponseEntity.ok(resource);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EntityModel<Curso>> crear(@RequestBody Curso curso) {
        Curso nuevoCurso = cursoService.guardarCurso(curso);

        EntityModel<Curso> resource = EntityModel.of(nuevoCurso,
                linkTo(methodOn(CursoController.class).obtenerPorId(nuevoCurso.getId())).withSelfRel(),
                linkTo(methodOn(CursoController.class).listarTodos()).withRel("cursos"));

        return ResponseEntity
                .created(resource.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(resource);
    }

    @PutMapping("{id}")
    public ResponseEntity<EntityModel<Curso>> actualizar(@PathVariable Long id, @RequestBody Curso cursoDetalles) {
        try {
            Curso curso = cursoService.obtenerCursoPorId(id)
                    .orElseThrow(() -> new Exception("Curso no encontrado"));

            curso.setTitulo(cursoDetalles.getTitulo());
            curso.setDescripcion(cursoDetalles.getDescripcion());
            curso.setDuracionHoras(cursoDetalles.getDuracionHoras());
            curso.setPrecio(cursoDetalles.getPrecio());
            curso.setDescuento(cursoDetalles.getDescuento());

            Curso actualizado = cursoService.guardarCurso(curso);

            EntityModel<Curso> resource = EntityModel.of(actualizado,
                    linkTo(methodOn(CursoController.class).obtenerPorId(actualizado.getId())).withSelfRel(),
                    linkTo(methodOn(CursoController.class).listarTodos()).withRel("cursos"));

            return ResponseEntity.ok(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            cursoService.eliminarCurso(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/descuentos")
    public CollectionModel<EntityModel<CursoConDescuentoDto>> conDescuento() {
        List<EntityModel<CursoConDescuentoDto>> descuentos = cursoService.obtenerConDescuento().stream()
                .map(c -> {
                    CursoConDescuentoDto dto = new CursoConDescuentoDto(
                            c.getId(),
                            c.getTitulo(),
                            c.getPrecio(),
                            c.getDescuento(),
                            cursoService.calcularPrecioFinal(c));
                    return EntityModel.of(dto,
                            linkTo(methodOn(CursoController.class).obtenerPorId(c.getId())).withRel("curso"),
                            linkTo(methodOn(CursoController.class).conDescuento()).withSelfRel());
                })
                .collect(Collectors.toList());

        return CollectionModel.of(descuentos,
                linkTo(methodOn(CursoController.class).conDescuento()).withSelfRel());
    }
}
