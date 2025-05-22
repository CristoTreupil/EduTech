package com.EduTechMicroservices.EduTech.repository;

import com.EduTechMicroservices.EduTech.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;


@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    @Query(value = """
    SELECT titulo, cantidad_compras FROM (
        SELECT c.titulo, COUNT(p.id) AS cantidad_compras
        FROM pagos p
        JOIN cursos c ON p.curso_id = c.id
        GROUP BY c.titulo
        ORDER BY cantidad_compras DESC
    ) sub
    WHERE cantidad_compras IN (
        SELECT DISTINCT cantidad_compras FROM (
            SELECT COUNT(p.id) AS cantidad_compras
            FROM pagos p
            GROUP BY p.curso_id
            ORDER BY cantidad_compras DESC
            LIMIT 3
        ) top
    )
    ORDER BY cantidad_compras DESC
    """, nativeQuery = true)
    List<CursosMasCompradosDto> findCursosMasComprados();
}
