package org.svazquez.appmockito.ejemplos.services;

import org.svazquez.appmockito.ejemplos.daos.ExamenRepository;
import org.svazquez.appmockito.ejemplos.daos.PreguntaRepository;
import org.svazquez.appmockito.ejemplos.models.Examen;

import java.util.List;
import java.util.Optional;

public class ExamenServiceImpl implements ExamenService{

    // esto puede ser JPA, Hibernate, JDBC, un cliente HTTP REST Template
    private ExamenRepository examenRepository;

    // esta es otra dependencia externa
    private PreguntaRepository preguntaRepository;

    //lo que si nos interesa es testear ExamenServiceImpl

    public ExamenServiceImpl(ExamenRepository examenRepository, PreguntaRepository preguntaRepository) {
        this.examenRepository = examenRepository;
        this.preguntaRepository = preguntaRepository;
    }

    @Override
    public Optional<Examen> findExamenPorNombre(String nombre) {
        return examenRepository.findAll().stream().filter(f -> f.getNombre().contains(nombre)).findFirst();
    }

    @Override
    public Examen findExamenPorNombreConPreguntas(String nombre) {
        Optional<Examen> examenOptional = findExamenPorNombre(nombre);
        Examen examen = null;
        if(examenOptional.isPresent()) {
            examen = examenOptional.orElseThrow();
            List<String> preguntas = preguntaRepository.findPreguntasPorExamenId(examen.getId());
            examen.setPreguntas(preguntas);
        }
        return examen;
    }

    @Override
    public Examen guardar(Examen examen) {
        if(!examen.getPreguntas().isEmpty()) {
            preguntaRepository.guardarVarias(examen.getPreguntas());
        }
        return examenRepository.guardar(examen);
    }


}
