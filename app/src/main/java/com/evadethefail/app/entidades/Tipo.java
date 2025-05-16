package com.evadethefail.app.entidades;

import io.objectbox.annotation.Backlink;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToMany;

@Entity
public class Tipo {

    /*
    -- Guía de tipos de ataque:
    -- Estado: 0-100
    -- Físicos: 100-300
    -- Mágicos: 300-500
    -- Antiguos: 400-500 (Los ataques antiguos son también tratados como mágicos)
     */
    @Id
    private long id;

    private String categoria;

    private String nombre;

    public Tipo() {}
    @Backlink(to = "tipos")
    private ToMany<Carta> cartas;
    @Backlink(to = "tipos")
    private ToMany<Ataque> ataques;

    public Tipo(String categoria, String nombre) {
        this.categoria = categoria;
        this.nombre = nombre;
    }

    public ToMany<Carta> getCartas() {
        return cartas;
    }

    public ToMany<Ataque> getAtaques() {
        return ataques;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}
