package com.evadethefail.app.entidades;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class Logro {
    @Id
    private long id;

    private String nombre;
    private String descripcion;
    private String rareza;

    public Logro() {}

    public Logro(String nombre, String descripcion, String rareza) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.rareza = rareza;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getRareza() { return rareza; }
    public void setRareza(String rareza) { this.rareza = rareza; }
}
