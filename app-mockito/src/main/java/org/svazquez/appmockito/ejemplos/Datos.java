package org.svazquez.appmockito.ejemplos;

import org.svazquez.appmockito.ejemplos.models.Examen;

import java.util.Arrays;
import java.util.List;

public class Datos {
    public final static List<Examen> EXAMENES = Arrays.asList(new Examen(5L, "Matematicas"),new Examen(6L,"Geografia"),
            new Examen(7L, "Fisica"));
    public final static List<Examen> EXAMENES_ID_NULL = Arrays.asList(new Examen(null, "Matematicas"),new Examen(null,
                    "Geografia"), new Examen(null, "Fisica"));

    public final static List<String> PREGUNTAS = Arrays.asList("Aritmetica","Integrales","Derivadas","Trigonometria",
            "Geometria");

    public final static Examen EXAMEN = new Examen(null, "Quimica");

    public final static List<Examen> EXAMENES_ID_NEGATIVOS = Arrays.asList(new Examen(-5L, "Matematicas"),
            new Examen(-6L,
                    "Geografia"),
            new Examen(-7L, "Fisica"));
}
