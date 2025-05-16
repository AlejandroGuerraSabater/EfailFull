package com.evadethefail.app.entidades;

import java.util.List;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToMany;
import io.objectbox.relation.ToOne;

@Entity
public class Jugador {
    @Id
    private long id;

    private String nombre;
    private String contrasena;
    private ToMany<Logro> logros; // Relación con múltiples logros
    private ToOne<EstadisticasJugador> estadisticas; // Relación con múltiples logros

    // Estadísticas del jugador

    public Jugador() {}

    public Jugador(String nombre, String contrasena) {
        this.nombre = nombre;
        this.contrasena = contrasena;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public List<Logro> getLogros() { return logros; }

    public void setLogros(ToMany<Logro> logros) {
        this.logros = logros;
    }

    public ToOne<EstadisticasJugador> getEstadisticas() {
        return estadisticas;
    }

    public void setEstadisticas(ToOne<EstadisticasJugador> estadisticas) {
        this.estadisticas = estadisticas;
    }
}