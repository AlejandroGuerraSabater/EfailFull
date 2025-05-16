package com.evadethefail.app.entidades;

import io.objectbox.annotation.Backlink;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToMany;
import io.objectbox.relation.ToOne;

@Entity
public class PersonajeBase {
    @Id
    private long id;

    public ToOne<Clase> clase;
    @Backlink(to = "personajeBase")
    public ToMany<Personaje> personajes;

    private int energia;
    private int energiaRestante;
    private int vida;
    private int vidaRestante;
    private int ataque;
    private int defensa;
    private int bloqueo;
    private float probabilidadCritico;
    private float danoCritico;

    public PersonajeBase() {}

    public PersonajeBase(Clase clase, int energia, int vida, int ataque, int defensa, float probabilidadCritico, float danoCritico) {
        this.clase.setTarget(clase);
        this.energia = energia;
        this.energiaRestante = energia;
        this.vida = vida;
        this.vidaRestante = vida;
        this.ataque = ataque;
        this.defensa = defensa;
        this.probabilidadCritico = probabilidadCritico;
        this.danoCritico = danoCritico;
        this.bloqueo = 0;
        this.energiaRestante = energia;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public Clase getClase() { return clase.getTarget(); }
    public void setClase(Clase clase) { this.clase.setTarget(clase); }

    public int getEnergia() { return energia; }
    public void setEnergia(int energia) { this.energia = energia; }

    public int getEnergiaRestante() { return energiaRestante; }
    public void setEnergiaRestante(int energiaRestante) { this.energiaRestante = energiaRestante; }

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

    public float getProbabilidadCritico() { return probabilidadCritico; }
    public void setProbabilidadCritico(float probabilidadCritico) { this.probabilidadCritico = probabilidadCritico; }

    public float getDanoCritico() { return danoCritico; }
    public void setDanoCritico(float danoCritico) { this.danoCritico = danoCritico; }
}
