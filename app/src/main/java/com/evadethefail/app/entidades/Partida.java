package com.evadethefail.app.entidades;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToOne;

@Entity
public class Partida {
    @Id
    private long id;

    private int casilla;

    // Relación con el jugador
    private int jugadorId;

    // Relación con el personaje usado en la partida
    private ToOne<Personaje> personaje;

    // Relación con las estadísticas de la partida
    private ToOne<EstadisticasPartida> estadisticas;

    public Partida() {}

    public Partida(int jugadorId, int casilla) {
        this.jugadorId = jugadorId;
        this.casilla = casilla;
    }

    // Getters y setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCasilla() {
        return casilla;
    }

    public void setCasilla(int casilla) {
        this.casilla = casilla;
    }
    public int getJugadorId() {
        return jugadorId;
    }
    public void setJugadorId(int jugadorId) {
        this.jugadorId = jugadorId;
    }

    public ToOne<Personaje> getPersonaje() {
        return personaje;
    }

    public void setPersonaje(Personaje personaje) {
        this.personaje.setTarget(personaje);
    }

    public ToOne<EstadisticasPartida> getEstadisticas() {
        return estadisticas;
    }

    public void setEstadisticas(EstadisticasPartida estadisticas) {
        this.estadisticas.setTarget(estadisticas);
    }

    public void incrementarCasilla() {
        this.casilla++;
    }
}
