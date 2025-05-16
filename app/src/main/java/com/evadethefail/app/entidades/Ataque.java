package com.evadethefail.app.entidades;

import java.util.ArrayList;
import java.util.List;

import com.evadethefail.app.objectBox.Utilidades;
import io.objectbox.annotation.Backlink;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToMany;

@Entity
public class Ataque {
    @Id
    private long id;

    private long idAtaque;

    private String nombre;
    private int objetivo;
    private int probabilidad;
    private int potencia;

    @Backlink(to = "ataques")
    private ToMany<Enemigo> enemigo;

    @Backlink(to = "ataques")
    private ToMany<EnemigoBase> enemigoBase;
    private ToMany<Tipo> tipos;     // Relación con múltiples tipos
    // Relación con múltiples AtaqueEfecto
    @Backlink(to = "ataque")
    private ToMany<AtaqueEfecto> ataqueEfectos;

    public Ataque() {}

    public Ataque(Ataque ataque) {
        this.idAtaque = ataque.getIdAtaque();
        this.nombre = ataque.nombre;
        this.potencia = ataque.potencia;
        this.probabilidad = ataque.probabilidad;
        this.objetivo = ataque.objetivo;

        // Copiar tipos
        if (ataque.getTipos() != null) {
            for (Tipo tipo : ataque.getTipos()) {
                this.getTipos().add(tipo);
            }
        }
    }


    public Ataque(long idAtaque, String nombre, int objetivo, int probabilidad, int potencia) {
        this.idAtaque = idAtaque;
        this.nombre = nombre;
        this.objetivo = objetivo;
        this.probabilidad = probabilidad;
        this.potencia = potencia;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getIdAtaque() {
        return idAtaque;
    }

    public void setIdAtaque(long idAtaque) {
        this.idAtaque = idAtaque;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public ToMany<Enemigo> getEnemigo() {
        return enemigo;
    }

    public ToMany<EnemigoBase> getEnemigoBase() {
        return enemigoBase;
    }

    public int getObjetivo() { return objetivo; }
    public void setObjetivo(int objetivo) { this.objetivo = objetivo; }

    public int getProbabilidad() { return probabilidad; }
    public void setProbabilidad(int probabilidad) { this.probabilidad = probabilidad; }

    public int getPotencia() { return potencia; }
    public void setPotencia(int potencia) { this.potencia = potencia; }

    public List<Tipo> getTipos() { return tipos; }
    public List<String> getTiposNombre() {

        List<String> nombreTipos = new ArrayList<String>();

        for (Tipo tipo : tipos){
            nombreTipos.add(tipo.getNombre());
        }

        return nombreTipos;
    }

    public List<String> getTiposCategoria() {

        List<String> nombreTipos = new ArrayList<String>();

        for (Tipo tipo : tipos){
            nombreTipos.add(tipo.getCategoria());
        }

        return nombreTipos;
    }

    public List<String> getTiposCompleto(){

        List<String> nombreTipos = new ArrayList<String>();

        nombreTipos.addAll(this.getTiposCategoria());
        nombreTipos.addAll(this.getTiposNombre());

        return nombreTipos;
    }

    public ToMany<AtaqueEfecto> getAtaqueEfectos() {
        return ataqueEfectos;
    }

    public void setEfecto(AtaqueEfecto ataqueEfecto) {
        this.getAtaqueEfectos().add(ataqueEfecto);
    }

    public void asignaTipos(Tipo... tipos) {
        for (Tipo tipo : tipos) {
            this.getTipos().add(tipo);
        }
    }

    public void asignaEfecto(AtaqueEfecto ae, Efecto efecto) {
        ae.getAtaque().setTarget(this);
        ae.getEfecto().setTarget(efecto);
        ae.setIdEfecto(efecto.getIdEfecto());
        ae.setNombreAtaque(this.getNombre());
        ae.setNombreEfecto(efecto.getNombre());
    }

    public List<Efecto> getEfectos() {
        List<Efecto> efectosList = new ArrayList<>();
        if (this.ataqueEfectos != null) {
            List<Integer> idsEfectos = new ArrayList<Integer>();
            for (AtaqueEfecto ae : this.ataqueEfectos) {
                if (!idsEfectos.contains((int) ae.getIdEfecto()))
                    idsEfectos.add((int) ae.getIdEfecto());
            }

            for (int idEfecto : idsEfectos) {
                efectosList.addAll(Utilidades.buscaEfectos(idEfecto));
            }

        }
        return efectosList;
    }

}
