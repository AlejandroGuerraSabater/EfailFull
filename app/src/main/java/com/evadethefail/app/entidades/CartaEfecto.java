package com.evadethefail.app.entidades;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToOne;

@Entity
public class CartaEfecto {
    @Id
    public long id;
    public long idEfecto;
    public String nombreEfecto;
    public String nombreCarta;

    public ToOne<Carta> carta;
    public ToOne<Efecto> efecto;

    // Constructor vacío requerido por ObjectBox
    public CartaEfecto() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ToOne<Carta> getCarta() {
        return carta;
    }

    public void setCarta(ToOne<Carta> carta) {
        this.carta = carta;
    }

    public ToOne<Efecto> getEfecto() {
        return efecto;
    }

    public void setEfecto(ToOne<Efecto> efecto) {
        this.efecto = efecto;
    }

    public long getIdEfecto() {
        return idEfecto;
    }

    public void setIdEfecto(long idEfecto) {
        this.idEfecto = idEfecto;
    }

    public String getNombreEfecto() {
        return nombreEfecto;
    }

    public void setNombreEfecto(String nombreEfecto) {
        this.nombreEfecto = nombreEfecto;
    }

    public String getNombreCarta() {
        return nombreCarta;
    }

    public void setNombreCarta(String nombreCarta) {
        this.nombreCarta = nombreCarta;
    }
}