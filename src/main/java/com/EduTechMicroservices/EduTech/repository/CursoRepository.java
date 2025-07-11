package com.EduTechMicroservices.EduTech.repository;

import com.EduTechMicroservices.EduTech.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
    List<Curso> findBydescuentoGreaterThan(int porcentajeMinimo);
}
