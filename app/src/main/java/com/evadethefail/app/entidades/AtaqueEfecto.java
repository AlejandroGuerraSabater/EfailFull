package com.evadethefail.app.entidades;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToOne;

@Entity
public class AtaqueEfecto {
    @Id
    public long id;
    public long idEfecto;
    public String nombreEfecto;
    public String nombreAtaque;

    public ToOne<Ataque> ataque;
    public ToOne<Efecto> efecto;

    public AtaqueEfecto() {}

    public AtaqueEfecto(Ataque ataque, Efecto efecto) {
        this.ataque.setTarget(ataque);
        this.efecto.setTarget(efecto);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ToOne<Ataque> getAtaque() {
        return ataque;
    }

    public void setAtaque(ToOne<Ataque> ataque) {
        this.ataque = ataque;
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

    public String getNombreAtaque() {
        return nombreAtaque;
    }

    public void setNombreAtaque(String nombreAtaque) {
        this.nombreAtaque = nombreAtaque;
    }
}