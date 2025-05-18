package com.EduTechMicroservices.EduTech.service;

import com.EduTechMicroservices.EduTech.model.Curso;
import com.EduTechMicroservices.EduTech.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    public List<Curso> obtenerTodosLosCursos(){
        return cursoRepository.findAll();
    }

    public Optional<Curso> obtenerCursoPorId(Long id){
        return cursoRepository.findById(id);
    }

    public Curso guardarCurso(Curso curso){
        return cursoRepository.save(curso);
    }

    public void eliminarCurso(Long id){
        cursoRepository.deleteById(id);
    }
}
