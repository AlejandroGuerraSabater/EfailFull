package com.evadethefail.app.entidades;

import io.objectbox.annotation.Entity;
import io.objectbox.relation.ToOne;

@Entity
public class Modificador extends Efecto {

    public String estadistica;
    public double porcentaje;
    public int variacionPlana;
    public ToOne<Marca> marca;

    // Constructor modificado para tomar un Efecto y extraer las duraciones de este
    public Modificador(Efecto efecto, String estadistica, double porcentaje, int variacionPlana) {
        // Llamada al constructor de la clase base Efecto, extrayendo las duraciones del Efecto
        super(efecto.getIdEfecto(), efecto.getNombre(), efecto.getDuracionTurnos(), efecto.getDuracionAtaques(), efecto.getDuracionAtaquesRecibidos(), efecto.getDuracionAcciones());
        this.estadistica = estadistica;
        this.porcentaje = porcentaje;
        this.variacionPlana = variacionPlana;
    }

    // Constructor vacío para ObjectBox
    public Modificador() {}

    public Modificador(Modificador modificador) {
        super(modificador);
        this.estadistica = modificador.estadistica;
        this.variacionPlana = modificador.variacionPlana;
        this.porcentaje = modificador.porcentaje;
    }

    public String getEstadistica() { return estadistica; }
    public void setEstadistica(String estadistica) { this.estadistica = estadistica; }

    public double getPorcentaje() { return porcentaje; }
    public void setPorcentaje(double porcentaje) { this.porcentaje = porcentaje; }

    public int getVariacionPlana() { return variacionPlana; }
    public void setVariacionPlana(int variacionPlana) { this.variacionPlana = variacionPlana; }
}
