package com.evadethefail.app.entidades;

import java.util.List;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToMany;

@Entity
public class EnemigoBase {
    @Id
    private long id;

    private String nombre;
    private String elemento;
    private int casilla;
    private int vida;
    private int vidaRestante;
    private int ataque;
    private int defensa;
    private int bloqueo;
    private double probabilidadCritico;
    private double danioCritico;

    private ToMany<Ataque> ataques; // Relación con múltiples ataques

    public EnemigoBase() {}

    public EnemigoBase(String nombre, String elemento, int casilla, int vida, int vidaRestante, int ataque, int defensa, int bloqueo, double probabilidadCritico, double danioCritico) {
        this.nombre = nombre;
        this.elemento = elemento;
        this.casilla = casilla;
        this.vida = vida;
        this.vidaRestante = vidaRestante;
        this.ataque = ataque;
        this.defensa = defensa;
        this.bloqueo = bloqueo;
        this.probabilidadCritico = probabilidadCritico;
        this.danioCritico = danioCritico;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getElemento() { return elemento; }
    public void setElemento(String elemento) { this.elemento = elemento; }

    public int getCasilla() { return casilla; }
    public void setCasilla(int casilla) { this.casilla = casilla; }

    public int getVida() { return vida; }
    public void setVida(int vida) { this.vida = vida; }

    public int getVidaRestante() { return vidaRestante; }
    public void setVidaRestante(int vidaRestante) { this.vidaRestante = vidaRestante; }

    public int getAtaque() { return ataque; }
    public void setAtaque(int ataque) { this.ataque = ataque; }

    public int getDefensa() { return defensa; }
    public void setDefensa(int defensa) { this.defensa = defensa; }

    public int getBloqueo() { return bloqueo; }
    public void setBloqueo(int bloqueo) { this.bloqueo = bloqueo; }

    public double getProbabilidadCritico() { return probabilidadCritico; }
    public void setProbabilidadCritico(double probabilidadCritico) { this.probabilidadCritico = probabilidadCritico; }

    public double getDanioCritico() { return danioCritico; }
    public void setDanioCritico(double danioCritico) { this.danioCritico = danioCritico; }

    public List<Ataque> getAtaques() { return ataques; }
}
