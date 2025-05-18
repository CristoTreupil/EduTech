package com.EduTechMicroservices.EduTech.repository;

import com.EduTechMicroservices.EduTech.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
}
