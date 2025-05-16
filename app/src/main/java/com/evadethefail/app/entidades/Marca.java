package com.evadethefail.app.entidades;

import java.util.List;

import io.objectbox.annotation.Backlink;
import io.objectbox.annotation.Entity;
import io.objectbox.relation.ToMany;

@Entity
public class Marca extends Efecto {

    private String marcaNombre;
    private String descripcion;
    private String momento;
    private String metodosAsociados;

    // Lista de Modificadores asociados a esta Marca
    @Backlink(to = "marca")
    private ToMany<Modificador> modificadores;

    // Constructor modificado para tomar un Efecto y extraer las duraciones de este
    public Marca(Efecto efecto, String marcaNombre, String descripcion, String momento, String metodosAsociados) {
        // Llamada al constructor de la clase base Efecto, extrayendo las duraciones del Efecto
        super(efecto.getIdEfecto(), efecto.getNombre(), efecto.getDuracionTurnos(), efecto.getDuracionAtaques(), efecto.getDuracionAtaquesRecibidos(), efecto.getDuracionAcciones());
        this.marcaNombre = marcaNombre;
        this.descripcion = descripcion;
        this.momento = momento;
        this.metodosAsociados = metodosAsociados;
    }

    // Constructor vacío para ObjectBox
    public Marca() {}

    public Marca(Marca marca) {
        super(marca);
        this.metodosAsociados = marca.metodosAsociados;
        this.marcaNombre = marca.marcaNombre;
        this.descripcion = marca.descripcion;
    }

    public String getMarcaNombre() { return marcaNombre; }
    public void setMarcaNombre(String marcaNombre) { this.marcaNombre = marcaNombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getMomento() { return momento; }
    public void setMomento(String momento) { this.momento = momento; }

    public String getMetodosAsociados() { return metodosAsociados; }
    public void setMetodosAsociados(String metodosAsociados) { this.metodosAsociados = metodosAsociados; }

    public List<Modificador> getModificadores() { return modificadores; }
}
