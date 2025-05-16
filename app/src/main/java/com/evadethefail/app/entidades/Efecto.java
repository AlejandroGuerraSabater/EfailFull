package com.evadethefail.app.entidades;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class Efecto {
    @Id
    private long id;
    private long idEfecto;
    private String nombre;
    private int duracionTurnos;
    private int duracionAtaques;
    private int duracionAtaquesRecibidos;
    private int duracionAcciones;
/*
    @Backlink(to = "efectos")
    private ToMany<Carta> cartas;
    @Backlink(to = "efectos")
    private ToMany<Ataque> ataques;
*/
    public Efecto() {}

    public Efecto(long idEfecto, String nombre, int duracionTurnos, int duracionAtaques, int duracionAtaquesRecibidos, int duracionAcciones) {
        this.idEfecto = idEfecto;
        this.nombre = nombre;
        this.duracionTurnos = duracionTurnos;
        this.duracionAtaques = duracionAtaques;
        this.duracionAtaquesRecibidos = duracionAtaquesRecibidos;
        this.duracionAcciones = duracionAcciones;
    }

    public Efecto(Efecto efecto) {
        this.idEfecto = efecto.getIdEfecto();
        this.nombre = efecto.nombre;
        this.duracionTurnos = efecto.duracionTurnos;
        this.duracionAtaques = efecto.duracionAtaques;
        this.duracionAcciones = efecto.duracionAcciones;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getIdEfecto() {
        return idEfecto;
    }

    public void setIdEfecto(long idEfecto) {
        this.idEfecto = idEfecto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMarcaNombre() { return nombre; }
    public void setMarcaNombre(String marcaNombre) { this.nombre = marcaNombre; }

    public int getDuracionTurnos() { return duracionTurnos; }
    public void setDuracionTurnos(int duracionTurnos) { this.duracionTurnos = duracionTurnos; }

    public int getDuracionAtaques() { return duracionAtaques; }
    public void setDuracionAtaques(int duracionAtaques) { this.duracionAtaques = duracionAtaques; }

    public int getDuracionAcciones() { return duracionAcciones; }
    public void setDuracionAcciones(int duracionAcciones) { this.duracionAcciones = duracionAcciones; }

    public int getDuracionAtaquesRecibidos() {
        return duracionAtaquesRecibidos;
    }

    public void setDuracionAtaquesRecibidos(int duracionAtaquesRecibidos) {
        this.duracionAtaquesRecibidos = duracionAtaquesRecibidos;
    }

    public String[] getDuracion() {
        if (duracionTurnos > 0) {
            if (duracionTurnos == 1){
                return new String[]{duracionTurnos + "", "turno"};
            }
            return new String[]{duracionTurnos + "", "turnos"};
        } else if (duracionAtaques > 0) {
            if (duracionAtaques == 1){
                return new String[]{duracionAtaques + "", "ataque"};
            }
            return new String[]{duracionAtaques + "", "ataques"};
        } else if (duracionAcciones > 0) {
            if(duracionAcciones == 1){
                return new String[]{duracionAcciones + "", "accion"};
            }
            return new String[]{duracionAcciones + "", "acciones"};
        } else {
            if (duracionAtaquesRecibidos == 1){
                return new String[]{duracionAtaquesRecibidos + "", "ataque recibido"};
            }
            return new String[]{duracionAtaquesRecibidos + "", "ataques recibidos"};
        }
    }

}
