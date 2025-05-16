package com.evadethefail.app.entidades;

import io.objectbox.annotation.Backlink;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToMany;

@Entity
public class Clase {
    @Id
    private long id; // ObjectBox requiere un ID numérico único
    private String nombre;
    @Backlink(to = "clase")
    private ToMany<Carta> cartas;

    public Clase() {} // Constructor vacío requerido por ObjectBox

    public Clase(String nombre) {
        this.nombre = nombre;
    }

    public ToMany<Carta> getCartas() {
        return cartas;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}
