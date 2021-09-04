package org.svazquez.appmockito.ejemplos.daos;

import org.svazquez.appmockito.ejemplos.models.Examen;

import java.util.List;

public interface ExamenRepository {
    Examen guardar(Examen examen);
    List<Examen> findAll();
}
