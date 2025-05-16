package com.evadethefail.app.entidades;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToOne;

@Entity
public class CartasPersonaje {

    @Id
    public long id;

    public ToOne<Personaje> personaje;
    public ToOne<Carta> carta;

    public int cantidad;

    // Constructor vacío requerido por ObjectBox
    public CartasPersonaje() {}

    // Getters y setters
    public long getId() {
        return id;
    }

    public Personaje getPersonaje() {
        return personaje.getTarget();
    }

    public void setPersonaje(Personaje personaje) {
        this.personaje.setTarget(personaje);
    }

    public Carta getCarta() {
        return carta.getTarget();
    }

    public void setCarta(Carta carta) {
        this.carta.setTarget(carta);
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
