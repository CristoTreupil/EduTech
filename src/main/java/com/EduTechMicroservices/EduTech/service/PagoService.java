package com.EduTechMicroservices.EduTech.service;

import com.EduTechMicroservices.EduTech.model.Pago;
import com.EduTechMicroservices.EduTech.repository.CursosMasCompradosDto;
import com.EduTechMicroservices.EduTech.repository.PagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    public List<Pago> obtenerTodosLosPagos() {
        return pagoRepository.findAll();
    }

    public Optional<Pago> obtenerPagoPorId(Long id) {
        return pagoRepository.findById(id);
    }

    public Pago guardarPago(Pago pago) {
        return pagoRepository.save(pago);
    }

    public void eliminarPago(Long id) {
        pagoRepository.deleteById(id);
    }

    public List<CursosMasCompradosDto> obtenerCursosMasComprados(){
        return pagoRepository.findCursosMasComprados();
    }
}
