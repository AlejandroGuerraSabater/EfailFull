package com.evadethefail.app.entidades;

import java.util.ArrayList;
import java.util.List;

import com.evadethefail.app.vista.CombateFragment;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToMany;

@Entity
public class Enemigo {
    @Id
    private long id;
    private long idEnemigoBase;
    private String nombre;
    private int nivel;
    private String elemento;
    private int casilla;
    private int vida;
    private int vidaRestante;
    private int ataque;
    private int defensa;
    private int bloqueo;
    private double probabilidadCritico;
    private double danioCritico;
    private int ataqueReal;
    private int defensaReal;
    private double probabilidadCriticoReal;
    private double danioCriticoReal;
    private List<Efecto> efectos = new ArrayList<Efecto>();
    private String intencion;

    private ToMany<Ataque> ataques; // Copia la lista de ataques

    public Enemigo() {}

    public Enemigo(EnemigoBase enemigoBase, int nivel) {
        this.idEnemigoBase = enemigoBase.getId();
        this.nombre = enemigoBase.getNombre();
        this.elemento = enemigoBase.getElemento();
        this.casilla = enemigoBase.getCasilla();
        this.vida = enemigoBase.getVida();
        this.vidaRestante = enemigoBase.getVidaRestante();
        this.ataque = enemigoBase.getAtaque();
        this.defensa = enemigoBase.getDefensa();
        this.bloqueo = enemigoBase.getBloqueo();
        this.probabilidadCritico = enemigoBase.getProbabilidadCritico();
        this.danioCritico = enemigoBase.getDanioCritico();
        this.nivel = nivel;
        this.ataques.addAll(enemigoBase.getAtaques()); // Copia los ataques
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getIdEnemigoBase() { return idEnemigoBase; }
    public void setIdEnemigoBase(long idEnemigoBase) { this.idEnemigoBase = idEnemigoBase; }

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
    public void setAtaque(int ataque) { this.ataque = ataque; this.ataqueReal = ataque;}

    public int getBloqueo() { return bloqueo; }
    public void setBloqueo(int bloqueo) { this.bloqueo = bloqueo; }

    public int getDefensa() { return defensa; }
    public void setDefensa(int defensa) { this.defensa = defensa; this.defensaReal = defensa;}

    public double getProbabilidadCritico() { return probabilidadCritico; }
    public void setProbabilidadCritico(double probabilidadCritico) { this.probabilidadCritico = probabilidadCritico; this.probabilidadCriticoReal = probabilidadCritico; }

    public double getDanioCritico() { return danioCritico; }
    public void setDanioCritico(double danoCritico) { this.danioCritico = danoCritico; this.danioCriticoReal = danoCritico;}

    public int getAtaqueReal() {
        return ataqueReal;
    }

    public void setAtaqueReal(int ataqueReal) {
        this.ataqueReal = ataqueReal;
    }

    public int getDefensaReal() {
        return defensaReal;
    }

    public void setDefensaReal(int defensaReal) {
        this.defensaReal = defensaReal;
    }

    public double getProbabilidadCriticoReal() {
        return probabilidadCriticoReal;
    }

    public void setProbabilidadCriticoReal(double probabilidadCriticoReal) {
        this.probabilidadCriticoReal = probabilidadCriticoReal;
    }

    public double getDanioCriticoReal() {
        return danioCriticoReal;
    }

    public void setDanioCriticoReal(double danioCriticoReal) {
        this.danioCriticoReal = danioCriticoReal;
    }

    public List<Efecto> getEfectos() {
        return efectos;
    }

    public void setEfectos(List<Efecto> efectos) {
        this.efectos = efectos;
    }

    public String getIntencion() {
        return intencion;
    }

    public void setIntencion(String intencion) {
        this.intencion = intencion;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    // Método para agregar un efecto
    public void agregarEfecto(CombateFragment combate, Efecto efecto) {
        boolean esBloqueo = false;
        if (efecto instanceof Modificador) {
            Modificador mod = (Modificador) efecto;
            if (mod.getEstadistica().equals("Bloqueo")) {
                this.setBloqueo((int) (this.getBloqueo() + mod.getVariacionPlana()));
                combate.txtBloqueoEnemigo.setText((this.getBloqueo() == 0) ? "" : this.getBloqueo() + "");

            } else {
                switch (mod.getEstadistica()) {

                    case "Vida":
                        this.setVida((int) (this.getVida() - mod.getVariacionPlana()));

                        this.setVidaRestante((int) (this.getVidaRestante() + mod.getVariacionPlana()));
                        this.setVidaRestante((int) (this.getVidaRestante() + this.getVida() * mod.getPorcentaje()/100));
                        break;

                    case "Vida Restante":

                        this.setVidaRestante((int) (this.getVidaRestante() + mod.getVariacionPlana()));
                        this.setVidaRestante((int) (this.getVidaRestante() + this.getVida() * mod.getPorcentaje()/100));
                        if (this.getVidaRestante() > this.getVida()){
                            this.setVidaRestante(this.getVida());
                        } else if (this.getVidaRestante() < 0) {
                            this.setVidaRestante(0);
                        }

                        break;

                    case "Defensa":
                        this.setDefensaReal((int) (this.getDefensaReal() + mod.getVariacionPlana()));
                        this.setDefensaReal((int) (this.getDefensaReal() + this.getDefensa() * mod.getPorcentaje()/100));
                        break;

                    case "Ataque":
                        this.setAtaqueReal((int) (this.getAtaqueReal() + mod.getVariacionPlana()));
                        this.setAtaqueReal((int) (this.getAtaqueReal() + this.getAtaque() * mod.getPorcentaje()/100));
                        break;

                    case "Probabilidad de Crítico":
                        this.setProbabilidadCriticoReal(
                                (int) (this.getProbabilidadCriticoReal() + mod.getVariacionPlana()));
                        this.setProbabilidadCriticoReal((int) (this.getProbabilidadCriticoReal()
                                + this.getProbabilidadCritico() * mod.getPorcentaje()/100));
                        break;

                    case "Dano Crítico":
                        this.setDanioCriticoReal((int) (this.getDanioCriticoReal() + mod.getVariacionPlana()));
                        this.setDanioCriticoReal(
                                (int) (this.getDanioCriticoReal() + this.getDanioCritico() * mod.getPorcentaje()/100));
                        break;

                }
            }
        }
        if (!esBloqueo) {
            this.efectos.add(efecto);
        }
    }

    // Método para remover un efecto
    public void removerEfecto(CombateFragment combate, Efecto efecto) {
        boolean esBloqueo = false;
        if (efecto instanceof Modificador) {
            Modificador mod = (Modificador) efecto;
            if (mod.getEstadistica().equals("Bloqueo")) {
            } else {
                switch (mod.getEstadistica()) {

                    case "Vida":
                        this.setVida((int) (this.getVida() - mod.getVariacionPlana()));

                        this.setVidaRestante((int) (this.getVidaRestante() - mod.getVariacionPlana()));
                        this.setVidaRestante((int) (this.getVidaRestante() - this.getVida() * mod.getPorcentaje()/100));
                        break;

                    case "VidaRestante":

                        this.setVidaRestante((int) (this.getVidaRestante() - mod.getVariacionPlana()));
                        this.setVidaRestante((int) (this.getVidaRestante() - this.getVida() * mod.getPorcentaje()/100));
                        break;

                    case "Defensa":
                        this.setDefensaReal((int) (this.getDefensaReal() - mod.getVariacionPlana()));
                        this.setDefensaReal((int) (this.getDefensaReal() - this.getDefensa() * mod.getPorcentaje()/100));
                        break;

                    case "Ataque":
                        this.setAtaqueReal((int) (this.getAtaqueReal() - mod.getVariacionPlana()));
                        this.setAtaqueReal((int) (this.getAtaqueReal() - this.getAtaque() * mod.getPorcentaje()/100));
                        break;

                    case "Probabilidad de Crítico":
                        this.setProbabilidadCriticoReal(
                                (int) (this.getProbabilidadCriticoReal() - mod.getVariacionPlana()));
                        this.setProbabilidadCriticoReal((int) (this.getProbabilidadCriticoReal()
                                - this.getProbabilidadCritico() * mod.getPorcentaje()/100));
                        break;

                    case "Daño Crítico":
                        this.setDanioCriticoReal((int) (this.getDanioCriticoReal() - mod.getVariacionPlana()));
                        this.setDanioCriticoReal(
                                (int) (this.getDanioCriticoReal() - this.getDanioCritico() * mod.getPorcentaje()/100));
                        break;

                }
            }
        }
        if (!esBloqueo) {
            this.efectos.remove(efecto);
            combate.pintaEfectos(false);
        }
    }

    public List<Ataque> getAtaques() { return ataques; }
    public void setAtaques(List<Ataque> ataques) {
        this.ataques.clear();
        this.ataques.addAll(ataques);
    }

    public void limpiaEfectos(){
        efectos.clear();
        ataqueReal = ataque;
        defensaReal = defensa;
        probabilidadCriticoReal = probabilidadCritico;
        danioCriticoReal = danioCritico;
        bloqueo = 0;
    }

    public void curar(int cantidad) {
        this.vidaRestante += cantidad;

        if (this.vidaRestante > this.vida) {
            this.vidaRestante = this.vida;
        }
    }
}
