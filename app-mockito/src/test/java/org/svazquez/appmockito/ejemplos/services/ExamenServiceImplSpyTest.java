package org.svazquez.appmockito.ejemplos.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.stubbing.Answer;
import org.svazquez.appmockito.ejemplos.Datos;
import org.svazquez.appmockito.ejemplos.daos.ExamenRepository;
import org.svazquez.appmockito.ejemplos.daos.ExamenRepositoryImpl;
import org.svazquez.appmockito.ejemplos.daos.PreguntaRepository;
import org.svazquez.appmockito.ejemplos.daos.PreguntaRepositoryImpl;
import org.svazquez.appmockito.ejemplos.models.Examen;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExamenServiceImplSpyTest {

    @Spy
    ExamenRepositoryImpl repository;

    @Spy
    PreguntaRepositoryImpl preguntaRepository;

    @InjectMocks
    ExamenServiceImpl service;

    @BeforeEach
    void setUp() {
        // habilitar anotaciones
        MockitoAnnotations.openMocks(this);

//        repository = mock(ExamenRepositoryImpl.class);
//        preguntaRepository = mock(PreguntaRepositoryImpl.class);
//        service = new ExamenServiceImpl(repository,preguntaRepository);
    }

    @Test
    void testSpy() {
        List<String> preguntas = Arrays.asList("aritmetica");

//        when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(preguntas);
        doReturn(preguntas).when(preguntaRepository).findPreguntasPorExamenId(anyLong());

        // llamando al metodo real
        Examen examen = service.findExamenPorNombreConPreguntas("Matematicas");
        assertEquals(5, examen.getId());
        assertEquals("Matematicas",examen.getNombre());
        assertEquals(1,examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("aritmetica"));

        verify(repository).findAll();
        verify(preguntaRepository).findPreguntasPorExamenId(anyLong());
    }
}